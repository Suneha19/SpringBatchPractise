package com.qiwkreport.qiwk.etl.configuration;


import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;
import com.qiwkreport.qiwk.etl.processor.EmployeeProcessor;
import com.qiwkreport.qiwk.etl.reader.Reader;
import com.qiwkreport.qiwk.etl.util.ColumnRangePartitioner;
import com.qiwkreport.qiwk.etl.writer.Writer;

@Configuration
@EnableBatchProcessing
@Import(BatchJobConfiguration.class)
public class EmployeeJobConfiguration{

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private TaskExecutorConfiguration taskExecutorConfiguration;

	@Autowired
	private Reader reader;
	
	@Autowired
	private Writer writer;
	
	@Value("${data.chunk.size}")
	private int chunkSize;
	
	@Value("${partition.grid.size}")
	private int gridSize;
	
	@Bean
	public Job employeeJob() throws Exception {
		return jobBuilderFactory.get("EmployeeJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeMasterStep())
				.build();
	}

	@Bean
	public Step employeeMasterStep() throws Exception {
		return stepBuilderFactory.get("employeeMasterStep")
				.partitioner(employeeSlaveStep().getName(), columnRangePartitioner())
				.partitionHandler(employeeMasterSlaveHandler())
				.build();
	}
	
	@Bean
	public Step employeeSlaveStep() throws Exception {
		return stepBuilderFactory.get("employeeSlaveStep")
				.<Employee, NewEmployee>chunk(chunkSize)
				.reader(reader.employeeReader(null, null, null))
				.processor(employeeProcessor())
				.writer(writer.employeeWriter())
				.build();
	}
	
	@Bean
	public PartitionHandler employeeMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(gridSize);
		handler.setTaskExecutor(taskExecutor());
		handler.setStep(employeeSlaveStep());
		handler.afterPropertiesSet();
		
		return handler;
	}

	@Bean
	public ColumnRangePartitioner columnRangePartitioner() {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(dataSource);
		partitioner.setTable("EMPLOYEE");
		return partitioner;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return taskExecutorConfiguration.taskExecutor();
	}

	@Bean
	@StepScope
	public EmployeeProcessor employeeProcessor() {
		return new EmployeeProcessor();
	}

}


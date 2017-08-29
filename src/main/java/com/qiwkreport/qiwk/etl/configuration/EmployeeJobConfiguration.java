package com.qiwkreport.qiwk.etl.configuration;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;
import com.qiwkreport.qiwk.etl.processor.EmployeeProcessor;
import com.qiwkreport.qiwk.etl.util.ColumnRangePartitioner;
import com.qiwkreport.qiwk.etl.writer.JpaEmployeeItemWriter;

/**
 * This is configurations class for EmployeeJoB, this class is responsible for moving records from
 * Employee table to NewEmployee table
 * 
 * @author Abhilash
 *
 */

@Configuration
@EnableBatchProcessing
@Import(BatchJobConfiguration.class)
public class EmployeeJobConfiguration{
	
	@Autowired
	private QiwkJobsConfiguration configuration;

	@Bean
	public Job employeeJob() throws Exception {
		return configuration.getJobBuilderFactory()
				.get("EmployeeJob")
				.incrementer(new RunIdIncrementer())
				.start(employeeMasterStep())
				.build();
	}

	@Bean
	public Step employeeMasterStep() throws Exception {
		return configuration.getStepBuilderFactory()
				.get("employeeMasterStep")
				.partitioner(employeeSlaveStep().getName(), columnRangePartitioner())
				.partitionHandler(employeeMasterSlaveHandler())
				.build();
	}
	
	@Bean
	public Step employeeSlaveStep() throws Exception {
		return configuration.getStepBuilderFactory()
				.get("employeeSlaveStep")
				.<Employee, NewEmployee>chunk(configuration.getChunkSize())
				.reader(jpaEmployeeReader(null, null, null))
				.processor(employeeProcessor())
				.writer(jpaEmployeeItemWriter())
				.build();
	}
	
	@Bean
	public PartitionHandler employeeMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(configuration.getGridSize());
		handler.setTaskExecutor(taskExecutor());
		handler.setStep(employeeSlaveStep());
		handler.afterPropertiesSet();
		
		return handler;
	}

	@Bean
	public ColumnRangePartitioner columnRangePartitioner() {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(configuration.getDataSource());
		partitioner.setTable("EMPLOYEE");
		return partitioner;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return configuration.getTaskExecutorConfiguration()
				.taskExecutor();
	}

	@Bean
	public EmployeeProcessor employeeProcessor() {
		return new EmployeeProcessor();
	}
	
	/**
	 * {@code} The @StepScope annotation is very imp, as this instantiate this
	 * bean in spring context only when this is loaded
	 */
	@Bean
	@StepScope
	public JpaPagingItemReader<Employee> jpaEmployeeReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JpaPagingItemReader<Employee> reader = new JpaPagingItemReader<Employee>();
		reader.setPageSize(configuration.getChunkSize());
		reader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		reader.setQueryString("FROM Employee o where o.id>=" + fromId + " and o.id <= " + toId +" order by o.id ASC");
		reader.setSaveState(false);
		reader.afterPropertiesSet();
		return reader;
	}
	
	@Bean
	public ItemWriter<NewEmployee> jpaEmployeeItemWriter() {
		return new JpaEmployeeItemWriter();
	}

}


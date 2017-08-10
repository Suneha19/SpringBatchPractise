/**
 * 
 */
package com.qiwkreport.qiwk.etl.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;
import com.qiwkreport.qiwk.etl.processor.EmployeeProcessor;
import com.qiwkreport.qiwk.etl.util.ColumnRangePartitioner;
import com.qiwkreport.qiwk.etl.writer.EmployeeWriter;

/**
 * @author Abhilash
 *
 */
@Configuration
public class JobConfiguration implements ApplicationContextAware{

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private TaskExecutorConfiguration taskExecutorConfiguration;
	
	@Autowired
	private EmployeeWriter employeeWriter; 
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobRegistry jobRegistry;
	
	@Autowired
	private JobLauncher jobLauncher;

	private ApplicationContext applicationContext;
	
	@Value("${data.chunk.size}")
	private int chunkSize;
	
	@Value("${partition.grid.size}")
	private int gridSize;
	
    @Value("#{'${qiwk.etl.jobs}'.split(',')}") 
	private List<String> qiwkJobs;
	
	@Bean
	public JobRegistryBeanPostProcessor jobRegistrar() throws Exception{
		JobRegistryBeanPostProcessor registrar=new JobRegistryBeanPostProcessor();
		registrar.setJobRegistry(this.jobRegistry);
		registrar.setBeanFactory(this.applicationContext.getAutowireCapableBeanFactory());
		registrar.afterPropertiesSet();
		return registrar;
	}
	
	@Bean
	public JobOperator jobOperator() throws Exception{
		SimpleJobOperator simpleJobOperator=new SimpleJobOperator();
		simpleJobOperator.setJobLauncher(this.jobLauncher);
		simpleJobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
		simpleJobOperator.setJobRepository(this.jobRepository);
		simpleJobOperator.setJobExplorer(this.jobExplorer);
		simpleJobOperator.setJobRegistry(this.jobRegistry);
		
		simpleJobOperator.afterPropertiesSet();
		return simpleJobOperator;
		
	}
	
	/** 
	 * @category JdbcPagingitemReader
	 * We are using JdbcPagingitemReader for reading data from source database.
	 * This supports multiple threads can read from the data source at the same
	 * time. It is also going to track the last key that was read.
	 */

	@Bean
	public ColumnRangePartitioner partitioner() {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(this.dataSource);
		partitioner.setTable("Employee");
		return partitioner;
	}

	@Bean
	public Step masterStep() {
		return stepBuilderFactory.get("masterStep")
				.partitioner(slaveStep().getName(), partitioner())
				.step(slaveStep())
				.gridSize(gridSize)
				.taskExecutor(taskExecutorConfiguration.taskExecutor())
				.build();
	}

	@Bean
	public Step slaveStep() {
		return stepBuilderFactory.get("slaveStep")
				.<Employee, NewEmployee>chunk(chunkSize)
				.reader(pagingItemReader(null, null))
				.processor(employeeProcessor())
				.writer(employeeWriter.customItemWriter())
				.build();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("FR")
				.start(masterStep())
				.build();
	}
	
	@Bean
	public ItemProcessor<Employee, NewEmployee> employeeProcessor() {
		return new EmployeeProcessor();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	
	@Bean
	@StepScope
	public JdbcPagingItemReader<Employee> pagingItemReader(@Value("#{stepExecutionContext['minValue']}") Long minvalue,
			@Value("#{stepExecutionContext['maxValue']}") Long maxvalue) {

		JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReader<Employee>();
		reader.setDataSource(this.dataSource);
		// this should be equal to chunk size for the performance reasons.
		reader.setFetchSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
			return new Employee(resultSet.getLong("id"), 
					resultSet.getString("firstName"),
					resultSet.getString("lastName"));
		});

		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("id, firstName, lastName");
		provider.setFromClause("from Employee");
		provider.setWhereClause("where id>=" + minvalue + " and id < " + maxvalue);

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		reader.setQueryProvider(provider);

		return reader;
	}
}
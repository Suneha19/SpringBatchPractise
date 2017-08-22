package com.qiwkreport.qiwk.etl.configuration;


import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;
import com.qiwkreport.qiwk.etl.processor.Processor;
import com.qiwkreport.qiwk.etl.reader.Reader;
import com.qiwkreport.qiwk.etl.util.ColumnRangePartitioner;
import com.qiwkreport.qiwk.etl.writer.Writer;

@Configuration
public class FRJobConfiguration implements ApplicationContextAware{

  private static final Logger LOGGER = LoggerFactory.getLogger(FRJobConfiguration.class);

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private TaskExecutorConfiguration taskExecutorConfiguration;

	@Autowired
	private JobExplorer jobExplorer;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobRegistry jobRegistry;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Reader reader;

	@Autowired
	private Writer writer;

	private ApplicationContext applicationContext;
  
	@Value("${data.chunk.size}")
	private int chunkSize;
	
	@Value("${partition.grid.size}")
	private int gridSize;
	
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

	@Bean
	public Job FR() throws Exception {
		return jobBuilderFactory.get("FR")
				.incrementer(new RunIdIncrementer())
				.start(slaveStep())
				.build();
	}

	@Bean
	public Step masterStep() throws Exception {
		return stepBuilderFactory.get("masterStep")
				.partitioner(slaveStep().getName(), columnRangePartitioner())
				.partitionHandler(masterSlaveHandler())
				.build();
	}

	@Bean
	public PartitionHandler masterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(gridSize);
		handler.setTaskExecutor(taskExecutor());
		handler.setStep(slaveStep());
		handler.afterPropertiesSet();
		return handler;
	}

	@Bean
	public Step slaveStep() {
		return stepBuilderFactory.get("slave").<Employee, NewEmployee>chunk(chunkSize)
				.reader(reader.slaveReader(null, null, null))
				.processor(slaveProcessor())
				.writer(writer.slaveWriter())
				.build();
	}

	@Bean
	public ColumnRangePartitioner columnRangePartitioner() {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(dataSource);
		partitioner.setTable("Employee");
		return partitioner;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return taskExecutorConfiguration.taskExecutor();
	}

	@Bean
	@StepScope
	public Processor slaveProcessor() {
		return new Processor();
	}

/*	@Bean
	@StepScope
	public JdbcPagingItemReader<Employee> slaveReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) {

		JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		
		reader.setQueryProvider(queryProvider());
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("fromId", fromId);
		parameterValues.put("toId", toId);
		reader.setParameterValues(parameterValues);
		reader.setPageSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
			return new Employee(resultSet.getLong("id"), 
					resultSet.getString("firstName"),
					resultSet.getString("lastName"));
		});
		LOGGER.info("slaveReader end " + fromId + " " + toId);
		return reader;
	}

	@Bean
	public PagingQueryProvider queryProvider() {
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("id, firstName, lastName");
		provider.setFromClause("from Employee");
		provider.setWhereClause("where id >= :fromId and id <= :toId");
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);
		return provider;
	}

	@StepScope
	@Bean
	public ItemWriter<NewEmployee> slaveWriter() {
		JdbcBatchItemWriter<NewEmployee> writer = new JdbcBatchItemWriter<NewEmployee>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT INTO NEWEMPLOYEE values (:id, :firstName ,:lastName)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.afterPropertiesSet();
		return writer;
	}*/

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}


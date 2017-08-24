package com.qiwkreport.qiwk.etl.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
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

import com.qiwkreport.qiwk.etl.domain.NewUser;
import com.qiwkreport.qiwk.etl.domain.OldUser;
import com.qiwkreport.qiwk.etl.processor.UserProcessor;
import com.qiwkreport.qiwk.etl.reader.UserReader;
import com.qiwkreport.qiwk.etl.util.UserRangePartitioner;
import com.qiwkreport.qiwk.etl.writer.UserWriter;

/**
 * @author Abhilash
 *
 */
@Configuration
public class UserJobConfiguration implements ApplicationContextAware{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserJobConfiguration.class);

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

	private ApplicationContext applicationContext;
	
	@Autowired
	private UserWriter userWriter; 
	
	@Autowired
	private UserReader userReader; 
	
	@Value("${data.chunk.size}")
	private int chunkSize;
	
	@Value("${partition.grid.size}")
	private int gridSize;
	
	@Bean
	public JobRegistryBeanPostProcessor userJobRegistrar() throws Exception{
		JobRegistryBeanPostProcessor registrar=new JobRegistryBeanPostProcessor();
		registrar.setJobRegistry(this.jobRegistry);
		registrar.setBeanFactory(this.applicationContext.getAutowireCapableBeanFactory());
		registrar.afterPropertiesSet();
		return registrar;
	}
	
	@Bean
	public JobOperator userJobOperator() throws Exception{
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
	public UserRangePartitioner userPartitioner() {
		UserRangePartitioner partitioner = new UserRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(this.dataSource);
		partitioner.setTable("OldUser");
		return partitioner;
	}

	@Bean
	public Step userMasterStep() throws Exception {
		return stepBuilderFactory
				.get("userMasterStep")
				.partitioner(userSlaveStep().getName(), userPartitioner())
				.partitionHandler(userMasterSlaveHandler())
	            .build();
	}

	@Bean
	public PartitionHandler userMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(gridSize);
		handler.setTaskExecutor(taskExecutorConfiguration.taskExecutor());
		handler.setStep(userSlaveStep());
		handler.afterPropertiesSet();
		return handler;
	}

	@Bean
	public Step userSlaveStep() throws Exception {
		return stepBuilderFactory.get("userSlaveStep")
				.<OldUser, NewUser>chunk(chunkSize)
				.reader(userReader.userItemReader(null, null, null))
			    .processor(userProcessor())
				.writer(userWriter.userItemWriter())
				.build();
	}

	@Bean
	public Job userJob() throws Exception {
		return jobBuilderFactory.get("UserJob")
				.incrementer(new RunIdIncrementer())
				.start(userMasterStep())
				.build();
	}
	
	
	@Bean
	public ItemProcessor<OldUser, NewUser> userProcessor() {
		return new UserProcessor();
	}
	/*
	@Bean
	public void oldUserReader() throws Exception {
		return new UserReader().userItemReader(null, null, null);
	}
	
	@Bean
	public ItemWriter<NewUser> oldUserWriter() {
		return new UserWriter().userItemWriter();
	}*/

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	/** 
	 * @throws Exception 
	 * @category JdbcPagingitemReader
	 * We are using JdbcPagingitemReader for reading data from source database.
	 * This supports multiple threads can read from the data source at the same
	 * time. It is also going to track the last key that was read.
	 */
	
/*	@Bean
	@StepScope
	public JdbcPagingItemReader<OldUser> userItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
		      @Value("#{stepExecutionContext[toId]}") final String toId,
		      @Value("#{stepExecutionContext[name]}") final String name) throws Exception {
		JdbcPagingItemReader<OldUser> reader = new JdbcPagingItemReader<OldUser>();
		reader.setDataSource(this.dataSource);
		// this should be equal to chunk size for the performance reasons.
		reader.setFetchSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
			return new OldUser(resultSet.getInt("id"), 
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getInt("age"));
		});

		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("id, username, password,age");
		provider.setFromClause("from OLDUSER");
		provider.setWhereClause("where id>=" + fromId + " and id <= " + toId);
		
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		reader.setQueryProvider(provider);
		reader.afterPropertiesSet();
		return reader;
	}
	
	@Bean
	@StepScope
	public JdbcPagingItemReader<OldUser> userItemReaderWithoutPartition() throws Exception{
		JdbcPagingItemReader<OldUser> reader = new JdbcPagingItemReader<OldUser>();
		reader.setDataSource(this.dataSource);
		// this should be equal to chunk size for the performance reasons.
		reader.setFetchSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
			return new OldUser(resultSet.getInt("id"), 
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getInt("age"));
		});

		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("id, username, password, age");
		provider.setFromClause("from OLDUSER");
		
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		reader.setQueryProvider(provider);
		reader.afterPropertiesSet();
		return reader;
	}
	
	@StepScope
	@Bean
	public ItemWriter<NewUser> userItemWriter() {
		JdbcBatchItemWriter<NewUser> writer = new JdbcBatchItemWriter<NewUser>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT INTO NEWUSER values (:id, :username ,:password, :age)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.afterPropertiesSet();
		return writer;
	}*/
}
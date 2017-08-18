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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.EmployeeProcessor;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;
import com.qiwkreport.qiwk.etl.util.ColumnRangePartitioner;

@Configuration
@EnableBatchProcessing
public class PartitionerJobConfiguration implements ApplicationContextAware{

  private static final Logger LOGGER = LoggerFactory.getLogger(PartitionerJobConfiguration.class);
  

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
  public Job FR() {
    return jobBuilderFactory
    		.get("FR")
    		.incrementer(new RunIdIncrementer())
            .start(masterStep())
    //        .next(step2())
            .build();
  }

/*  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2").tasklet(dummyTask()).build();
  }

  @Bean
  public DummyTasklet dummyTask() {
    return new DummyTasklet();
  }*/

  @Bean
  public Step masterStep() {
    return stepBuilderFactory
    		.get("masterStep")
    		//.partitioner(slave().getName(), rangePartitioner())
    		.partitioner(slave().getName(), columnRangePartitioner())
            .partitionHandler(masterSlaveHandler())
            .build();
  }

  @Bean
  public PartitionHandler masterSlaveHandler() {
    TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
    handler.setGridSize(gridSize);
    handler.setTaskExecutor(taskExecutor());
    handler.setStep(slave());
    try {
      handler.afterPropertiesSet();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return handler;
  }

  @Bean(name = "slave")
  public Step slave() {
	  LOGGER.info("...........called slave .........");

    return stepBuilderFactory.get("slave").<Employee, NewEmployee>chunk(chunkSize)
        .reader(slaveReader(null, null, null))
        .processor(slaveProcessor(null))
        .writer(slaveWriter())
        .build();
  }

 /* @Bean
  public RangePartitioner rangePartitioner() {
    return new RangePartitioner();
  }*/
  
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
  public EmployeeProcessor slaveProcessor(@Value("#{stepExecutionContext[name]}") String name) {
    LOGGER.info("********called slave processor **********");
    EmployeeProcessor employee = new EmployeeProcessor();
    employee.setThreadName(name);
    return employee;
  }

  @Bean
  @StepScope
  public JdbcPagingItemReader<Employee> slaveReader(
      @Value("#{stepExecutionContext[fromId]}") final String fromId,
      @Value("#{stepExecutionContext[toId]}") final String toId,
      @Value("#{stepExecutionContext[name]}") final String name) {
    LOGGER.info("slaveReader start " + fromId + " " + toId);
    JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReader<>();
    reader.setDataSource(dataSource);
    reader.setQueryProvider(queryProvider());
    Map<String, Object> parameterValues = new HashMap<>();
    parameterValues.put("fromId", fromId);
    parameterValues.put("toId", toId);
    LOGGER.info("Parameter Value " + name + " " + parameterValues);
    reader.setParameterValues(parameterValues);
    reader.setPageSize(10);
    reader.setRowMapper(new BeanPropertyRowMapper<Employee>() {{
      setMappedClass(Employee.class);
    }});
    LOGGER.info("slaveReader end " + fromId + " " + toId);
    return reader;
  }

  @Bean
  public PagingQueryProvider queryProvider() {
	  LOGGER.info("queryProvider start ");
   // SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
    OraclePagingQueryProvider provider=new OraclePagingQueryProvider();
    //provider.setDataSource(dataSource);
    provider.setSelectClause("select id, firstName, lastName");
    provider.setFromClause("from Employee");
    provider.setWhereClause("where id >= :fromId and id <= :toId");
    Map<String, Order> sortKeys = new HashMap<>(1);
	sortKeys.put("id", Order.ASCENDING);
	provider.setSortKeys(sortKeys);

    LOGGER.info("queryProvider end ");
    try {
      return provider;
    } catch (Exception e) {
      LOGGER.info("queryProvider exception ");
      e.printStackTrace();
    }

    return null;
  }

  @StepScope
	@Bean
	public ItemWriter<NewEmployee> slaveWriter() {
		JdbcBatchItemWriter<NewEmployee> writer = new JdbcBatchItemWriter<NewEmployee>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT INTO NEWEMPLOYEE values (:id, :firstName ,:lastName)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.afterPropertiesSet();
		System.out.println("writer---->"+writer);
		return writer;
	}
  
  @Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
}


package com.qiwkreport.qiwk.etl.job;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.qiwkreport.qiwk.etl.common.BatchJobConfiguration;
import com.qiwkreport.qiwk.etl.common.ColumnRangePartitioner;
import com.qiwkreport.qiwk.etl.common.QiwkJobsConfiguration;
import com.qiwkreport.qiwk.etl.domain.Department;
import com.qiwkreport.qiwk.etl.domain.NewDepartment;
import com.qiwkreport.qiwk.etl.processor.DepartmentProcessor;
import com.qiwkreport.qiwk.etl.writer.JpaDepartmentItemWriter;

/**
 * This is configurations class for Department table, this class is responsible for moving records from
 * Department table to NewDepartment table
 * 
 * @author Abhilash
 *
 */

@Configuration
@EnableBatchProcessing
@Import(BatchJobConfiguration.class)
public class DepartmentJob{
	
	@Autowired
	private QiwkJobsConfiguration configuration;
	
	/**
	 * This job needed any more as Employee table is linked to department table. when employee table record will 
	 * move department table record itself will move.
	 * 
	 * @return
	 * @throws Exception
	 */
	
	//@Bean
	public Job departmentJob() throws Exception {
		return configuration.getJobBuilderFactory()
				.get("DepartmentJob")
				.incrementer(new RunIdIncrementer())
				.start(departmentMasterStep())
				.build();
	}

	@Bean
	public Step departmentMasterStep() throws Exception {
		return configuration.getStepBuilderFactory()
				.get("departmentMasterStep")
				.partitioner(departmentSlaveStep().getName(), columnRangePartitioner())
				.partitionHandler(departmentMasterSlaveHandler())
				.build();
	}
	
	@Bean
	public Step departmentSlaveStep() throws Exception {
		return configuration.getStepBuilderFactory()
				.get("departmentSlaveStep")
				.<Department, NewDepartment>chunk(configuration.getChunkSize())
				.reader(jpaDepartmentReader(null, null, null))
				.processor(departmentProcessor())
				.writer(jpaDepartmentItemWriter())
				.build();
	}
	
	@Bean
	public PartitionHandler departmentMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(configuration.getGridSize());
		handler.setTaskExecutor(taskExecutor());
		handler.setStep(departmentSlaveStep());
		handler.afterPropertiesSet();
		
		return handler;
	}

	@Bean
	public ColumnRangePartitioner columnRangePartitioner() {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(configuration.getDataSource());
		partitioner.setTable("DEPARTMENT");
		return partitioner;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return configuration.getTaskExecutorConfiguration()
				.taskExecutor();
	}

	@Bean
	public DepartmentProcessor departmentProcessor() {
		return new DepartmentProcessor();
	}
	
	
	@Bean
	public ItemWriter<NewDepartment> jpaDepartmentItemWriter() {
		return new JpaDepartmentItemWriter<NewDepartment>();
	}
	
	@StepScope
	@Bean
	public ItemWriter<NewDepartment> hibernateDepartmentItemWriter() throws IOException {
	        HibernateItemWriter<NewDepartment> itemWriter = new HibernateItemWriter<>();
	        itemWriter.setSessionFactory(sessionFactory().getObject());
	        return itemWriter;
	}

	/**
	 * Below methods contains various reader for reading from DB, we have Hibernate based reader,
	 * we have JDBC based readers with & without partitioning, & then 
	 * we have JPA based reader.
	 * We need to compare the performances of all 4 & use the one which suits Best.
	 */
	
	
	/**
	 * JPA Based reader with partitioning 
	 * 
	 * @param fromId
	 * @param toId
	 * @param name
	 * @return
	 * @throws Exception
	 */
	
	@Bean
	@StepScope
	public JpaPagingItemReader<Department> jpaDepartmentReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JpaPagingItemReader<Department> reader = new JpaPagingItemReader<Department>();
		reader.setPageSize(configuration.getChunkSize());
		reader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		reader.setQueryString("FROM Department o where o.id>=" + fromId + " and o.id <= " + toId +" order by o.id ASC");
		reader.setSaveState(false);
		reader.afterPropertiesSet();
		return reader;
	}
	

	/**
	 * JPA Based reader without partitioning 
	 * 
	 * @param fromId
	 * @param toId
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Bean
	@StepScope
	public JpaPagingItemReader<Department> jpaDepartmentReaderWithoutPartitioning() throws Exception {

		JpaPagingItemReader<Department> reader = new JpaPagingItemReader<Department>();
		reader.setPageSize(configuration.getChunkSize());
		reader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		reader.setQueryString("FROM Department");
		reader.setSaveState(false);
		reader.afterPropertiesSet();
		return reader;
	}
	
	
	/**
	 * Hibernate based reader without partitioning
	 * 
	 * @param fromId
	 * @param toId
	 * @param name
	 * @return
	 * @throws Exception
	 */
	
	@Bean
	@StepScope
	public HibernatePagingItemReader<Department> hibernateDepartmentItemReaderWithoutPartitioning() throws Exception {
	    
		HibernatePagingItemReader<Department> hibernateReader=new HibernatePagingItemReader<>();
		hibernateReader.setFetchSize(configuration.getChunkSize());
		hibernateReader.setQueryString("FROM Department");
		hibernateReader.setSessionFactory(sessionFactory().getObject());
		hibernateReader.setSaveState(false);
		hibernateReader.afterPropertiesSet();
		return hibernateReader;
	}
	
	/**
	 * Hibernate based reader with partitioning logic
	 * 
	 * @param fromId
	 * @param toId
	 * @param name
	 * @return
	 * @throws Exception
	 */
	
	@Bean
	@StepScope
	public HibernatePagingItemReader<Department> hibernateDepartmentItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
	    
		HibernatePagingItemReader<Department> hibernateReader=new HibernatePagingItemReader<>();
		hibernateReader.setFetchSize(configuration.getChunkSize());
		hibernateReader.setQueryString("FROM Department o where o.id>=" + fromId + " and o.id <= " + toId +" order by o.id ASC");
		hibernateReader.setSessionFactory(sessionFactory().getObject());
		hibernateReader.setSaveState(false);
		hibernateReader.afterPropertiesSet();
		return hibernateReader;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() throws IOException{
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
	    factoryBean.setDataSource(configuration.getDataSource());
	   // factoryBean.setAnnotatedPackages("com.qiwkreport.qiwk.etl.domain");
	    factoryBean.setPackagesToScan("com.qiwkreport.qiwk.etl.domain");
	    factoryBean.afterPropertiesSet();
		return factoryBean;
	}
	
	
	@Bean
	public JpaTransactionManager transactionManager() {
	    JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		return jpaTransactionManager;
	}
	
	
	/**
	 * Following is JDBC based readers with partitioning logic
	 * 
	 * @param fromId
	 * @param toId
	 * @param name
	 * @return
	 * @throws Exception
	 */
	
	@Bean
	@StepScope
	public JdbcPagingItemReader<Department> departmentReaderWithPartitioning(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JdbcPagingItemReader<Department> reader = new JdbcPagingItemReader<Department>();
		reader.setDataSource(configuration.getDataSource());
		// the fetch size should be equal to chunk size for the performance reasons.
		reader.setFetchSize(configuration.getChunkSize());

		reader.setRowMapper(new BeanPropertyRowMapper<>(Department.class));
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("DEPARTMENTID, DEPARTMENTNAME ,DEPARTMENTLOCATION, DEPARTMENTWORK");
		provider.setFromClause("from DEPARTMENT");
		provider.setWhereClause("where id>=" + fromId + " and id <=" + toId);

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		reader.setQueryProvider(provider);
		reader.afterPropertiesSet();
		return reader;
	}
	
	
	/**
	 * Following is JDBC based readers with no partitioning logic.
	 * 
	 * @param fromId
	 * @param toId
	 * @param name
	 * @return
	 * @throws Exception
	 */
	
	@Bean
	@StepScope
	public JdbcPagingItemReader<Department> readEmployeeWithoutPartitioning() throws Exception {

		JdbcPagingItemReader<Department> reader = new JdbcPagingItemReader<Department>();
		reader.setDataSource(configuration.getDataSource());
		// the fetch size equal to chunk size for the performance reasons. 
		reader.setFetchSize(configuration.getChunkSize());
		reader.setRowMapper(new BeanPropertyRowMapper<>(Department.class));
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("DEPARTMENTID, DEPARTMENTNAME ,DEPARTMENTLOCATION, DEPARTMENTWORK");
		provider.setFromClause("from Department");

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		reader.setQueryProvider(provider);
		reader.afterPropertiesSet();
		return reader;
	}
 
	@Bean
	public OraclePagingQueryProvider employeeQueryProvider() {
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause(
				"DEPARTMENTID, DEPARTMENTNAME ,DEPARTMENTLOCATION, DEPARTMENTWORK");
		provider.setFromClause("from Department");
		provider.setWhereClause("where id >= :fromId and id <= :toId");
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);
		return provider;
	}
}


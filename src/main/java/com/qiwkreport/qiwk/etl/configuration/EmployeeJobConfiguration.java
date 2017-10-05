package com.qiwkreport.qiwk.etl.configuration;


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
import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;
import com.qiwkreport.qiwk.etl.processor.EmployeeProcessor;
import com.qiwkreport.qiwk.etl.writer.JpaBasedItemWriter;

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
	
	
	@Bean
	public ItemWriter<NewEmployee> jpaEmployeeItemWriter() {
		return new JpaBasedItemWriter<NewEmployee>();
	}
	
	@StepScope
	@Bean
	public ItemWriter<NewEmployee> hibernateEmployeeItemWriter() throws IOException {
	        HibernateItemWriter<NewEmployee> itemWriter = new HibernateItemWriter<>();
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
	public JpaPagingItemReader<Employee> jpaEmployeeReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JpaPagingItemReader<Employee> reader = new JpaPagingItemReader<Employee>();
		reader.setPageSize(configuration.getChunkSize());
		reader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		reader.setQueryString("FROM Employee e where e.id>=" + fromId + " and e.id <= " + toId +" order by e.id ASC");
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
	public JpaPagingItemReader<Employee> jpaEmployeeReaderWithoutPartitioning() throws Exception {

		JpaPagingItemReader<Employee> reader = new JpaPagingItemReader<Employee>();
		reader.setPageSize(configuration.getChunkSize());
		reader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		reader.setQueryString("FROM Employee");
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
	public HibernatePagingItemReader<Employee> hibernateEmployeeItemReaderWithoutPartitioning() throws Exception {
	    
		HibernatePagingItemReader<Employee> hibernateReader=new HibernatePagingItemReader<>();
		hibernateReader.setFetchSize(configuration.getChunkSize());
		hibernateReader.setQueryString("FROM Employee");
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
	public HibernatePagingItemReader<Employee> hibernateEmployeeItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
	    
		HibernatePagingItemReader<Employee> hibernateReader=new HibernatePagingItemReader<>();
		hibernateReader.setFetchSize(configuration.getChunkSize());
		hibernateReader.setQueryString("FROM Employee o where o.id>=" + fromId + " and o.id <= " + toId +" order by o.id ASC");
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
	public JdbcPagingItemReader<Employee> employeeReaderWithPartitioning(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReader<Employee>();
		reader.setDataSource(configuration.getDataSource());
		// the fetch size should be equal to chunk size for the performance reasons.
		reader.setFetchSize(configuration.getChunkSize());
/*		reader.setRowMapper((resultSet, i) -> {
			return new Employee(resultSet.getLong("id"), 
					resultSet.getString("firstName"),
					resultSet.getString("lastName"),
					resultSet.getString("village"),
					resultSet.getString("street"),
					resultSet.getString("city"),
					resultSet.getString("district"),
					resultSet.getString("state"),
					resultSet.getString("pincode"),
					resultSet.getString("managerid"),
					resultSet.getString("managerName"),
					(Department) resultSet.getObject("departmentId"));
		});*/

		reader.setRowMapper(new BeanPropertyRowMapper<>(Employee.class));
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("id, firstName ,lastName, village, street , city, district, state, pincode, managerid, managerName");
		provider.setFromClause("from EMPLOYEE");
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
	public JdbcPagingItemReader<Employee> readEmployeeWithoutPartitioning() throws Exception {

		JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReader<Employee>();
		reader.setDataSource(configuration.getDataSource());
		// the fetch size equal to chunk size for the performance reasons. 
		reader.setFetchSize(configuration.getChunkSize());
/*		reader.setRowMapper((resultSet, i) -> {
			return new Employee(resultSet.getLong("id"), 
					resultSet.getString("firstName"),
					resultSet.getString("lastName"),
					resultSet.getString("village"),
					resultSet.getString("street"),
					resultSet.getString("city"),
					resultSet.getString("district"),
					resultSet.getString("state"),
					resultSet.getString("pincode"),
					resultSet.getString("managerid"),
					resultSet.getString("managerName"),
					(Department) resultSet.getObject("departmentId"));
		});*/
		reader.setRowMapper(new BeanPropertyRowMapper<>(Employee.class));
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("id, firstName ,lastName, village, street , city, district, state, pincode, managerid, managerName");
		provider.setFromClause("from Employee");

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
				"id, firstName ,lastName, village, street , city, district, state, pincode, managerid, managerName");
		provider.setFromClause("from Employee");
		provider.setWhereClause("where id >= :fromId and id <= :toId");
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);
		return provider;
	}
}


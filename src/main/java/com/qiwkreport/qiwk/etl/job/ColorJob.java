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

import com.lcs.wc.color.LCSColor;
import com.qiwkreport.qiwk.etl.common.BatchJobConfiguration;
import com.qiwkreport.qiwk.etl.common.ColumnRangePartitioner;
import com.qiwkreport.qiwk.etl.common.QiwkJobsConfiguration;
import com.qiwkreport.qiwk.etl.domain.Department;
import com.qiwkreport.qiwk.etl.domain.NewDepartment;
import com.qiwkreport.qiwk.etl.domain.QiwkColor;
import com.qiwkreport.qiwk.etl.processor.ColorProcessor;
import com.qiwkreport.qiwk.etl.processor.DepartmentProcessor;
import com.qiwkreport.qiwk.etl.writer.JpaDepartmentItemWriter;

/**
 * This is configurations class for Color table, this class is responsible for moving records from
 * Flex Color table to Qiwk Color table
 * 
 * @author Abhilash
 *
 */

@Configuration
@EnableBatchProcessing
@Import(BatchJobConfiguration.class)
public class ColorJob{
	
	@Autowired
	private QiwkJobsConfiguration configuration;
	
	
	@Bean
	public Job colorJob() throws Exception {
		return configuration.getJobBuilderFactory()
				.get("ColorJob")
				.incrementer(new RunIdIncrementer())
				.start(colorMasterStep())
				.build();
	}

	@Bean
	public Step colorMasterStep() throws Exception {
		return configuration.getStepBuilderFactory()
				.get("colorMasterStep")
				.partitioner(colorSlaveStep().getName(), columnRangePartitioner())
				.partitionHandler(colorMasterSlaveHandler())
				.build();
	}
	
	@Bean
	public Step colorSlaveStep() throws Exception {
		return configuration.getStepBuilderFactory()
				.get("colorSlaveStep")
				.<LCSColor, QiwkColor>chunk(configuration.getChunkSize())
				.reader(jpaColorReader(null, null, null))
				.processor(colorProcessor())
				.writer(jpaColorWriter())
				.build();
	}
	
	@Bean
	public PartitionHandler colorMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(configuration.getGridSize());
		handler.setTaskExecutor(taskExecutor());
		handler.setStep(colorSlaveStep());
		handler.afterPropertiesSet();
		
		return handler;
	}

	@Bean
	public ColumnRangePartitioner columnRangePartitioner() {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("IDA2A2");
		partitioner.setDataSource(configuration.getDataSource());
		partitioner.setTable("LCSCOLOR");
		return partitioner;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return configuration.getTaskExecutorConfiguration()
				.taskExecutor();
	}

	@Bean
	public ColorProcessor colorProcessor() {
		return new ColorProcessor();
	}
	
	
	@Bean
	public ItemWriter<QiwkColor> jpaColorWriter() {
		return new JpaDepartmentItemWriter<QiwkColor>();
	}
	
	@StepScope
	@Bean
	public ItemWriter<QiwkColor> hibernateColorItemWriter() throws IOException {
	        HibernateItemWriter<QiwkColor> itemWriter = new HibernateItemWriter<>();
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
	public JpaPagingItemReader<LCSColor> jpaColorReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JpaPagingItemReader<LCSColor> reader = new JpaPagingItemReader<LCSColor>();
		reader.setPageSize(configuration.getChunkSize());
		reader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		reader.setQueryString(
				"FROM LCSColor o where o.IDA2A2>=" + fromId + " and o.IDA2A2 <= " + toId + " order by o.id ASC");
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
	public JpaPagingItemReader<LCSColor> jpaColorReaderWithoutPartitioning() throws Exception {

		JpaPagingItemReader<LCSColor> reader = new JpaPagingItemReader<LCSColor>();
		reader.setPageSize(configuration.getChunkSize());
		reader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		reader.setQueryString("FROM LCSColor");
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
	public HibernatePagingItemReader<LCSColor> hibernateColorItemReaderWithoutPartitioning() throws Exception {
	    
		HibernatePagingItemReader<LCSColor> hibernateReader=new HibernatePagingItemReader<>();
		hibernateReader.setFetchSize(configuration.getChunkSize());
		hibernateReader.setQueryString("FROM LCSColor");
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
	public HibernatePagingItemReader<LCSColor> hibernateColorItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
	    
		HibernatePagingItemReader<LCSColor> hibernateReader=new HibernatePagingItemReader<>();
		hibernateReader.setFetchSize(configuration.getChunkSize());
		hibernateReader.setQueryString(
				"FROM LCSColor o where o.IDA2A2>=" + fromId + " and o.IDA2A2 <= " + toId + " order by o.id ASC");
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
	    factoryBean.setPackagesToScan("com.lcs.wc.color");
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
	public JdbcPagingItemReader<LCSColor> colorReaderWithPartitioning(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JdbcPagingItemReader<LCSColor> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(configuration.getDataSource());
		// the fetch size should be equal to chunk size for the performance reasons.
		reader.setFetchSize(configuration.getChunkSize());

		reader.setRowMapper(new BeanPropertyRowMapper<>(LCSColor.class));
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("DEPARTMENTID, DEPARTMENTNAME ,DEPARTMENTLOCATION, DEPARTMENTWORK");
		provider.setFromClause("from LCSColor");
		provider.setWhereClause("where IDA2A2>=" + fromId + " and IDA2A2 <=" + toId);

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("IDA2A2", Order.ASCENDING);
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
	public JdbcPagingItemReader<LCSColor> readColorWithoutPartitioning() throws Exception {

		JdbcPagingItemReader<LCSColor> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(configuration.getDataSource());
		// the fetch size equal to chunk size for the performance reasons. 
		reader.setFetchSize(configuration.getChunkSize());
		reader.setRowMapper(new BeanPropertyRowMapper<>(LCSColor.class));
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("DEPARTMENTID, DEPARTMENTNAME ,DEPARTMENTLOCATION, DEPARTMENTWORK");
		provider.setFromClause("from LCSColor");

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("IDA2A2", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		reader.setQueryProvider(provider);
		reader.afterPropertiesSet();
		return reader;
	}
}


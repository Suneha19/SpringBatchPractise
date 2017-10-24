package com.qiwkreport.qiwk.etl.job;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.lcs.wc.color.LCSColor;
import com.qiwkreport.qiwk.etl.common.BatchJobConfiguration;
import com.qiwkreport.qiwk.etl.common.ColumnRangePartitioner;
import com.qiwkreport.qiwk.etl.common.FlexDBConfiguration;
import com.qiwkreport.qiwk.etl.common.QiwkJobsConfiguration;
import com.qiwkreport.qiwk.etl.domain.QiwkColor_old;
import com.qiwkreport.qiwk.etl.processor.ColorProcessor;
import com.qiwkreport.qiwk.etl.writer.JpaBasedItemWriter;

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
public class ColorJobCongiguration{
	
	@Autowired
	private QiwkJobsConfiguration configuration;
	
	@Autowired
	private FlexDBConfiguration flexDBConfiguration;
	
	 @PersistenceContext(unitName = "flexDB")
	 private EntityManager entityManager;
	
	
	//@Bean
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
				.<com.qiwkreport.qiwk.etl.flex.domain.LCSColor, QiwkColor_old>chunk(configuration.getChunkSize())
				.reader(colorReaderWithPartitioning(null, null, null))
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
	public ColumnRangePartitioner columnRangePartitioner() throws SQLException {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("IDA2A2");
		partitioner.setDataSource(flexDBConfiguration.flexDataSource());
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
	public ItemWriter<QiwkColor_old> jpaColorWriter() {
		return new JpaBasedItemWriter<QiwkColor_old>();
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
	public JdbcPagingItemReader<com.qiwkreport.qiwk.etl.flex.domain.LCSColor> colorReaderWithPartitioning(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JdbcPagingItemReader<com.qiwkreport.qiwk.etl.flex.domain.LCSColor> reader = new JdbcPagingItemReader<>();
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		StringBuilder queryBuilder = new StringBuilder("select  ");
		String attrib;
		String columnName = null;
		List<String> colorColumns = getColorColumnNames();
		List<String> fields=new ArrayList<>();
		Iterator<String> columnItr = colorColumns.iterator();
		
		// the fetch size should be equal to chunk size for the performance reasons.
		reader.setFetchSize(configuration.getChunkSize());
		reader.setDataSource(flexDBConfiguration.flexDataSource());

		while (columnItr.hasNext()) {

			columnName = columnItr.next();
			attrib = columnName;
			attrib.toUpperCase();

			if (columnName.startsWith("IDA2A2")) {
				//fields.add("BRANCHID");
				queryBuilder = queryBuilder.append("LCSColor." + columnName + " BRANCHID ");
			}
			if (attrib.startsWith("COLOR") || attrib.startsWith("SECURITY") || attrib.startsWith("CREATESTAMPA2")
					|| attrib.startsWith("UPDATE") || attrib.startsWith("PTC_STR_") || attrib.startsWith("PTC_DBL_")
					|| attrib.startsWith("PTC_LNG_") || attrib.startsWith("PTC_BLN_") || attrib.startsWith("PTC_TMS_")
					|| attrib.startsWith("IDA2TYPEDEFINITIONREFERENCE") || attrib.startsWith("FLEX")
					|| attrib.startsWith("THUMBNAIL")) {
				//fields.add(attrib);
				queryBuilder = queryBuilder.append(" LCSColor." + columnName);
			}
			if (attrib.contains("TYPEINFO") && (attrib.startsWith("BRANCHIDA3") || attrib.startsWith("IDA3"))) {
				//fields.add(attrib);
				queryBuilder = queryBuilder.append(" LCSColor." + columnName);
			}
			if (attrib.startsWith("MAR")) {
				//fields.add("MARKEDFORDELETEA2");
				queryBuilder = queryBuilder.append(" LCSColor." + columnName + " MARKEDFORDELETEA2 ");
			}
			if (attrib.startsWith("BRANCHIDA2TYPEDEFINITIONREFE")) {
			//	fields.add("FLEXTYPEID");
				queryBuilder = queryBuilder.append(" LCSColor." + columnName + " FLEXTYPEID ");
			}

		}
		
		provider.setSelectClause(queryBuilder.toString());
		provider.setFromClause("from LCSCOLOR o");
		provider.setWhereClause("where o.IDA2A2>=" + fromId + " and o.IDA2A2 <=" + toId);
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("IDA2A2", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		reader.setRowMapper(getLCSColorObject());

		reader.setQueryProvider(provider);
		reader.afterPropertiesSet();

		return reader;
	}

	private RowMapper<com.qiwkreport.qiwk.etl.flex.domain.LCSColor> getLCSColorObject() {
		return new RowMapper<com.qiwkreport.qiwk.etl.flex.domain.LCSColor>() {

			@Override
			public com.qiwkreport.qiwk.etl.flex.domain.LCSColor mapRow(ResultSet rs, int arg1) throws SQLException {
				com.qiwkreport.qiwk.etl.flex.domain.LCSColor lcsColor=new com.qiwkreport.qiwk.etl.flex.domain.LCSColor();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				lcsColor.setIDA2A2(Integer.parseInt(rs.getString("IDA2A2")));
				lcsColor.setCOLORHEXIDECIMALVALUE(rs.getString("COLORHEXIDECIMALVALUE"));
				lcsColor.setCOLORNAME(rs.getString("COLORNAME"));
				lcsColor.setSECURITYLABELS(rs.getString("SECURITYLABELS"));
				
				try {
					lcsColor.setCREATESTAMPA2(dateFormat.parse(rs.getString("CREATESTAMPA2")));
					lcsColor.setUPDATESTAMPA2(dateFormat.parse(rs.getString("UPDATESTAMPA2")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				lcsColor.setUPDATECOUNTA2(Integer.parseInt(rs.getString("UPDATECOUNTA2")));
				lcsColor.setPTC_STR_1TYPEINFOLCSCOLOR(rs.getString("PTC_STR_1TYPEINFOLCSCOLOR"));
				lcsColor.setIDA2TYPEDEFINITIONREFERENCE(Integer.parseInt(rs.getString("IDA2TYPEDEFINITIONREFERENCE")));
				lcsColor.setFLEXTYPEIDPATH(rs.getString("FLEXTYPEIDPATH"));
				lcsColor.setTHUMBNAIL(rs.getString("THUMBNAIL"));
				lcsColor.setIDA3A2FOLDERINGINFO(Integer.parseInt(rs.getString("IDA3A2FOLDERINGINFO")));
				lcsColor.setIDA3A2OWNERSHIP(Integer.parseInt(rs.getString("IDA3A2OWNERSHIP")));
				lcsColor.setIDA3A2STATE(Integer.parseInt(rs.getString("IDA3A2STATE")));
				lcsColor.setIDA3A7(Integer.parseInt(rs.getString("IDA3A7")));
				lcsColor.setIDA3B2FOLDERINGINFO(Integer.parseInt(rs.getString("IDA3B2FOLDERINGINFO")));
				lcsColor.setIDA3B8(Integer.parseInt(rs.getString("IDA3B8")));
				lcsColor.setIDA3C8(Integer.parseInt(rs.getString("IDA3C8")));
				lcsColor.setIDA3CONTAINERREFERENCE(Integer.parseInt(rs.getString("IDA3CONTAINERREFERENCE")));
				lcsColor.setIDA3DOMAINREF(Integer.parseInt(rs.getString("IDA3DOMAINREF")));
				lcsColor.setIDA3TEAMID(Integer.parseInt(rs.getString("IDA3TEAMID")));
				lcsColor.setIDA3TEAMTEMPLATEID(Integer.parseInt(rs.getString("IDA3TEAMTEMPLATEID")));
				lcsColor.setMARKFORDELETEA2(Integer.parseInt(rs.getString("MARKFORDELETEA2")));
				lcsColor.setBRANCHIDA2TYPEDEFINITIONREFE(Integer.parseInt(rs.getString("BRANCHIDA2TYPEDEFINITIONREFE")));
				
				return lcsColor;
			}
		};
	}
	
	private List<String> getColorColumnNames() throws Exception {
		
		JdbcPagingItemReader<String> reader=new JdbcPagingItemReader<>();
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		List<String> columnNames=new ArrayList<>();

		provider.setSelectClause("COLUMN_NAME ");
		provider.setFromClause("from LCSColor ");
		provider.setWhereClause("where TABLE_NAME='LCSCOLOR'");
		reader.setQueryProvider(provider);	
		reader.afterPropertiesSet();
		reader.setRowMapper(new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					columnNames.add(rs.getMetaData().getColumnName(i));
				}
				return null;

			}
		});
		return columnNames;
	}
	
	@StepScope
	@Bean
	public ItemWriter<QiwkColor_old> hibernateColorItemWriter() throws IOException {
	        HibernateItemWriter<QiwkColor_old> itemWriter = new HibernateItemWriter<>();
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
	public JpaPagingItemReader<LCSColor> jpaColorReader(@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JpaPagingItemReader<LCSColor> reader = new JpaPagingItemReader<LCSColor>();
	

		reader.setPageSize(configuration.getChunkSize());
		reader.setEntityManagerFactory(entityManager.getEntityManagerFactory());
		
		List<String> columns = new ArrayList<>();
		StringBuilder queryBuilder = new StringBuilder("select  ");
		String attrib;
		String columnName = null;
		
		EntityManagerFactory entityManagerFactory =flexDBConfiguration.getEntityManagerFactory().getObject();
		
		 reader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		Query createNativeQuery = entityManager
				.createQuery("select COLUMN_NAME from user_tab_cols where TABLE_NAME='LCSCOLOR'");
		
		List<String> resultList = createNativeQuery.getResultList();
		Iterator<String> itr = resultList.iterator();
		while (itr.hasNext()) {

			columnName = itr.next();
			attrib = columnName;
			attrib.toUpperCase();

			if (columnName.startsWith("IDA2A2")) {
				columns.add("BRANCHID");
				queryBuilder = queryBuilder.append("LCSColor." + columnName + " BRANCHID");

			}
			if (attrib.startsWith("COLOR") || attrib.startsWith("SECURITY") || attrib.startsWith("CREATESTAMPA2")
					|| attrib.startsWith("UPDATE") || attrib.startsWith("PTC_STR_") || attrib.startsWith("PTC_DBL_")
					|| attrib.startsWith("PTC_LNG_") || attrib.startsWith("PTC_BLN_") || attrib.startsWith("PTC_TMS_")
					|| attrib.startsWith("IDA2TYPEDEFINITIONREFERENCE") || attrib.startsWith("FLEX")
					|| attrib.startsWith("THUMBNAIL")) {
				columns.add(attrib);
				queryBuilder = queryBuilder.append("LCSColor." + columnName);
			}
			if (attrib.contains("TYPEINFO") && (attrib.startsWith("BRANCHIDA3") || attrib.startsWith("IDA3"))) {

				columns.add(attrib);
				queryBuilder = queryBuilder.append("LCSColor." + columnName);
			}
			if (attrib.startsWith("MAR")) {
				columns.add("MARKEDFORDELETEA2");
				queryBuilder = queryBuilder.append("LCSColor." + columnName + " MARKEDFORDELETEA2");
			}
			if (attrib.startsWith("BRANCHIDA2TYPEDEFINITIONREFE")) {
				columns.add("FLEXTYPEID");
				queryBuilder = queryBuilder.append("LCSColor." + columnName + " FLEXTYPEID");
			}

		}

		reader.setQueryString(" FROM LCSColor o where o.IDA2A2>=" + fromId
				+ " and o.IDA2A2 <= " + toId + " order by o.id ASC");
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


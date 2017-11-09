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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.lcs.wc.color.LCSColor;
import com.qiwkreport.qiwk.etl.common.BatchJobConfiguration;
import com.qiwkreport.qiwk.etl.common.ColumnRangePartitioner;
import com.qiwkreport.qiwk.etl.common.FlexDBConfiguration;
import com.qiwkreport.qiwk.etl.common.QiwkJobsConfiguration;
import com.qiwkreport.qiwk.etl.domain.QiwkColor;
import com.qiwkreport.qiwk.etl.domain.QiwkMaterial;
import com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial;
import com.qiwkreport.qiwk.etl.processor.ColorProcessor;
import com.qiwkreport.qiwk.etl.processor.MaterialProcessor;
import com.qiwkreport.qiwk.etl.writer.JpaBasedItemWriter;

/**
 * This is configurations class for Material table, this class is responsible for moving records from
 * Flex Color table to Qiwk Material table
 * 
 * @author Abhilash
 *
 */

@Configuration
@EnableBatchProcessing
@Import(BatchJobConfiguration.class)
public class MaterialJobCongiguration{
	
	@Autowired
	private QiwkJobsConfiguration configuration;
	
	@Autowired
	private FlexDBConfiguration flexDBConfiguration;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MaterialJobCongiguration.class);
	
	
	@Bean
	public Job materialJob() throws Exception {
		return configuration.getJobBuilderFactory()
				.get("MaterialJob")
				.incrementer(new RunIdIncrementer())
				.start(materialSlaveStep())
				.build();
	}

	@Bean
	public Step materialMasterStep() throws Exception {
		return configuration.getStepBuilderFactory()
				.get("materialMasterStep")
				.partitioner(materialSlaveStep().getName(), columnRangePartitioner())
				.partitionHandler(materialMasterSlaveHandler())
				.build();
	}
	
	@Bean
	public Step materialSlaveStep() throws Exception {
		return configuration.getStepBuilderFactory()
				.get("materialSlaveStep")
				.<com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial, QiwkMaterial>chunk(configuration.getChunkSize())
				.reader(readMaterialWithoutPartitioning())
				.processor(materialProcessor())
				.writer(jpaMaterialWriter())
				.build();
	}
	
	@Bean
	public PartitionHandler materialMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(configuration.getGridSize());
		handler.setTaskExecutor(taskExecutor());
		handler.setStep(materialSlaveStep());
		handler.afterPropertiesSet();
		
		return handler;
	}

	@Bean
	public ColumnRangePartitioner columnRangePartitioner() throws SQLException {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("IDA2A2");
		partitioner.setDataSource(flexDBConfiguration.flexDataSource());
		partitioner.setTable("LCSMATERIAL");
		return partitioner;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return configuration.getTaskExecutorConfiguration()
				.taskExecutor();
	}

	@Bean
	public MaterialProcessor materialProcessor() {
		return new MaterialProcessor();
	}
	
	
	@Bean
	public ItemWriter<QiwkMaterial> jpaMaterialWriter() {
		return new JpaBasedItemWriter<QiwkMaterial>();
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
	public JdbcPagingItemReader<com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial> readMaterialWithoutPartitioning() throws Exception {

		
		JdbcPagingItemReader<com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial> reader = new JdbcPagingItemReader<>();
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		StringBuilder queryBuilder = new StringBuilder("*  ");
		//StringBuilder queryBuilder = new StringBuilder(" ");
		String attrib;
		String columnName = null;
		List<String> materialColumns = getMaterialColumnNames();
		List<String> fields=new ArrayList<>();
		Iterator<String> columnItr = materialColumns.iterator();
		
		// the fetch size should be equal to chunk size for the performance reasons.
		reader.setFetchSize(configuration.getChunkSize());
		reader.setDataSource(flexDBConfiguration.flexDataSource());

		while (columnItr.hasNext()) {

			columnName = columnItr.next();
			attrib = columnName;
			attrib.toUpperCase();

			if (columnName.startsWith("IDA2A2")) {
				//fields.add("BRANCHID");
				queryBuilder = queryBuilder.append("LCSMaterial." + columnName + " BRANCHID ");
			}
			if (attrib.startsWith("COLOR") || attrib.startsWith("SECURITY") || attrib.startsWith("CREATESTAMPA2")
					|| attrib.startsWith("UPDATE") || attrib.startsWith("PTC_STR_") || attrib.startsWith("PTC_DBL_")
					|| attrib.startsWith("PTC_LNG_") || attrib.startsWith("PTC_BLN_") || attrib.startsWith("PTC_TMS_")
					|| attrib.startsWith("IDA2TYPEDEFINITIONREFERENCE") || attrib.startsWith("FLEX")
					|| attrib.startsWith("THUMBNAIL")) {
				//fields.add(attrib);
				queryBuilder = queryBuilder.append(" LCSMaterial." + columnName);
			}
			if (attrib.contains("TYPEINFO") && (attrib.startsWith("BRANCHIDA3") || attrib.startsWith("IDA3"))) {
				//fields.add(attrib);
				queryBuilder = queryBuilder.append(" LCSMaterial." + columnName);
			}
			if (attrib.startsWith("MAR")) {
				//fields.add("MARKEDFORDELETEA2");
				queryBuilder = queryBuilder.append(" LCSMaterial." + columnName + " MARKEDFORDELETEA2 ");
			}
			if (attrib.startsWith("BRANCHIDA2TYPEDEFINITIONREFE")) {
			//	fields.add("FLEXTYPEID");
				queryBuilder = queryBuilder.append(" LCSMaterial." + columnName + " FLEXTYPEID ");
			}

		}
		
		provider.setSelectClause(queryBuilder.toString());
		provider.setFromClause("from LCSMaterial o");
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("IDA2A2", Order.ASCENDING);
		provider.setSortKeys(sortKeys);
		reader.setRowMapper(getLCSMaterialObject());
		reader.setQueryProvider(provider);
		reader.afterPropertiesSet();

		return reader;
	}
	


	private RowMapper<com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial> getLCSMaterialObject() {
		return new RowMapper<com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial>() {

			@Override
			public com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial mapRow(ResultSet rs, int arg1) throws SQLException {
				com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial lcsMaterial=new com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
				lcsMaterial.setIDA2A2(Integer.parseInt(rs.getString("IDA2A2")));
				lcsMaterial.setCOLORHEXIDECIMALVALUE(rs.getString("COLORHEXIDECIMALVALUE"));
				lcsMaterial.setCOLORNAME(rs.getString("COLORNAME"));
				lcsMaterial.setSECURITYLABELS(rs.getString("SECURITYLABELS"));
				
				try {
					lcsMaterial.setCREATESTAMPA2(dateFormat.parse(rs.getString("CREATESTAMPA2")));
					lcsMaterial.setUPDATESTAMPA2(dateFormat.parse(rs.getString("UPDATESTAMPA2")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				lcsMaterial.setUPDATECOUNTA2(Integer.parseInt(rs.getString("UPDATECOUNTA2")));
				lcsMaterial.setPTC_STR_1TYPEINFOLCSCOLOR(rs.getString("PTC_STR_1TYPEINFOLCSCOLOR"));
				lcsMaterial.setIDA2TYPEDEFINITIONREFERENCE(Integer.parseInt(rs.getString("IDA2TYPEDEFINITIONREFERENCE")));
				lcsMaterial.setFLEXTYPEIDPATH(rs.getString("FLEXTYPEIDPATH"));
				lcsMaterial.setTHUMBNAIL(rs.getString("THUMBNAIL"));
				lcsMaterial.setIDA3A2FOLDERINGINFO(Integer.parseInt(rs.getString("IDA3A2FOLDERINGINFO")));
				lcsMaterial.setIDA3A2OWNERSHIP(Integer.parseInt(rs.getString("IDA3A2OWNERSHIP")));
				lcsMaterial.setIDA3A2STATE(Integer.parseInt(rs.getString("IDA3A2STATE")));
				lcsMaterial.setIDA3A7(Integer.parseInt(rs.getString("IDA3A7")));
				lcsMaterial.setIDA3B2FOLDERINGINFO(Integer.parseInt(rs.getString("IDA3B2FOLDERINGINFO")));
				lcsMaterial.setIDA3B8(Integer.parseInt(rs.getString("IDA3B8")));
				lcsMaterial.setIDA3C8(Integer.parseInt(rs.getString("IDA3C8")));
				lcsMaterial.setIDA3CONTAINERREFERENCE(Integer.parseInt(rs.getString("IDA3CONTAINERREFERENCE")));
				lcsMaterial.setIDA3DOMAINREF(Integer.parseInt(rs.getString("IDA3DOMAINREF")));
				lcsMaterial.setIDA3TEAMID(Integer.parseInt(rs.getString("IDA3TEAMID")));
			//	lcsColor.setIDA3TEAMTEMPLATEID(Integer.parseInt(rs.getString("IDA3TEAMTEMPLATEID")));
				lcsMaterial.setMARKFORDELETEA2(Integer.parseInt(rs.getString("MARKFORDELETEA2")));
				lcsMaterial.setBRANCHIDA2TYPEDEFINITIONREFE(Integer.parseInt(rs.getString("BRANCHIDA2TYPEDEFINITIONREFE")));
				
				return lcsMaterial;
			}
		};
	}
	
	private List<String> getMaterialColumnNames() throws Exception {
		
		JdbcPagingItemReader<String> reader=new JdbcPagingItemReader<>();
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		List<String> columnNames=new ArrayList<>();

		provider.setSelectClause("COLUMN_NAME ");
		provider.setFromClause("from LCSMaterial ");
		provider.setWhereClause("where TABLE_NAME='LCSMATERIAL'");
		reader.setQueryProvider(provider);	
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
	
}


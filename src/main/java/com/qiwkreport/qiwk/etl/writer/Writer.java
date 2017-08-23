/*package com.qiwkreport.qiwk.etl.writer;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.qiwkreport.qiwk.etl.domain.NewEmployee;

@Component
public class Writer {

	@Autowired
	public DataSource dataSource;

	@StepScope
	@Bean
	public ItemWriter<NewEmployee> customItemWriter() {
		JdbcBatchItemWriter<NewEmployee> writer = new JdbcBatchItemWriter<NewEmployee>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT INTO NEWEMPLOYEE values (:id, :firstName ,:lastName)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.afterPropertiesSet();
		return writer;
	}

	@StepScope
	@Bean
	public ItemWriter<NewEmployee> slaveWriter() {
		JdbcBatchItemWriter<NewEmployee> writer = new JdbcBatchItemWriter<NewEmployee>();
		writer.setDataSource(dataSource);
		writer.setSql(
				"INSERT INTO NEWEMPLOYEE values (:id, :firstName ,:lastName, :village, :street , :city, :district, :state, :pincode, :managerid, :managerName)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.afterPropertiesSet();
		return writer;
	}
}
*/
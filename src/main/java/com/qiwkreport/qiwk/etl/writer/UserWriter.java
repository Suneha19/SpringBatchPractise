package com.qiwkreport.qiwk.etl.writer;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qiwkreport.qiwk.etl.domain.NewUser;

@Configuration
public class UserWriter{
	
	@Autowired
	private DataSource dataSource;

	@StepScope
	@Bean
	public ItemWriter<NewUser> userItemWriter() {
		JdbcBatchItemWriter<NewUser> writer = new JdbcBatchItemWriter<NewUser>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT INTO NEWUSER values (:id, :username ,:password, :age)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.afterPropertiesSet();
		return writer;
	}
}

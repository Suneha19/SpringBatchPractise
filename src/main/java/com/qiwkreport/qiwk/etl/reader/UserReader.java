package com.qiwkreport.qiwk.etl.reader;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qiwkreport.qiwk.etl.domain.OldUser;

@Configuration
public class UserReader {

	@Autowired
	private DataSource dataSource;

	@Value("${data.chunk.size}")
	private int chunkSize;

	@Value("${partition.grid.size}")
	private int gridSize;

	@Bean
	@StepScope
	public JdbcPagingItemReader<OldUser> userItemReader(@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
		
		System.out.println("FromId:-->" + fromId);
		System.out.println("toId:-->" + toId);
		System.out.println("name:-->" + name);
		
		JdbcPagingItemReader<OldUser> reader = new JdbcPagingItemReader<OldUser>();
		reader.setDataSource(this.dataSource);
		// this should be equal to chunk size for the performance reasons.
		reader.setFetchSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
			return new OldUser(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"),
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

}

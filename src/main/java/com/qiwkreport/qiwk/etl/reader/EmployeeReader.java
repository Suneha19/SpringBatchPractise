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
import org.springframework.stereotype.Component;

import com.qiwkreport.qiwk.etl.domain.Employee;

@Component
public class EmployeeReader{
	
	@Autowired
	public DataSource dataSource;

	@Value("${data.chunk.size}")
	private int chunkSize;

	/**
	 * {@code} The @StepScope annotation is very imp, as this instantiate this
	 * bean in spring context only when this is loaded
	 */
	@Bean
	@StepScope
	public JdbcPagingItemReader<Employee> pagingItemReader(@Value("#{stepExecutionContext['minValue']}") Long minvalue,
			@Value("#{stepExecutionContext['maxValue']}") Long maxvalue) {

		JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReader<Employee>();
		reader.setDataSource(this.dataSource);
		// this should be equal to chunk size for the performance reasons.
		reader.setFetchSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
			return new Employee(resultSet.getLong("id"), resultSet.getString("firstName"),
					resultSet.getString("firstName"));
		});

		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("id, firstName, lastName");
		provider.setFromClause("from Employee");
		provider.setWhereClause("where id>=" + minvalue + " and id < " + maxvalue);

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		reader.setQueryProvider(provider);

		return reader;
	}


}

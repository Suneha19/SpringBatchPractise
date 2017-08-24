package com.qiwkreport.qiwk.etl.reader;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.OldUser;

@Configuration
public class Reader{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);

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
	public JdbcPagingItemReader<OldUser> userItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
		
		JdbcPagingItemReader<OldUser> reader = new JdbcPagingItemReader<OldUser>();
		reader.setDataSource(this.dataSource);
		// this should be equal to chunk size for the performance reasons.
		reader.setFetchSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
			return new OldUser(
					resultSet.getInt("id"), 
					resultSet.getString("username"), 
					resultSet.getString("password"),
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


	/**
	 * {@code} The @StepScope annotation is very imp, as this instantiate this
	 * bean in spring context only when this is loaded
	 */
	
	@StepScope
	public JdbcPagingItemReader<Employee> employeeReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {

		JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReader<Employee>();
		reader.setDataSource(this.dataSource);
		// the fetch size should be equal to chunk size for the performance reasons.
		reader.setFetchSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
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
					resultSet.getString("managerName"));
		});

		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("id, firstName ,lastName, village, street , city, district, state, pincode, managerid, managerName");
		provider.setFromClause("from Employee");
		provider.setWhereClause("where id<=" + fromId + " and id > " + toId);

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		reader.setQueryProvider(provider);
		reader.afterPropertiesSet();
		return reader;
	}
	
	@Bean
	@StepScope
	public JdbcPagingItemReader<Employee> readEmployeeWithoutPartitioning() throws Exception {

		JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReader<Employee>();
		reader.setDataSource(this.dataSource);
		// the fetch size equal to chunk size for the performance reasons. 
		reader.setFetchSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
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
					resultSet.getString("managerName"));
		});

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
	public PagingQueryProvider employeeQueryProvider() {
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("id, firstName ,lastName, village, street , city, district, state, pincode, managerid, managerName");
		provider.setFromClause("from Employee");
		provider.setWhereClause("where id >= :fromId and id <= :toId");
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);
		return provider;
	}
	
}

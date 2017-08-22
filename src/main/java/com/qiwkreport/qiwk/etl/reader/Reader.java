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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.qiwkreport.qiwk.etl.configuration.FRJobConfiguration;
import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.QiwkBOMLink;

@Component
public class Reader{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FRJobConfiguration.class);

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
		/*	return new Employee(resultSet.getLong("id"), resultSet.getString("firstName"),
					resultSet.getString("firstName"));*/
			return null;
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

	@Bean
	@StepScope
	public JdbcPagingItemReader<Employee> slaveReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) {

		JdbcPagingItemReader<Employee> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		
		reader.setQueryProvider(queryProvider());
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("fromId", fromId);
		parameterValues.put("toId", toId);
		reader.setParameterValues(parameterValues);
		reader.setPageSize(chunkSize);
	/*	reader.setRowMapper((resultSet, i) -> {
			return new Employee(resultSet.getLong("id"), 
					resultSet.getString("firstName"),
					resultSet.getString("lastName"));
		});*/
		reader.setRowMapper(new BeanPropertyRowMapper<Employee>(Employee.class)) ;
		return reader;
	}
 
	@Bean
	public PagingQueryProvider queryProvider() {
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("*");
		provider.setFromClause("from Employee");
		provider.setWhereClause("where id >= :fromId and id <= :toId");
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		provider.setSortKeys(sortKeys);
		return provider;
	}
	
	@Bean
	@StepScope
	public JdbcPagingItemReader<QiwkBOMLink> bomLinkReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) {

		JdbcPagingItemReader<QiwkBOMLink> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		
		reader.setQueryProvider(queryProvider());
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("fromId", fromId);
		parameterValues.put("toId", toId);
		reader.setParameterValues(parameterValues);
		reader.setPageSize(chunkSize);
		reader.setRowMapper(new BeanPropertyRowMapper<QiwkBOMLink>() {{
		      setMappedClass(QiwkBOMLink.class);
		    }});
		LOGGER.info("slaveReader end " + fromId + " " + toId);
		return reader;
	}

	@Bean
	public PagingQueryProvider bomLinkQueryProvider() {
		OraclePagingQueryProvider provider = new OraclePagingQueryProvider();
		provider.setSelectClause("*");
		provider.setFromClause("from QiwkBOMLink");
		provider.setWhereClause("where primaryKey >= :fromId and primaryKey <= :toId");
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("primaryKey", Order.ASCENDING);
		provider.setSortKeys(sortKeys);
		return provider;
	}
}

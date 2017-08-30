package com.qiwkreport.qiwk.etl.reader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.qiwkreport.qiwk.etl.domain.Olduser;

@Configuration
public class Reader{
	
	@Autowired
	public DataSource dataSource;
	
	@PersistenceContext
    private EntityManager em;

	@Value("${data.chunk.size}")
	private int chunkSize;
	
	
	/**
	 * {@code} The @StepScope annotation is very imp, as this instantiate this
	 * bean in spring context only when this is loaded
	 */
	
	@Bean
	@StepScope
	public JdbcPagingItemReader<Olduser> userItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
		
		JdbcPagingItemReader<Olduser> reader = new JdbcPagingItemReader<Olduser>();
		//HibernatePagingItemReader<T>
		
		reader.setDataSource(this.dataSource);
		// this should be equal to chunk size for the performance reasons.
		reader.setFetchSize(chunkSize);
		reader.setRowMapper((resultSet, i) -> {
			return new Olduser(
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

	@Bean
	@StepScope
	public HibernatePagingItemReader<Olduser> hibernateUserItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
	    
		HibernatePagingItemReader<Olduser> hibernateReader=new HibernatePagingItemReader<>();
		hibernateReader.setFetchSize(chunkSize);
		hibernateReader.setQueryString("FROM Olduser o where o.id>=" + fromId + " and o.id <= " + toId +" order by o.id ASC");
		hibernateReader.setSessionFactory(sessionFactory().getObject());
		hibernateReader.setSaveState(false);
		hibernateReader.afterPropertiesSet();
		return hibernateReader;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() throws IOException{
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
	    factoryBean.setDataSource(this.dataSource);
	    factoryBean.setAnnotatedPackages("com.qiwkreport.qiwk.etl.domain");
	    factoryBean.afterPropertiesSet();
		return factoryBean;
	}
	
	
	@Bean
	public JpaTransactionManager transactionManager() {
	    return new JpaTransactionManager();
	}
	
	
}

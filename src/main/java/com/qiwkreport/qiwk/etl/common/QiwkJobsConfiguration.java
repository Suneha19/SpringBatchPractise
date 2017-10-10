
package com.qiwkreport.qiwk.etl.common;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * This contains all the common job related configuration 
 * @author Abhilash
 *
 */
@Configuration
public class QiwkJobsConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private TaskExecutorConfiguration taskExecutorConfiguration;
	
	@PersistenceContext
    private EntityManager entityManager;

	@Value("${data.chunk.size}")
	private int chunkSize;
	
	@Value("${partition.grid.size}")
	private int gridSize;
	
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() throws IOException{
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
	    factoryBean.setDataSource(this.dataSource);
	   // factoryBean.setAnnotatedPackages("com.qiwkreport.qiwk.etl.domain");
	    factoryBean.setPackagesToScan("com.qiwkreport.qiwk.etl.domain");
	    factoryBean.afterPropertiesSet();
		return factoryBean;
	}
	
	@Bean
	public JpaTransactionManager transactionManager() {
	    return new JpaTransactionManager();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public JobBuilderFactory getJobBuilderFactory() {
		return jobBuilderFactory;
	}

	public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
	}

	public StepBuilderFactory getStepBuilderFactory() {
		return stepBuilderFactory;
	}

	public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
		this.stepBuilderFactory = stepBuilderFactory;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public TaskExecutorConfiguration getTaskExecutorConfiguration() {
		return taskExecutorConfiguration;
	}

	public void setTaskExecutorConfiguration(TaskExecutorConfiguration taskExecutorConfiguration) {
		this.taskExecutorConfiguration = taskExecutorConfiguration;
	}


	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	public int getGridSize() {
		return gridSize;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	@Override
	public String toString() {
		return "QiwkJobsConfiguration [jobBuilderFactory=" + jobBuilderFactory + ", stepBuilderFactory="
				+ stepBuilderFactory + ", dataSource=" + dataSource + ", taskExecutorConfiguration="
				+ taskExecutorConfiguration + ", entityManager=" + entityManager + ", chunkSize=" + chunkSize
				+ ", gridSize=" + gridSize + "]";
	}

	
}

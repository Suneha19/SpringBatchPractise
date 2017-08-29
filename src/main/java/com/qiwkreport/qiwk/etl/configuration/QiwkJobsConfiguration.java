
package com.qiwkreport.qiwk.etl.configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.qiwkreport.qiwk.etl.reader.Reader;
import com.qiwkreport.qiwk.etl.writer.Writer;

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
	
	@Autowired
	private Reader reader; 
	
	@Autowired
	private Writer writer; 
	
	@PersistenceContext
    private EntityManager entityManager;

	@Value("${data.chunk.size}")
	private int chunkSize;
	
	@Value("${partition.grid.size}")
	private int gridSize;

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

	public Reader getReader() {
		return reader;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
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
				+ taskExecutorConfiguration + ", reader=" + reader + ", writer=" + writer + ", entityManager="
				+ entityManager + ", chunkSize=" + chunkSize + ", gridSize=" + gridSize + "]";
	}
	
}

package com.qiwkreport.qiwk.etl.configuration;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.qiwkreport.qiwk.etl.domain.NewUser;
import com.qiwkreport.qiwk.etl.domain.Olduser;
import com.qiwkreport.qiwk.etl.processor.UserProcessor;
import com.qiwkreport.qiwk.etl.reader.Reader;
import com.qiwkreport.qiwk.etl.util.UserRangePartitioner;
import com.qiwkreport.qiwk.etl.writer.HibernateUserItemWriter;

/**
 * @author Abhilash
 *
 */
@Configuration
@EnableBatchProcessing
@Import(BatchConfiguration.class)
public class UserJobConfiguration {
	
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
	
	@Value("${data.chunk.size}")
	private int chunkSize;
	
	@Value("${partition.grid.size}")
	private int gridSize;
	
	@Bean
	public Job userJob() throws Exception {
		return jobBuilderFactory.get("UserJob")
				.incrementer(new RunIdIncrementer())
				.start(userMasterStep())
				.build();
	}
	
	@Bean
	public UserRangePartitioner userPartitioner() {
		UserRangePartitioner partitioner = new UserRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(this.dataSource);
		partitioner.setTable("OLDUSER");
		return partitioner;
	}

	@Bean
	public Step userMasterStep() throws Exception {
		return stepBuilderFactory
				.get("userMasterStep")
				.partitioner(userSlaveStep().getName(), userPartitioner())
				.partitionHandler(userMasterSlaveHandler())
	            .build();
	}

	@Bean
	public PartitionHandler userMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(gridSize);
		handler.setTaskExecutor(taskExecutorConfiguration.taskExecutor());
		handler.setStep(userSlaveStep());
		handler.afterPropertiesSet();
		return handler;
	}

	@Bean
	public Step userSlaveStep() throws Exception {
		return stepBuilderFactory.get("userSlaveStep")
				.<Olduser, NewUser>chunk(chunkSize)
				.reader(reader.jpaUserItemReader(null, null, null))
			    .processor(userProcessor())
				.writer(userItemWriter())
				.build();
	}

	@Bean
	public ItemWriter<NewUser> userItemWriter() {
		return new HibernateUserItemWriter();
	}

	
	@Bean
	public ItemProcessor<Olduser, NewUser> userProcessor() {
		return new UserProcessor();
	}

}
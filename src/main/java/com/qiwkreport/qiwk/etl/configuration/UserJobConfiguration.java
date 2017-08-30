package com.qiwkreport.qiwk.etl.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.qiwkreport.qiwk.etl.domain.NewUser;
import com.qiwkreport.qiwk.etl.domain.Olduser;
import com.qiwkreport.qiwk.etl.processor.UserProcessor;
import com.qiwkreport.qiwk.etl.util.UserRangePartitioner;
import com.qiwkreport.qiwk.etl.writer.JpaUserItemWriter;

/**
 * This is configurations class for UserJob, this class is responsible for moving records from
 * OldUser table to NewUser table
 * 
 * @author Abhilash
 *
 */
@Configuration
@EnableBatchProcessing
@Import(BatchJobConfiguration.class)
public class UserJobConfiguration {
	
	@Autowired
	private QiwkJobsConfiguration configuration;
	
	//@Bean
	public Job userJob() throws Exception {
		return configuration.getJobBuilderFactory()
				.get("UserJob")
				.incrementer(new RunIdIncrementer())
				.start(userMasterStep())
				.build();
	}
	
	@Bean
	public Step userMasterStep() throws Exception {
		return   configuration.getStepBuilderFactory()
				.get("userMasterStep")
				.partitioner(userSlaveStep().getName(), userPartitioner())
				.partitionHandler(userMasterSlaveHandler())
	            .build();
	}
	
	@Bean
	public Step userSlaveStep() throws Exception {
		return configuration.getStepBuilderFactory().get("userSlaveStep")
				.<Olduser, NewUser>chunk(configuration.getChunkSize())
				.reader(jpaUserItemReader(null, null, null))
			    .processor(userProcessor())
				.writer(jpaUserItemWriter())
				.build();
	}
	
	@Bean
	public UserRangePartitioner userPartitioner() {
		UserRangePartitioner partitioner = new UserRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(configuration.getDataSource());
		partitioner.setTable("OLDUSER");
		return partitioner;
	}

	@Bean
	public PartitionHandler userMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(configuration.getGridSize());
		handler.setTaskExecutor(configuration.getTaskExecutorConfiguration().taskExecutor());
		handler.setStep(userSlaveStep());
		handler.afterPropertiesSet();
		return handler;
	}


	/**
	 * {@code} The @StepScope annotation is very imp, as this instantiate this
	 * bean in spring context only when this is loaded
	 */
	
	@Bean
	@StepScope
	public JpaPagingItemReader<Olduser> jpaUserItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
		
		JpaPagingItemReader<Olduser> jpaReader=new JpaPagingItemReader<>();
		jpaReader.setPageSize(configuration.getChunkSize());
		jpaReader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		jpaReader.setQueryString("FROM Olduser o where o.id>=" + fromId + " and o.id <= " + toId +" order by o.id ASC");
		jpaReader.setSaveState(false);
		jpaReader.afterPropertiesSet();
		return jpaReader;
	}
	
	@Bean
	public ItemWriter<NewUser> jpaUserItemWriter() {
		return new JpaUserItemWriter();
	}

	@Bean
	public ItemProcessor<Olduser, NewUser> userProcessor() {
		return new UserProcessor();
	}
	
	/**
	 *  @Warning
	 *  DONOT Use the below implementation of JpaItemWriter code as
	 *  some of records are getting missed . Investigation is pending.
	 */
/*	
	@StepScope
	@Bean
	public ItemWriter<NewUser> jpaUserItemWriter() throws Exception {

		JpaItemWriter<NewUser> writer = new JpaItemWriter<NewUser>();
		writer.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		return writer;
	}*/
	
	
}
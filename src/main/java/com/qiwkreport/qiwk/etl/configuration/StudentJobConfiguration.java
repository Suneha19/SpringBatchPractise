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

import com.qiwkreport.qiwk.etl.common.BatchJobConfiguration;
import com.qiwkreport.qiwk.etl.common.ColumnRangePartitioner;
import com.qiwkreport.qiwk.etl.common.QiwkJobsConfiguration;
import com.qiwkreport.qiwk.etl.domain.NewStudent;
import com.qiwkreport.qiwk.etl.domain.OldStudent;
import com.qiwkreport.qiwk.etl.processor.StudentProcessor;
import com.qiwkreport.qiwk.etl.writer.JpaBasedItemWriter;

/**
 * This is configurations class for StudentJob, this class is responsible for moving records from
 * Student table to NewUser table
 * 
 * @author Abhilash
 *
 */
@Configuration
@EnableBatchProcessing
@Import(BatchJobConfiguration.class)
public class StudentJobConfiguration {
	
	@Autowired
	private QiwkJobsConfiguration configuration;
	
	/**
	 * This job commenetd because student class is one to one joined with teacher class. As soon as teacher jobs starts ,
	 * since  every teacher is related to every students one to one , it will move the data from old student to new 
	 * student table
	 * @return
	 * @throws Exception
	 */
	//@Bean
	public Job studentJob() throws Exception {
		return configuration.getJobBuilderFactory()
				.get("StudentJob")
				.incrementer(new RunIdIncrementer())
				.start(studentMasterStep())
				.build();
	}
	
	@Bean
	public Step studentMasterStep() throws Exception {
		return   configuration.getStepBuilderFactory()
				.get("studentMasterStep")
				.partitioner(studentSlaveStep().getName(), studentPartitioner())
				.partitionHandler(studentMasterSlaveHandler())
	            .build();
	}
	
	@Bean
	public Step studentSlaveStep() throws Exception {
		return configuration.getStepBuilderFactory().get("studentSlaveStep")
				.<OldStudent, NewStudent>chunk(configuration.getChunkSize())
				.reader(jpaStudentItemReader(null, null, null))
			    .processor(studentProcessor())
				.writer(jpaStudentItemWriter())
				.build();
	}
	
	@Bean
	public ColumnRangePartitioner studentPartitioner() {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(configuration.getDataSource());
		partitioner.setTable("OLDSTUDENT");
		return partitioner;
	}

	@Bean
	public PartitionHandler studentMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(configuration.getGridSize());
		handler.setTaskExecutor(configuration.getTaskExecutorConfiguration().taskExecutor());
		handler.setStep(studentSlaveStep());
		handler.afterPropertiesSet();
		return handler;
	}


	/**
	 * {@code} The @StepScope annotation is very imp, as this instantiate this
	 * bean in spring context only when this is loaded
	 */
	
	@Bean
	@StepScope
	public JpaPagingItemReader<OldStudent> jpaStudentItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
		
		JpaPagingItemReader<OldStudent> jpaReader=new JpaPagingItemReader<>();
		jpaReader.setPageSize(configuration.getChunkSize());
		jpaReader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		jpaReader.setQueryString("FROM OldStudent o where o.id>=" + fromId + " and o.id <= " + toId +" order by o.id ASC");
		jpaReader.setSaveState(false);
		jpaReader.afterPropertiesSet();
		return jpaReader;
	}
	
	@Bean
	public ItemWriter<NewStudent> jpaStudentItemWriter() {
		return new JpaBasedItemWriter<NewStudent>();
	}

	@Bean
	public ItemProcessor<OldStudent, NewStudent> studentProcessor() {
		return new StudentProcessor();
	}
	
	/**
	 *  @Warning
	 *  DONOT Use the below implementation of JpaItemWriter code as
	 *  some of records are getting missed . Investigation is pending.
	 */
	
/*	@StepScope
	@Bean
	public ItemWriter<NewUser> jpaUserItemWriter() throws Exception {

		JpaItemWriter<NewUser> writer = new JpaItemWriter<NewUser>();
		writer.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		return writer;
	}*/
	
	
}
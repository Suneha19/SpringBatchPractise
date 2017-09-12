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
import com.qiwkreport.qiwk.etl.domain.NewTeacher;
import com.qiwkreport.qiwk.etl.domain.OldTeacher;
import com.qiwkreport.qiwk.etl.processor.TeacherProcessor;
import com.qiwkreport.qiwk.etl.writer.JpaBasedItemWriter;

/**
 * This is configurations class for Teacher Job, this class is responsible for moving records from
 * OldTeacher table to NewTable table. Also it is onetoone joined with Employee table. It should also
 * move the record from OldEmployee to NewEmployee
 * 
 * @author Abhilash
 *
 */
@Configuration
@EnableBatchProcessing
@Import(BatchJobConfiguration.class)
public class TeacherJobConfiguration {
	
	@Autowired
	private QiwkJobsConfiguration configuration;
	
	@Bean
	public Job teacherJob() throws Exception {
		return configuration.getJobBuilderFactory()
				.get("TeacherJob")
				.incrementer(new RunIdIncrementer())
				.start(teacherSlaveStep())
				.build();
	}
	
	@Bean
	public Step teacherMasterStep() throws Exception {
		return   configuration.getStepBuilderFactory()
				.get("teacherMasterStep")
				.partitioner(teacherSlaveStep().getName(), teacherPartitioner())
				.partitionHandler(teacherMasterSlaveHandler())
	            .build();
	}
	
	@Bean
	public Step teacherSlaveStep() throws Exception {
		return configuration.getStepBuilderFactory().get("teacherSlaveStep")
				.<OldTeacher, NewTeacher>chunk(configuration.getChunkSize())
				.reader(jpaTeacherItemReaderWithoutPartitioning())
			    .processor(teacherProcessor())
				.writer(jpaTeacherItemWriter())
				.build();
	}
	
	@Bean
	public ColumnRangePartitioner teacherPartitioner() {
		ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
		partitioner.setColumn("id");
		partitioner.setDataSource(configuration.getDataSource());
		partitioner.setTable("OLDTEACHER");
		return partitioner;
	}

	@Bean
	public PartitionHandler teacherMasterSlaveHandler() throws Exception {
		TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(configuration.getGridSize());
		handler.setTaskExecutor(configuration.getTaskExecutorConfiguration().taskExecutor());
		handler.setStep(teacherSlaveStep());
		handler.afterPropertiesSet();
		return handler;
	}


	/**
	 * {@code} The @StepScope annotation is very imp, as this instantiate this
	 * bean in spring context only when this is loaded
	 */
	
	@Bean
	@StepScope
	public JpaPagingItemReader<OldTeacher> jpaTeacherItemReader(
			@Value("#{stepExecutionContext[fromId]}") final String fromId,
			@Value("#{stepExecutionContext[toId]}") final String toId,
			@Value("#{stepExecutionContext[name]}") final String name) throws Exception {
		
		JpaPagingItemReader<OldTeacher> jpaReader=new JpaPagingItemReader<>();
		jpaReader.setPageSize(configuration.getChunkSize());
		jpaReader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		jpaReader.setQueryString("FROM OldTeacher o where o.id>=" + fromId + " and o.id <= " + toId +" order by o.id ASC");
		jpaReader.setSaveState(false);
		jpaReader.afterPropertiesSet();
		return jpaReader;
	}
	
	@Bean
	@StepScope
	public JpaPagingItemReader<OldTeacher> jpaTeacherItemReaderWithoutPartitioning() throws Exception {
		
		JpaPagingItemReader<OldTeacher> jpaReader=new JpaPagingItemReader<>();
		jpaReader.setPageSize(configuration.getChunkSize());
		jpaReader.setEntityManagerFactory(configuration.getEntityManager().getEntityManagerFactory());
		jpaReader.setQueryString("FROM OldTeacher");
		jpaReader.setSaveState(false);
		jpaReader.afterPropertiesSet();
		return jpaReader;
	}
	
	@Bean
	public ItemWriter<NewTeacher> jpaTeacherItemWriter() {
		return new JpaBasedItemWriter<NewTeacher>();
	}
	
	

	@Bean
	public ItemProcessor<OldTeacher, NewTeacher> teacherProcessor() {
		return new TeacherProcessor();
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
package com.qiwkreport.qiwk.etl.configuration;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains configurations related to Job launching and Job registry 
 * related to application
 * 
 * @author Abhilash
 *
 */

@Configuration
public class BatchJobConfiguration implements ApplicationContextAware{

	@Autowired
	private JobExplorer jobExplorer;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobRegistry jobRegistry;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	private ApplicationContext applicationContext;
	
	@Bean
	public JobRegistryBeanPostProcessor jobRegistrar() throws Exception{
		JobRegistryBeanPostProcessor registrar=new JobRegistryBeanPostProcessor();
		registrar.setJobRegistry(this.jobRegistry);
		registrar.setBeanFactory(this.applicationContext.getAutowireCapableBeanFactory());
		registrar.afterPropertiesSet();
		return registrar;
	}
	
	@Bean
	public JobOperator jobOperator() throws Exception{
		SimpleJobOperator simpleJobOperator=new SimpleJobOperator();
		simpleJobOperator.setJobLauncher(this.jobLauncher);
		simpleJobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
		simpleJobOperator.setJobRepository(this.jobRepository);
		simpleJobOperator.setJobExplorer(this.jobExplorer);
		simpleJobOperator.setJobRegistry(this.jobRegistry);
		
		simpleJobOperator.afterPropertiesSet();
		return simpleJobOperator;
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}

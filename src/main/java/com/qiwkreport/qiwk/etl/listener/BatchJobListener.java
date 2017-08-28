
package com.qiwkreport.qiwk.etl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 * This class extends JobExecutionListenerSupport and continuously listen the
 * application change and prints the message once completed.
 * 
 * @author abhilash
 *
 */
public class BatchJobListener extends JobExecutionListenerSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchJobListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.STARTED) {
			LOGGER.info("The Job started" + jobExecution.getJobConfigurationName());
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOGGER.info("BATCH JOB COMPLETED SUCCESSFULLY" + jobExecution.getJobConfigurationName());
		}
	}

}

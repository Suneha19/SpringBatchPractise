package com.qiwkreport.qiwk.etl.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLaunchingController {

	@Autowired
	private JobOperator jobOperator;

	private static final Logger LOGGER = LoggerFactory.getLogger(JobLaunchingController.class);

	@RequestMapping(value = "qiwk/etl/start", method = RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public String launch() {
		LOGGER.info("FR Job Started" + System.currentTimeMillis());
		Set<String> jobNames = jobOperator.getJobNames();
		jobNames.parallelStream().forEach(job -> {
			try {
				jobOperator.start(job, null);
			} catch (NoSuchJobException | JobInstanceAlreadyExistsException | JobParametersInvalidException e) {
				e.printStackTrace();
			}
		});
		LOGGER.info("FR Job Finished" + System.currentTimeMillis());
		return "FR Job Completed Sucessfully !";
	}

}

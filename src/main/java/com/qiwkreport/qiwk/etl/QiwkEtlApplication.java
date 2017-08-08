package com.qiwkreport.qiwk.etl;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class QiwkEtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(QiwkEtlApplication.class, args);
	}
	
}
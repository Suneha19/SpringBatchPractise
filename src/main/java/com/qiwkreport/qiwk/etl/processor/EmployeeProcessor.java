package com.qiwkreport.qiwk.etl.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;

public class EmployeeProcessor implements ItemProcessor<Employee, NewEmployee> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeProcessor.class);

	@Override
	public NewEmployee process(Employee employee) throws Exception {
		
		NewEmployee newEmployee=new NewEmployee(employee.getId(),
				employee.getFirstName().toUpperCase(),
				employee.getLastName().toUpperCase());
		
		LOGGER.info("newEmployee--->"+newEmployee);
		LOGGER.info("Employee--->"+employee);
		
		return newEmployee;
	}

}

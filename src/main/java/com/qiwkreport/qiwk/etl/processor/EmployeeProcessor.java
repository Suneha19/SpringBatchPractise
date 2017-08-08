package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;

public class EmployeeProcessor implements ItemProcessor<Employee, NewEmployee> {

	@Override
	public NewEmployee process(Employee employee) throws Exception {

		return new NewEmployee(employee.getId(),
				employee.getFirstName().toUpperCase(),
				employee.getLastName().toUpperCase());
	}

}

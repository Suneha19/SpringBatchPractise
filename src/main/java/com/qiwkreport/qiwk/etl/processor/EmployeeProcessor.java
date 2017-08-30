package com.qiwkreport.qiwk.etl.processor;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;

public class EmployeeProcessor implements ItemProcessor<Employee, NewEmployee> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeProcessor.class);

	@Override
	public NewEmployee process(Employee employee) throws Exception {

		if(employee==null){
			LOGGER.info("Employee is null -->"+employee);
			employee=new Employee();
			employee.setCity("Varanasi");
			employee.setDistrict("Varanansi");
			employee.setFirstName("Abhisu");
			employee.setId(new Random(99999999).nextLong());
			employee.setLastName("Singh");
			employee.setManagerid("Sunu");
			employee.setManagerName("Sunu");
			employee.setPincode("221202");
			employee.setState("U.P");
			employee.setStreet("Managari");
			employee.setVillage("Nevada");
		}
		
		NewEmployee newEmployee = new NewEmployee();
		
		if (employee.getId() > 0) {
			newEmployee.setId(employee.getId());
		}
		if (employee.getFirstName().isEmpty()) {
			newEmployee.setFirstName("Abhilash");
		} else {
			newEmployee.setFirstName(employee.getFirstName());
		}

		if (employee.getLastName().isEmpty()) {
			newEmployee.setLastName("Singh");
		} else {
			newEmployee.setLastName(employee.getLastName());
		}

		if (employee.getVillage().isEmpty()) {
			newEmployee.setVillage("Nevada");
		} else {
			newEmployee.setVillage(employee.getVillage());
		}

		if (employee.getStreet().isEmpty()) {
			newEmployee.setStreet("Mangari");
		} else {
			newEmployee.setStreet(employee.getStreet());
		}

		if (employee.getCity().isEmpty()) {
			newEmployee.setCity("Varanasi");
		} else {
			newEmployee.setCity(employee.getCity());
		}

		if (employee.getDistrict().isEmpty()) {
			newEmployee.setDistrict("Varanasi");
		} else {
			newEmployee.setDistrict(employee.getDistrict());
		}

		if (employee.getState().isEmpty()) {
			newEmployee.setState("Abhilash");
		} else {
			newEmployee.setState(employee.getState());
		}

		if (employee.getPincode().isEmpty()) {
			newEmployee.setPincode("Varanasi");
		} else {
			newEmployee.setPincode(employee.getPincode());
		}

		if (employee.getManagerid().isEmpty()) {
			newEmployee.setManagerid("Varanasi");
		} else {
			newEmployee.setManagerid(employee.getManagerid());
		}

		if (employee.getManagerName().isEmpty()) {
			newEmployee.setManagerName("Abhilash");
		} else {
			newEmployee.setManagerName(employee.getManagerName());
		}

		return newEmployee;
	}

}

package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.NewEmployee;

public class Processor implements ItemProcessor<Employee, NewEmployee> {

	@Override
	public NewEmployee process(Employee employee) throws Exception {

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

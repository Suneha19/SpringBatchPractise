package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.Department;
import com.qiwkreport.qiwk.etl.domain.NewDepartment;

public class DepartmentProcessor implements ItemProcessor<Department, NewDepartment> {
	

	@Override
	public NewDepartment process(Department department) throws Exception {

		NewDepartment newDepartment = new NewDepartment();
		newDepartment.setDepartmentId(department.getDepartmentId());
		newDepartment.setDepartmentLocation(department.getDepartmentLocation().toUpperCase());
		newDepartment.setDepartmentName(department.getDepartmentName().toUpperCase());
		newDepartment.setDepartmentWork(department.getDepartmentWork().toUpperCase());
		
		return newDepartment;
	}

}

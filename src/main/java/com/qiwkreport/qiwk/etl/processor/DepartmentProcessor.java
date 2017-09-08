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
		newDepartment.setDepartmentManager(department.getDepartmentManager());
		newDepartment.setDepartmentEmployeeCount(department.getDepartmentEmployeeCount());
		newDepartment.setDepartmentAverageSalary(department.getDepartmentAverageSalary());
		newDepartment.setDepartmentHead(department.getDepartmentHead());
		newDepartment.setDepartmentResponsibility1(department.getDepartmentResponsibility1());
		newDepartment.setDepartmentResponsibility2(department.getDepartmentResponsibility2());
		
		return newDepartment;
	}

}

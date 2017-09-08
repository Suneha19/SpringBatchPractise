package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.NewStudent;
import com.qiwkreport.qiwk.etl.domain.OldStudent;

public class StudentProcessor implements ItemProcessor<OldStudent, NewStudent> {

	@Override
	public NewStudent process(OldStudent item) throws Exception {
		
		NewStudent newStudent = new NewStudent();
		
		newStudent.setStudentAddress1(item.getStudentAddress1());
		newStudent.setStudentAddress2(item.getStudentAddress2());
		newStudent.setStudentAddress3(item.getStudentAddress3());
		newStudent.setStudentAge(item.getStudentAge());
		newStudent.setStudentClass(item.getStudentClass());
		newStudent.setStudentCurrentClass(item.getStudentCurrentClassSection());
		newStudent.setStudentHomePhone(item.getStudentHomePhone());
		newStudent.setStudentId(item.getStudentId());
		newStudent.setStudentMobile(item.getStudentMobile());
		newStudent.setStudentName(item.getStudentName());
		newStudent.setStudentPercentage(item.getStudentPercentage());
		
		return newStudent;
	}

}

package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.NewStudent;
import com.qiwkreport.qiwk.etl.domain.NewTeacher;
import com.qiwkreport.qiwk.etl.domain.OldStudent;
import com.qiwkreport.qiwk.etl.domain.OldTeacher;

public class TeacherProcessor implements ItemProcessor<OldTeacher, NewTeacher> {
	

	@Override
	public NewTeacher process(OldTeacher oldTeacher) throws Exception {
		
		NewTeacher newTeacher=new NewTeacher();
		
		newTeacher.setId(oldTeacher.getId());
		newTeacher.setNewStudent(getNewStudent(oldTeacher.getOldStudent()));
		newTeacher.setTeacherAddress1(oldTeacher.getTeacherAddress1());
		newTeacher.setTeacherAddress2(oldTeacher.getTeacherAddress2());
		newTeacher.setTeacherAddress3(oldTeacher.getTeacherAddress3());
		newTeacher.setTeacherAge(oldTeacher.getTeacherAge());
		newTeacher.setTeacherEducation(oldTeacher.getTeacherEducation());
		newTeacher.setTeacherName(oldTeacher.getTeacherName());
		newTeacher.setTeacherPhone(oldTeacher.getTeacherPhone());
		newTeacher.setTeacherSalary(oldTeacher.getTeacherSalary());
		
		return newTeacher;
	}

	private NewStudent getNewStudent(OldStudent oldStudent) {
		NewStudent newStudent = new NewStudent();
		
		newStudent.setStudentAddress1(oldStudent.getStudentAddress1());
		newStudent.setStudentAddress2(oldStudent.getStudentAddress2());
		newStudent.setStudentAddress3(oldStudent.getStudentAddress3());
		newStudent.setStudentAge(oldStudent.getStudentAge());
		newStudent.setStudentClass(oldStudent.getStudentClass());
		newStudent.setStudentCurrentClass(oldStudent.getStudentCurrentClassSection());
		newStudent.setStudentHomePhone(oldStudent.getStudentHomePhone());
		newStudent.setStudentId(oldStudent.getStudentId());
		newStudent.setStudentMobile(oldStudent.getStudentMobile());
		newStudent.setStudentName(oldStudent.getStudentName());
		newStudent.setStudentPercentage(oldStudent.getStudentPercentage());
		
		return newStudent;
	}

}

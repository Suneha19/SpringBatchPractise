package com.qiwkreport.qiwk.etl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qiwkreport.qiwk.etl.domain.Department;
import com.qiwkreport.qiwk.etl.domain.Employee;
import com.qiwkreport.qiwk.etl.domain.OldStudent;
import com.qiwkreport.qiwk.etl.domain.OldTeacher;
import com.qiwkreport.qiwk.etl.repository.EmployeeRepository;
import com.qiwkreport.qiwk.etl.repository.OldStudentRepository;
import com.qiwkreport.qiwk.etl.repository.OldTeacherRepository;

@Component
public class SaveDataInDB {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private OldStudentRepository oldStudentRepository;
	
	@Autowired
	private OldTeacherRepository oldTeacherRepository;

	private long departmentId = 1;

	

	public void saveEmployeeData() {

		List<Employee> listOfEmploee = new ArrayList<>();
		long count = 0L;
		// put the value as per the max DB id column
		for (long i = 8225207; i < 9926000; i++) {
			Employee employee = new Employee();
			employee.setBloodGroup("B+");
			employee.setCity("Varanasi");
			employee.setDepartment(getDepartmentOfEmployee());
			employee.setDistrict("Varanasi");
			employee.setEmployeeId("12345");
			employee.setFatherName("TARKESHWAR SINGH");
			employee.setFirstName("Abhilash");
			employee.setHomeNumber("9986639460");
			employee.setId(i);
			employee.setLastName("Singh");
			employee.setManagerid("19");
			employee.setManagerName("Suneha");
			employee.setMobileNumber("9986639460");
			employee.setMotherName("SUDHA SINGH");
			employee.setOfficeNumber("9986639460");
			employee.setPincode("221202");
			employee.setPrimarySkill("JAVA");
			employee.setSecondarySkill("ORACLE");
			employee.setState("UttarPradesh");
			employee.setStreet("Mangari");
			employee.setVillage("Nevada");
			employee.setYearOfExperince("5 Years");
			listOfEmploee.add(employee);
			count++;
			if (count % 20000 == 0) {
				count = 0;
				employeeRepository.save(listOfEmploee);
				listOfEmploee = new ArrayList<>();
			}
		}

	}

	private Department getDepartmentOfEmployee() {
		Department department = new Department();
		department.setDepartmentAverageSalary("567700");
		department.setDepartmentEmployeeCount("10000");
		department.setDepartmentHead("SUDHA SINGH");
		department.setDepartmentId(departmentId++);
		department.setDepartmentLocation("Babatpur");
		department.setDepartmentManager("TARKESHWAR SINGH");
		department.setDepartmentName("Teaching");
		department.setDepartmentResponsibility1("EDUCATION DEPARTMENT");
		department.setDepartmentResponsibility2("Informatio");
		department.setDepartmentWork("Teaches the Customer");
		if (departmentId == 300000) {
			departmentId = 0;
		}
		return department;
	}

	public void saveTeacherData() {
		List<OldTeacher> listOfOldTeacher = new ArrayList<>();
		long teacherId = 1100001;
		long count = 0L;
		for (long i = teacherId; i <= 1700000; i++) {
		OldTeacher oldTeacher = new OldTeacher();
		oldTeacher.setId(i);
		oldTeacher.setOldStudent(getOldStudent(i));
		oldTeacher.setTeacherAddress1("16/2 Room No 16 Opposite Max Show room");
		oldTeacher.setTeacherAddress2("TNR Layout, lane beside Igate optics");
		oldTeacher.setTeacherAddress3("Marathalli, Bangalore,Karanataka");
		oldTeacher.setTeacherAge("27");
		oldTeacher.setTeacherEducation("B.Tech in Computer Science");
		oldTeacher.setTeacherName("Abhilash");
		oldTeacher.setTeacherPhone("+91-9986639460");
		oldTeacher.setTeacherSalary("1200000");
		listOfOldTeacher.add(oldTeacher);
		count++;
		if (count % 50000 == 0) {
			count = 0;
			oldTeacherRepository.save(listOfOldTeacher);
			listOfOldTeacher = new ArrayList<>();
		}
		
		}
	}

	

	private OldStudent getOldStudent(long studentId) {

			OldStudent oldStudent = new OldStudent();
			oldStudent.setStudentAddress1("97/2A Room No 16,2nd floor, TNR Layout");
			oldStudent.setStudentAddress2("Lane Beside Igate optics, opposite Max showroom");
			oldStudent.setStudentAddress3("Marathalli ,Bnagalore-560037");
			oldStudent.setStudentAge("27");
			oldStudent.setStudentClass("B.teach 1st year 1st Semester in CS Branch");
			oldStudent.setStudentCurrentClass("B.teach 1st year 1st Semester in CS Branch");
			oldStudent.setStudentHomePhone("+91-9986639460");
			oldStudent.setStudentId(studentId);
			oldStudent.setStudentMobile("+91-9986639460");
			oldStudent.setStudentName("Abhilash Singh");
			oldStudent.setStudentPercentage("84%");
			return oldStudent;
	}
	
	public void saveStudent() {
		List<OldStudent> listOfOldStudent = new ArrayList<>();
		long count = 0L;
		 long studentId = 500002;

		for (long i = studentId; i <= 600000; i++) {
			OldStudent oldStudent = new OldStudent();
			oldStudent.setStudentAddress1("97/2A Room No 16,2nd floor, TNR Layout");
			oldStudent.setStudentAddress2("Lane Beside Igate optics, opposite Max showroom");
			oldStudent.setStudentAddress3("Marathalli ,Bnagalore-560037");
			oldStudent.setStudentAge("27");
			oldStudent.setStudentClass("B.teach 1st year 1st Semester in CS Branch");
			oldStudent.setStudentCurrentClass("B.teach 1st year 1st Semester in CS Branch");
			oldStudent.setStudentHomePhone("+91-9986639460");
			oldStudent.setStudentId(i);
			oldStudent.setStudentMobile("+91-9986639460");
			oldStudent.setStudentName("Abhilash Singh");
			oldStudent.setStudentPercentage("84%");
			listOfOldStudent.add(oldStudent);
			count++;
			if (count % 50000 == 0) {
				count = 0;
				oldStudentRepository.save(listOfOldStudent);
				listOfOldStudent = new ArrayList<>();
			}
		}
	}

}

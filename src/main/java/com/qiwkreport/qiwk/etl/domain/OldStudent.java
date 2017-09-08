/**
 * 
 */
package com.qiwkreport.qiwk.etl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author abhilash
 *
 */
@Entity
@Table(name = "OLDSTUDENT")
public class OldStudent {
	
	/**
	 * Don't include any id genrator type here
	 * when developing actual FR/NC job  use database sequence generator or any other genrator based on 
	 * business decision. 
	 */
	@Id
	@Column(name = "STUDENTID")
	private long studentId;

	@Column(name = "STUDENTNAME")
	private String studentName;

	@Column(name = "STUDENETAGE")
	private String studentAge;

	@Column(name = "STUDENETCLASS")
	private String studentClass;

	@Column(name = "STUDENETADDRESS1")
	private String studentAddress1;

	@Column(name = "STUDENETADDRESS2")
	private String studentAddress2;

	@Column(name = "STUDENETADDRESS3")
	private String studentAddress3;

	@Column(name = "STUDENETMOBILE")
	private String studentMobile;

	@Column(name = "STUDENETHOMEPHONE")
	private String studentHomePhone;

	@Column(name = "STUDENETCURRENRTCLASS")
	private String studentCurrentClass;

	@Column(name = "STUDENETCURRENTCLASSSECTION")
	private String studentCurrentClassSection;

	@Column(name = "STUDENETPERCENTAGE")
	private String studentPercentage;
	
	public OldStudent() {
		super();
	}

	public OldStudent(long studentId, String studentName, String studentAge, String studentClass,
			String studentAddress1, String studentAddress2, String studentAddress3, String studentMobile,
			String studentHomePhone, String studentCurrentClass, String studentCurrentClassSection,
			String studentPercentage) {
		super();
		this.studentId = studentId;
		this.studentName = studentName;
		this.studentAge = studentAge;
		this.studentClass = studentClass;
		this.studentAddress1 = studentAddress1;
		this.studentAddress2 = studentAddress2;
		this.studentAddress3 = studentAddress3;
		this.studentMobile = studentMobile;
		this.studentHomePhone = studentHomePhone;
		this.studentCurrentClass = studentCurrentClass;
		this.studentCurrentClassSection = studentCurrentClassSection;
		this.studentPercentage = studentPercentage;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentAge() {
		return studentAge;
	}

	public void setStudentAge(String studentAge) {
		this.studentAge = studentAge;
	}

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	public String getStudentAddress1() {
		return studentAddress1;
	}

	public void setStudentAddress1(String studentAddress1) {
		this.studentAddress1 = studentAddress1;
	}

	public String getStudentAddress2() {
		return studentAddress2;
	}

	public void setStudentAddress2(String studentAddress2) {
		this.studentAddress2 = studentAddress2;
	}

	public String getStudentAddress3() {
		return studentAddress3;
	}

	public void setStudentAddress3(String studentAddress3) {
		this.studentAddress3 = studentAddress3;
	}

	public String getStudentMobile() {
		return studentMobile;
	}

	public void setStudentMobile(String studentMobile) {
		this.studentMobile = studentMobile;
	}

	public String getStudentHomePhone() {
		return studentHomePhone;
	}

	public void setStudentHomePhone(String studentHomePhone) {
		this.studentHomePhone = studentHomePhone;
	}

	public String getStudentCurrentClass() {
		return studentCurrentClass;
	}

	public void setStudentCurrentClass(String studentCurrentClass) {
		this.studentCurrentClass = studentCurrentClass;
	}

	public String getStudentCurrentClassSection() {
		return studentCurrentClassSection;
	}

	public void setStudentCurrentClassSection(String studentCurrentClassSection) {
		this.studentCurrentClassSection = studentCurrentClassSection;
	}

	public String getStudentPercentage() {
		return studentPercentage;
	}

	public void setStudentPercentage(String studentPercentage) {
		this.studentPercentage = studentPercentage;
	}

	@Override
	public String toString() {
		return "OldStudent [studentId=" + studentId + ", studentName=" + studentName + ", studentAge=" + studentAge
				+ ", studentClass=" + studentClass + ", studentAddress1=" + studentAddress1 + ", studentAddress2="
				+ studentAddress2 + ", studentAddress3=" + studentAddress3 + ", studentMobile=" + studentMobile
				+ ", studentHomePhone=" + studentHomePhone + ", studentCurrentClass=" + studentCurrentClass
				+ ", studentCurrentClassSection=" + studentCurrentClassSection + ", studentPercentage="
				+ studentPercentage + "]";
	}


}

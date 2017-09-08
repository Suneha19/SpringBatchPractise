package com.qiwkreport.qiwk.etl.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "OLDTEACHER")
public class OldTeacher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "TEACHERNAME")
	private String teacherName;

	@Column(name = "TEACHERAGE")
	private String teacherAge;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "STUDENTID")
	private OldStudent oldStudent;

	@Column(name = "TEACHEREDUCATION")
	private String teacherEducation;

	@Column(name = "TEACHERSALARY")
	private String teacherSalary;

	@Column(name = "TEACHERPHONE")
	private String teacherPhone;

	@Column(name = "TEACHERADDRESS1")
	private String teacherAddress1;

	@Column(name = "TEACHERADDRESS2")
	private String teacherAddress2;

	@Column(name = "TEACHERADDRESS3")
	private String teacherAddress3;

	public OldTeacher() {
		super();
	}

	public OldTeacher(long id, String teacherName, String teacherAge, OldStudent oldStudent, String teacherEducation,
			String teacherSalary, String teacherPhone, String teacherAddress1, String teacherAddress2,
			String teacherAddress3) {
		super();
		this.id = id;
		this.teacherName = teacherName;
		this.teacherAge = teacherAge;
		this.oldStudent = oldStudent;
		this.teacherEducation = teacherEducation;
		this.teacherSalary = teacherSalary;
		this.teacherPhone = teacherPhone;
		this.teacherAddress1 = teacherAddress1;
		this.teacherAddress2 = teacherAddress2;
		this.teacherAddress3 = teacherAddress3;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherAge() {
		return teacherAge;
	}

	public void setTeacherAge(String teacherAge) {
		this.teacherAge = teacherAge;
	}

	public OldStudent getOldStudent() {
		return oldStudent;
	}

	public void setOldStudent(OldStudent oldStudent) {
		this.oldStudent = oldStudent;
	}

	public String getTeacherEducation() {
		return teacherEducation;
	}

	public void setTeacherEducation(String teacherEducation) {
		this.teacherEducation = teacherEducation;
	}

	public String getTeacherSalary() {
		return teacherSalary;
	}

	public void setTeacherSalary(String teacherSalary) {
		this.teacherSalary = teacherSalary;
	}

	public String getTeacherPhone() {
		return teacherPhone;
	}

	public void setTeacherPhone(String teacherPhone) {
		this.teacherPhone = teacherPhone;
	}

	public String getTeacherAddress1() {
		return teacherAddress1;
	}

	public void setTeacherAddress1(String teacherAddress1) {
		this.teacherAddress1 = teacherAddress1;
	}

	public String getTeacherAddress2() {
		return teacherAddress2;
	}

	public void setTeacherAddress2(String teacherAddress2) {
		this.teacherAddress2 = teacherAddress2;
	}

	public String getTeacherAddress3() {
		return teacherAddress3;
	}

	public void setTeacherAddress3(String teacherAddress3) {
		this.teacherAddress3 = teacherAddress3;
	}

	@Override
	public String toString() {
		return "OldTeacher [id=" + id + ", teacherName=" + teacherName + ", teacherAge=" + teacherAge + ", oldStudent="
				+ oldStudent + ", teacherEducation=" + teacherEducation + ", teacherSalary=" + teacherSalary
				+ ", teacherPhone=" + teacherPhone + ", teacherAddress1=" + teacherAddress1 + ", teacherAddress2="
				+ teacherAddress2 + ", teacherAddress3=" + teacherAddress3 + "]";
	}

}

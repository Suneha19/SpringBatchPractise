package com.qiwkreport.qiwk.etl.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//@Entity
//@Table(name = "EMPLOYEE")
public class Employee {

	/**
	 * Don't include any id genrator type here
	 * when developing actual FR/NC job  use database sequence generator or any other genrator based on 
	 * business decision. 
	 */
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;

	@Column(name = "FIRSTNAME")
	private String firstName;

	@Column(name = "LASTNAME")
	private String lastName;

	@Column(name = "VILLAGE")
	private String village;

	@Column(name = "STREET")
	private String street;

	@Column(name = "CITY")
	private String city;

	@Column(name = "DISTRICT")
	private String district;

	@Column(name = "STATE")
	private String state;

	@Column(name = "PINCODE")
	private String pincode;

	@Column(name = "MANAGERID")
	private String managerid;

	@Column(name = "MANAGERNAME")
	private String managerName;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "DEPARTMENTID")
	private Department department;

	@Column(name = "FATHERNAME")
	private String fatherName;

	@Column(name = "MOTHERNAME")
	private String motherName;

	@Column(name = "EMPLOYEEID")
	private String employeeId;

	@Column(name = "BLOODGROUP")
	private String bloodGroup;

	@Column(name = "MOBILENUMBER")
	private String mobileNumber;

	@Column(name = "OFFICENUMBER")
	private String officeNumber;

	@Column(name = "HOMENUMBER")
	private String homeNumber;

	@Column(name = "PRIMARYSKILL")
	private String primarySkill;

	@Column(name = "SECONDARYSKILL")
	private String secondarySkill;

	@Column(name = "YEAROFEXPERINCE")
	private String yearOfExperince;

	public Employee() {
		super();
	}

	public Employee(long id, String firstName, String lastName, String village, String street, String city,
			String district, String state, String pincode, String managerid, String managerName, Department department,
			String fatherName, String motherName, String employeeId, String bloodGroup, String mobileNumber,
			String officeNumber, String homeNumber, String primarySkill, String secondarySkill,
			String yearOfExperince) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.village = village;
		this.street = street;
		this.city = city;
		this.district = district;
		this.state = state;
		this.pincode = pincode;
		this.managerid = managerid;
		this.managerName = managerName;
		this.department = department;
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.employeeId = employeeId;
		this.bloodGroup = bloodGroup;
		this.mobileNumber = mobileNumber;
		this.officeNumber = officeNumber;
		this.homeNumber = homeNumber;
		this.primarySkill = primarySkill;
		this.secondarySkill = secondarySkill;
		this.yearOfExperince = yearOfExperince;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOfficeNumber() {
		return officeNumber;
	}

	public void setOfficeNumber(String officeNumber) {
		this.officeNumber = officeNumber;
	}

	public String getHomeNumber() {
		return homeNumber;
	}

	public void setHomeNumber(String homeNumber) {
		this.homeNumber = homeNumber;
	}

	public String getPrimarySkill() {
		return primarySkill;
	}

	public void setPrimarySkill(String primarySkill) {
		this.primarySkill = primarySkill;
	}

	public String getSecondarySkill() {
		return secondarySkill;
	}

	public void setSecondarySkill(String secondarySkill) {
		this.secondarySkill = secondarySkill;
	}

	public String getYearOfExperince() {
		return yearOfExperince;
	}

	public void setYearOfExperince(String yearOfExperince) {
		this.yearOfExperince = yearOfExperince;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", village=" + village
				+ ", street=" + street + ", city=" + city + ", district=" + district + ", state=" + state + ", pincode="
				+ pincode + ", managerid=" + managerid + ", managerName=" + managerName + ", department=" + department
				+ ", fatherName=" + fatherName + ", motherName=" + motherName + ", employeeId=" + employeeId
				+ ", bloodGroup=" + bloodGroup + ", mobileNumber=" + mobileNumber + ", officeNumber=" + officeNumber
				+ ", homeNumber=" + homeNumber + ", primarySkill=" + primarySkill + ", secondarySkill=" + secondarySkill
				+ ", yearOfExperince=" + yearOfExperince + "]";
	}

}

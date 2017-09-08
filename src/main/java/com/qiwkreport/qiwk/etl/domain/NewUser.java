package com.qiwkreport.qiwk.etl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NEWUSER")
public class NewUser {

	/**
	 * {@code} Don't include any Hibernate generator here, as before insertion
	 * update operation is called by hibernate using ids if we use generator ,
	 * Hibernate will generate its own id and try to move it.And then it will
	 * throw org.hibernate.StaleStateException exception
	 */
	/**
	 * Don't include any id genrator type here,
	 * this application copies old id to the new id of the system.
	 */
	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "AGE")
	private int age;

	@Column(name = "EMAILID")
	private String emailId;

	@Column(name = "FIRSTNAME")
	private String firstName;

	@Column(name = "LASTNAME")
	private String lastName;

	@Column(name = "MOBILENUMBER")
	private String mobileNumber;

	@Column(name = "SALARY")
	private String salary;

	@Column(name = "ISABOVE18")
	private String isAbove18;

	public NewUser() {
		super();
	}

	public NewUser(int id, String username, String password, int age, String emailId, String firstName, String lastName,
			String mobileNumber, String salary, String isAbove18) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.age = age;
		this.emailId = emailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.salary = salary;
		this.isAbove18 = isAbove18;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String isAbove18() {
		return isAbove18;
	}

	public void setAbove18(String isAbove18) {
		this.isAbove18 = isAbove18;
	}

	@Override
	public String toString() {
		return "Olduser [id=" + id + ", username=" + username + ", password=" + password + ", age=" + age + ", emailId="
				+ emailId + ", firstName=" + firstName + ", lastName=" + lastName + ", mobileNumber=" + mobileNumber
				+ ", salary=" + salary + ", isAbove18=" + isAbove18 + "]";
	}
}

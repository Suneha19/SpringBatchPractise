package com.qiwkreport.qiwk.etl.domain;

public class Employee {

	private long id;
	private String firstName;
	private String lastName;
	private String village;
	private String street;
	private String city;
	private String district;
	private String state;
	private String pincode;
	private String managerid;
	private String managerName;
	
	public Employee(long id, String firstName, String lastName, String village, String street, String city,
			String district, String state, String pincode, String managerid, String managerName) {
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
	}
	
	public Employee() {
		super();
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

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", village=" + village
				+ ", street=" + street + ", city=" + city + ", district=" + district + ", state=" + state + ", pincode="
				+ pincode + ", managerid=" + managerid + ", managerName=" + managerName + "]";
	}


}

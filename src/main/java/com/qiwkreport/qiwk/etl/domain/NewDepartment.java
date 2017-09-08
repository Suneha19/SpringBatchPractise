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
@Table(name = "NEWDEPARTMENT")
public class NewDepartment {

	/**
	 * Don't include any id genrator type here
	 * when developing actual FR/NC job  use database sequence generator or any other genrator based on 
	 * business decision. 
	 */
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DEPARTMENTID")
	private long departmentId;

	@Column(name = "DEPARTMENTNAME")
	private String departmentName;

	@Column(name = "DEPARTMENTLOCATION")
	private String departmentLocation;

	@Column(name = "DEPARTMENTWORK")
	private String departmentWork;

	@Column(name = "DEPARTMENTMANAGER")
	private String departmentManager;

	@Column(name = "DEPARTMENTEMPLOYEECOUNT")
	private String departmentEmployeeCount;

	@Column(name = "DEPARTMENTAVERAGESALARY")
	private String departmentAverageSalary;

	@Column(name = "DEPARTMENTHEAD")
	private String departmentHead;

	@Column(name = "DEPARTMENTRESPONSIBILTY1")
	private String departmentResponsibility1;

	@Column(name = "DEPARTMENTRESPONSIBILTY2")
	private String departmentResponsibility2;

	public NewDepartment() {
		super();
	}

	public NewDepartment(long departmentId, String departmentName, String departmentLocation, String departmentWork,
			String departmentManager, String departmentEmployeeCount, String departmentAverageSalary,
			String departmentHead, String departmentResponsibility1, String departmentResponsibility2) {
		super();
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.departmentLocation = departmentLocation;
		this.departmentWork = departmentWork;
		this.departmentManager = departmentManager;
		this.departmentEmployeeCount = departmentEmployeeCount;
		this.departmentAverageSalary = departmentAverageSalary;
		this.departmentHead = departmentHead;
		this.departmentResponsibility1 = departmentResponsibility1;
		this.departmentResponsibility2 = departmentResponsibility2;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentLocation() {
		return departmentLocation;
	}

	public void setDepartmentLocation(String departmentLocation) {
		this.departmentLocation = departmentLocation;
	}

	public String getDepartmentWork() {
		return departmentWork;
	}

	public void setDepartmentWork(String departmentWork) {
		this.departmentWork = departmentWork;
	}

	public String getDepartmentManager() {
		return departmentManager;
	}

	public void setDepartmentManager(String departmentManager) {
		this.departmentManager = departmentManager;
	}

	public String getDepartmentEmployeeCount() {
		return departmentEmployeeCount;
	}

	public void setDepartmentEmployeeCount(String departmentEmployeeCount) {
		this.departmentEmployeeCount = departmentEmployeeCount;
	}

	public String getDepartmentAverageSalary() {
		return departmentAverageSalary;
	}

	public void setDepartmentAverageSalary(String departmentAverageSalary) {
		this.departmentAverageSalary = departmentAverageSalary;
	}

	public String getDepartmentHead() {
		return departmentHead;
	}

	public void setDepartmentHead(String departmentHead) {
		this.departmentHead = departmentHead;
	}

	public String getDepartmentResponsibility1() {
		return departmentResponsibility1;
	}

	public void setDepartmentResponsibility1(String departmentResponsibility1) {
		this.departmentResponsibility1 = departmentResponsibility1;
	}

	public String getDepartmentResponsibility2() {
		return departmentResponsibility2;
	}

	public void setDepartmentResponsibility2(String departmentResponsibility2) {
		this.departmentResponsibility2 = departmentResponsibility2;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	@Override
	public String toString() {
		return "Department [ departmentId=" + departmentId + ", departmentName=" + departmentName
				+ ", departmentLocation=" + departmentLocation + ", departmentWork=" + departmentWork
				+ ", departmentManager=" + departmentManager + ", departmentEmployeeCount=" + departmentEmployeeCount
				+ ", departmentAverageSalary=" + departmentAverageSalary + ", departmentHead=" + departmentHead
				+ ", departmentResponsibility1=" + departmentResponsibility1 + ", departmentResponsibility2="
				+ departmentResponsibility2 + "]";
	}

}

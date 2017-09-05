/**
 * 
 */
package com.qiwkreport.qiwk.etl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author abhilash
 *
 */
@Entity
@Table(name = "DEPARTMENT")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DEPARTMENTID")
	private long departmentId;

	@Column(name = "DEPARTMENTNAME")
	private String departmentName;

	@Column(name = "DEPARTMENTLOCATION")
	private String departmentLocation;

	@Column(name = "DEPARTMENTWORK")
	private String departmentWork;

	public Department() {
		super();
	}

	public Department(long departmentId, String departmentName, String departmentLocation, String departmentWork) {
		super();
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.departmentLocation = departmentLocation;
		this.departmentWork = departmentWork;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
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

	@Override
	public String toString() {
		return "Department [departmentId=" + departmentId + ", departmentName=" + departmentName
				+ ", departmentLocation=" + departmentLocation + ", departmentWork=" + departmentWork + "]";
	}

}

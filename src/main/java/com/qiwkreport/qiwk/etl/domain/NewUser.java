package com.qiwkreport.qiwk.etl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "NEWUSER")
public class NewUser {

	/**
	 * {@code}
	 *  Don't include any Hibernate generator here, as before insertion update operation is called by hibernate using ids
	 *  if we use generator , Hibernate will generate its own id and try to move it.And then it will throw
	 *  org.hibernate.StaleStateException exception
	 */
	@Id
	@Column(name = "ID")
	int id;
	@Column(name = "USERNAME")
	String username;
	@Column(name = "PASSWORD")
	String password;
	@Column(name = "AGE")
	int age;

	public NewUser(int id, String username, String password, int age) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.age = age;
	}

	public NewUser() {
		super();
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

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", age=" + age + "]";
	}
}

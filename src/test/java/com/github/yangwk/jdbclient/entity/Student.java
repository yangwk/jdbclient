package com.github.yangwk.jdbclient.entity;

import java.util.Date;

public class Student{
	private String name;
	private Integer age;
	private Integer id;
	private Date birthDate;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	@Override
	public String toString() {
		return "{ \"name\":\"" + this.name + "\" , \"age\":\"" + this.age 
					+ "\" , \"id\":\"" + this.id 
					+ "\" , \"birthDate\":\"" + String.valueOf(this.birthDate) + "\" }";
	}
	
	
	
	
}

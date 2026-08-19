package com.nt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Person {
	@Id
	private Integer personID;
	@Column
	private Integer personAge;
	@Column
	private String personName;

	public Person(Integer personID, Integer personAge, String personName) {
		super();
		this.personID = personID;
		this.personAge = personAge;
		this.personName = personName;
	}

	public Integer getPersonID() {
		return personID;
	}

	public void setPersonID(Integer personID) {
		this.personID = personID;
	}

	public Integer getPersonAge() {
		return personAge;
	}

	public void setPersonAge(Integer personAge) {
		this.personAge = personAge;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	@Override
	public String toString() {
		return "Person [personID=" + personID + ", personAge=" + personAge + ", personName=" + personName + "]";
	}

}

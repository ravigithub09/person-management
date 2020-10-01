package com.mars.app.person.model;

public class Address {
	private Integer id;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private Integer personId;

	public Address() {
		super();
	}

	public Address(Integer id, String street, String city, String state, String postalCode, Integer personId) {
		super();
		this.id = id;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.personId = personId;
	}

	public Address(String street, String city, String state, String postalCode) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
	}

	public Address(Integer id, String street, String city, String state, String postalCode) {
		super();
		this.id = id;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
	}

	public Address(String street, String city, String state, String postalCode, Integer personId) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.personId = personId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

}

package com.oakclub.android.model;

import java.io.Serializable;

public class LocationObject implements Serializable {
	String id;
	String name;
	String country = "";
	String country_code = "";
	CoordinatesObject coordinates;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public CoordinatesObject getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(CoordinatesObject coordinates) {
		this.coordinates = coordinates;
	}
}

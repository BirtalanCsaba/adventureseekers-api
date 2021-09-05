package com.adventureseekers.adventurewebapi.user;

import java.util.Arrays;
import java.util.Date;

public class CustomUserDetail {
	
	private String username;
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private Date birthDate;
	
	private String description;
	
	private String country;
	
	private String county;
	
	private String city;
	
	private byte[] profileImage;
	
	public CustomUserDetail() {
		
	}

	public CustomUserDetail(String username, String email, String firstName, String lastName, Date birthDate,
			String description, String country, String county, String city, byte[] profileImage) {
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.description = description;
		this.country = country;
		this.county = county;
		this.city = city;
		this.profileImage = profileImage;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	@Override
	public String toString() {
		return "CustomUserDetail [username=" + username + ", email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", birthDate=" + birthDate + ", description=" + description + ", country="
				+ country + ", county=" + county + ", city=" + city + ", profileImage=" + Arrays.toString(profileImage)
				+ "]";
	}
	
}

package com.adventureseekers.adventurewebapi.entity;

import java.util.Arrays;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="user_detail")
public class UserDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "county")
	private String county;
	
	@Column(name = "city")
	private String city;
	
	@Lob
    @Column(name = "profile_image", columnDefinition = "mediumblob")
	private byte[] profileImage;
	
	@OneToOne(mappedBy = "userDetail",
			cascade = CascadeType.ALL)
	private User user;
	
	public UserDetail() {
		
	}

	public UserDetail(String description, String country, String county, String city, byte[] profileImage) {
		this.description = description;
		this.country = country;
		this.county = county;
		this.city = city;
		this.profileImage = profileImage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		return "UserDetail [id=" + id + ", description=" + description + ", country=" + country + ", county=" + county
				+ ", city=" + city + ", profileImage=" + Arrays.toString(profileImage) + "]";
	}

}

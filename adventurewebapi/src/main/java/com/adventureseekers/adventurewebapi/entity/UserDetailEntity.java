package com.adventureseekers.adventurewebapi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Entity
@Table(name="user_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "description")
	@Size(min = 1, max = 300, message = "is required")
	@With
	private String description;
	
	@Column(name = "country")
	@Size(min = 1, max = 50, message = "is required")
	@With
	private String country;
	
	@Column(name = "county")
	@Size(min = 1, max = 50, message = "is required")
	@With
	private String county;
	
	@Column(name = "city")
	@Size(min = 1, max = 50, message = "is required")
	@With
	private String city;
	
	@Lob
    @Column(name = "profile_image", columnDefinition = "mediumblob")
	private Byte[] profileImage;
	
	@OneToOne(mappedBy="userDetail", cascade = CascadeType.ALL)
	private UserEntity user;
	
	public UserDetailEntity(String description, String country, String county, String city, Byte[] profileImage) {
		this.description = description.trim();
		this.country = country.trim();
		this.county = county.trim();
		this.city = city.trim();
		this.profileImage = profileImage;
	}
	
	public void setDescription(String description) {
		this.description = description.trim();
	}
	
	public void setCity(String city) {
		this.city = city.trim();
	}
	
	public void setCountry(String country) {
		this.country = country.trim();
	}
	
	public void setCounty(String county) {
		this.county = county.trim();
	}


}

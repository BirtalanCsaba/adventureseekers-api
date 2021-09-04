package com.adventureseekers.adventurewebapi.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(name = "username")
	@NotNull(message = "is required")
	@Size(min = 1, max = 50, message = "is required")
	@Pattern(regexp="[^ ]*", message = "cannot contain space")
	private String userName;
	
	@Column(name = "password")
	@NotNull(message = "is required")
	@Size(min = 5, message = "too short")
	@Size(max = 60, message = "too long")
	private String password;
	
	@Column(name = "email")
	@NotNull(message = "is required")
	@Email(message = "Email should be valid")
	@Size(min = 1, max = 50, message = "is required")
	private String email;
	
	@Column(name = "first_name")
	@NotNull(message = "is required")
	@Size(min = 1, max = 50, message = "is required")
	private String firstName;
	
	@Column(name = "last_name")
	@NotNull(message = "is required")
	@Size(min = 1, max = 50, message = "is required")
	private String lastName;
	
	@Column(name = "birth_date")
	@Temporal(TemporalType.DATE)
	@NotNull(message="must not be empty")
	private Date birthDate;
	
	@Column(name = "enabled")
	private boolean enabled = false;
	
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, 
				cascade = CascadeType.ALL)
	private List<ConfirmationToken> confirmationTokens;
	
	@ManyToMany(fetch = FetchType.LAZY, 
			cascade = {
					CascadeType.DETACH,
					CascadeType.MERGE,
					CascadeType.PERSIST,
					CascadeType.REFRESH
			})
	@JoinTable(name = "users_roles", 
			joinColumns = @JoinColumn(name = "user_id"), 
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;
	
	public User() {
		
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.trim();
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public List<ConfirmationToken> getConfirmationTokens() {
		return confirmationTokens;
	}

	public void setConfirmationTokens(List<ConfirmationToken> confirmationTokens) {
		this.confirmationTokens = confirmationTokens;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}













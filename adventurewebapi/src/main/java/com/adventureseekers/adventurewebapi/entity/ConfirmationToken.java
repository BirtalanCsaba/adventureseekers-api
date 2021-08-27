package com.adventureseekers.adventurewebapi.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {
	
	@Id
	@GeneratedValue(strategy  = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "confirmed_at")
	private LocalDateTime confirmedAt;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "expired_at")
	private LocalDateTime expiredAt;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
						CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "user_id")
	private User user;
	
	public ConfirmationToken() {
		
	}

	public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, User user) {
		this.token = token;
		this.createdAt = createdAt;
		this.expiredAt = expiredAt;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getConfirmedAt() {
		return confirmedAt;
	}

	public void setConfirmedAt(LocalDateTime confirmedAt) {
		this.confirmedAt = confirmedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ConfirmationToken [id=" + id + ", token=" + token + ", confirmedAt=" + confirmedAt + ", createdAt="
				+ createdAt + ", expiredAt=" + expiredAt + "]";
	}
	
}










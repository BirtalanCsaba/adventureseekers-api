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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "confirmation_token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationTokenEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@JsonIgnore
	private UserEntity user;

	public ConfirmationTokenEntity(String token, LocalDateTime createdAt, LocalDateTime expiredAt, UserEntity user) {
		this.token = token;
		this.createdAt = createdAt;
		this.expiredAt = expiredAt;
		this.user = user;
	}

	@Override
	public String toString() {
		return "ConfirmationToken [id=" + id + ", token=" + token + ", confirmedAt=" + confirmedAt + ", createdAt="
				+ createdAt + ", expiredAt=" + expiredAt + "]";
	}
	
}










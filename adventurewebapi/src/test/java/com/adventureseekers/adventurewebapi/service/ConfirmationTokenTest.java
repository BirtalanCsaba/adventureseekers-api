package com.adventureseekers.adventurewebapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.adventureseekers.adventurewebapi.dao.ConfirmationTokenDAO;
import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.entity.ConfirmationTokenEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.TokenNotFoundException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenTest {
	
	@Mock ConfirmationTokenDAO confirmationTokenDAO;
	
	@Mock UserDAO userDAO;
	
	@Autowired
	@InjectMocks
	private ConfirmationTokenServiceImpl underTest;
	
	private ConfirmationTokenEntity confirmationTokenEntity;

	@BeforeEach
	public void setUp() {
		this.confirmationTokenEntity = new ConfirmationTokenEntity(
				UUID.randomUUID().toString(),
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(10),
				null);
		this.confirmationTokenEntity.setUser(new UserEntity());
	}
	
	@AfterEach
	public void tearDown() {
		this.confirmationTokenEntity = null;
	}
	
	@Test
	public void canSaveConfirmationToken() {
		// when
		this.underTest.saveConfirmationToken(this.confirmationTokenEntity);
		
		// then
		ArgumentCaptor<ConfirmationTokenEntity> confirmationTokenArgumentCaptor = 
				ArgumentCaptor.forClass(ConfirmationTokenEntity.class);
		
		verify(this.confirmationTokenDAO).save(confirmationTokenArgumentCaptor.capture());
		
		ConfirmationTokenEntity capturedConfirmationToken = 
				confirmationTokenArgumentCaptor.getValue();
		
		assertThat(capturedConfirmationToken).isEqualTo(this.confirmationTokenEntity);
	}
	
	@Test
	public void canConfirmUser() {
		when(this.confirmationTokenDAO.findByToken(this.confirmationTokenEntity.getToken()))
			.thenReturn(Optional.of(this.confirmationTokenEntity));
		
		// when
		this.underTest.confirmUser(this.confirmationTokenEntity.getToken());
		
		// then
		// test saving the confirmation token
		ArgumentCaptor<ConfirmationTokenEntity> confirmationTokenArgumentCaptor = 
				ArgumentCaptor.forClass(ConfirmationTokenEntity.class);
		
		verify(this.confirmationTokenDAO).save(confirmationTokenArgumentCaptor.capture());
		
		ConfirmationTokenEntity capturedConfirmationToken =
				confirmationTokenArgumentCaptor.getValue();
		
		assertThat(capturedConfirmationToken)
			.usingRecursiveComparison()
			.ignoringFields("confirmedAt")
			.isEqualTo(this.confirmationTokenEntity);
		
		// test saving the user with enabled
		this.confirmationTokenEntity.getUser().setEnabled(true);
		
		ArgumentCaptor<UserEntity> userArgumentCaptor =
				ArgumentCaptor.forClass(UserEntity.class);
		
		verify(this.userDAO).save(userArgumentCaptor.capture());
		
		UserEntity capturedUser = userArgumentCaptor.getValue();
		
		assertThat(capturedUser).isEqualTo(this.confirmationTokenEntity.getUser());
	}
	
	@Test
	public void confirmUserWillThrowWhenTokenNotFound() {
		when(this.confirmationTokenDAO.findByToken(this.confirmationTokenEntity.getToken()))
			.thenReturn(Optional.empty());
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.confirmUser(this.confirmationTokenEntity.getToken()))
			.isInstanceOf(TokenNotFoundException.class)
			.hasMessageContaining("Token not found - " + this.confirmationTokenEntity.getToken());
	}
	
	@Test
	public void confirmUserWillThrowWhenTokenIsAlreadyConfirmed() {
		when(this.confirmationTokenDAO.findByToken(this.confirmationTokenEntity.getToken()))
			.thenReturn(Optional.of(this.confirmationTokenEntity));
		
		this.confirmationTokenEntity.setConfirmedAt(LocalDateTime.now());
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.confirmUser(this.confirmationTokenEntity.getToken()))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("Account already confirmed");
	}
	
	@Test
	public void confirmUserWillThrowWhenTokenIsExpired() {
		when(this.confirmationTokenDAO.findByToken(this.confirmationTokenEntity.getToken()))
			.thenReturn(Optional.of(this.confirmationTokenEntity));
		
		this.confirmationTokenEntity.setExpiredAt(
				this.confirmationTokenEntity.getExpiredAt().minusMinutes(50));
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.confirmUser(this.confirmationTokenEntity.getToken()))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("Token expired");
	}
	
	@Test
	public void canFindByToken() {
		// when
		this.underTest.findByToken(this.confirmationTokenEntity.getToken());
		
		// then
		ArgumentCaptor<String> tokenArgumentCaptor =
				ArgumentCaptor.forClass(String.class);
		
		verify(this.confirmationTokenDAO).findByToken(tokenArgumentCaptor.capture());
		
		String capturedToken = tokenArgumentCaptor.getValue();
		
		assertThat(capturedToken).isEqualTo(this.confirmationTokenEntity.getToken());
	}
	
	@Test
	public void canResetToken() {
		String email = "user@test.com";
		
		when(this.userDAO.findByEmail(email))
			.thenReturn(Optional.of(this.confirmationTokenEntity.getUser()));
		
		List<ConfirmationTokenEntity> tokens = new ArrayList<ConfirmationTokenEntity>();
		tokens.add(this.confirmationTokenEntity);
		
		this.confirmationTokenEntity.getUser()
			.setConfirmationTokens(tokens);
		this.confirmationTokenEntity.setExpiredAt(LocalDateTime.now().minusMinutes(50));
		
		// when
		this.underTest.resetToken(email);
		
		//then
		ArgumentCaptor<ConfirmationTokenEntity> confirmationTokenArgumentCaptor =
				ArgumentCaptor.forClass(ConfirmationTokenEntity.class);
		
		verify(this.confirmationTokenDAO).save(confirmationTokenArgumentCaptor.capture());
		
		ConfirmationTokenEntity capturedConfirmationToken = confirmationTokenArgumentCaptor.getValue();
		
		assertThat(capturedConfirmationToken).isEqualTo(this.confirmationTokenEntity);
	}
	
	@Test
	public void resetTokenWillThrowWhenTokenNotExpired() {
		String email = "user@test.com";
		this.confirmationTokenEntity.setExpiredAt(LocalDateTime.now().plusMinutes(10));
		List<ConfirmationTokenEntity> tokens = new ArrayList<ConfirmationTokenEntity>();
		tokens.add(this.confirmationTokenEntity);
		
		this.confirmationTokenEntity.getUser()
			.setConfirmationTokens(tokens);
		
		when(this.userDAO.findByEmail(email))
			.thenReturn(Optional.of(this.confirmationTokenEntity.getUser()));
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.resetToken(email))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("Token is not expired");
	}
	
	@Test
	public void resetTokenWillThrowWhenEmailInvalid() {
		String email = "user@test.com";
		when(this.userDAO.findByEmail(email))
			.thenReturn(Optional.empty());
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.resetToken(email))
			.isInstanceOf(UserNotFoundException.class)
			.hasMessageContaining("User not found - " + email);
	}
	
}









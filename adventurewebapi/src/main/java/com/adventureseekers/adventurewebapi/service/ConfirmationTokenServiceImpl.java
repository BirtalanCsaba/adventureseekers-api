package com.adventureseekers.adventurewebapi.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adventureseekers.adventurewebapi.dao.ConfirmationTokenDAO;
import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.entity.ConfirmationTokenEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.TokenNotFoundException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
	
	@Autowired
	private ConfirmationTokenDAO confirmationTokenDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public void saveConfirmationToken(ConfirmationTokenEntity token) {
		this.confirmationTokenDAO.save(token);
	}
	
	/**
	 * Activates the user account by the given token
	 */
	@Override
	public void confirmUser(String token) throws IllegalStateException, TokenNotFoundException {
		ConfirmationTokenEntity theToken = 
				this.confirmationTokenDAO.findByToken(token)
				.orElseThrow(() -> new TokenNotFoundException(token));
		// check if the account is already confirmed
		if (theToken.getConfirmedAt() != null) {
			throw new IllegalStateException("Account already confirmed");
		}
		
		// check if the token is not expired 
		LocalDateTime expires_at = theToken.getExpiredAt();
		if (expires_at.isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Token expired");
		}
		
		// set the confirmation date time
		theToken.setConfirmedAt(LocalDateTime.now());
		this.confirmationTokenDAO.save(theToken);
		
		// enable the user account
		UserEntity theUser = theToken.getUser();
		theUser.setEnabled(true);
		this.userDAO.save(theUser);
	}

	@Override
	public Optional<ConfirmationTokenEntity> findByToken(String token) {
		return this.confirmationTokenDAO.findByToken(token);
	}

	@Override
	public void resetToken(String email) throws IllegalStateException, TokenNotFoundException  {
		UserEntity currentUser = 
				this.userDAO.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(email));
		
		ConfirmationTokenEntity token = currentUser.getConfirmationTokens().get(0);
		if (token.getExpiredAt().isAfter(LocalDateTime.now())) {
			throw new IllegalStateException("Token is not expired");
		}
		token.setCreatedAt(LocalDateTime.now());
		token.setExpiredAt(LocalDateTime.now().plusMinutes(15));
		token.setToken(UUID.randomUUID().toString());
		this.confirmationTokenDAO.save(token);
		
	}
}











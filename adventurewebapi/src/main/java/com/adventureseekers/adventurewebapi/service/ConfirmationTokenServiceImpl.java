package com.adventureseekers.adventurewebapi.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adventureseekers.adventurewebapi.dao.ConfirmationTokenDAO;
import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.entity.ConfirmationToken;
import com.adventureseekers.adventurewebapi.entity.User;
import com.adventureseekers.adventurewebapi.exception.TokenNotFoundException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
	
	@Autowired
	private ConfirmationTokenDAO confirmationTokenDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public void saveConfirmationToken(ConfirmationToken token) {
		this.confirmationTokenDAO.save(token);
	}
	
	/**
	 * Activates the user account by the given token
	 */
	@Override
	public void confirmUser(String token) throws IllegalStateException {
		ConfirmationToken theToken = 
				this.confirmationTokenDAO.findByToken(token)
				.orElseThrow(() -> new IllegalStateException("Token not found"));
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
		User theUser = theToken.getUser();
		theUser.setEnabled(true);
		this.userDAO.save(theUser);
	}

	@Override
	public Optional<ConfirmationToken> findByToken(String token) {
		return this.confirmationTokenDAO.findByToken(token);
	}

	@Override
	public void resetToken(String email) throws IllegalStateException, TokenNotFoundException  {
		Optional<User> currentUser = this.userDAO.findByEmail(email);
		if (currentUser.isPresent()) {
			User userObj = currentUser.get();
			ConfirmationToken token = userObj.getConfirmationTokens().get(0);
			if (token.getExpiredAt().isAfter(LocalDateTime.now())) {
				throw new IllegalStateException("Token is not expired");
			}
			token.setCreatedAt(LocalDateTime.now());
			token.setExpiredAt(LocalDateTime.now().plusMinutes(15));
			token.setToken(UUID.randomUUID().toString());
			this.confirmationTokenDAO.save(token);
		}
		else {
			throw new UserNotFoundException("User not found");
		}
		
	}
}











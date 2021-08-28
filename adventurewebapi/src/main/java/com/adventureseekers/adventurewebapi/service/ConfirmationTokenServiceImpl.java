package com.adventureseekers.adventurewebapi.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adventureseekers.adventurewebapi.dao.ConfirmationTokenDAO;
import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.entity.ConfirmationToken;
import com.adventureseekers.adventurewebapi.entity.User;

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

	@Override
	public void confirmUser(String token) {
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
}











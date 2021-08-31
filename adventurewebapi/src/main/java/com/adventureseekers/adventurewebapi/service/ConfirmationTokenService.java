package com.adventureseekers.adventurewebapi.service;

import java.util.Optional;

import com.adventureseekers.adventurewebapi.entity.ConfirmationToken;
import com.adventureseekers.adventurewebapi.exception.TokenNotFoundException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

public interface ConfirmationTokenService {
	
	public void saveConfirmationToken(ConfirmationToken token);
	
	/**
	 * Confirms the token and enables the user account
	 * @param token The token to be confirmed
	 */
	public void confirmUser(String token);
	
	public Optional<ConfirmationToken> findByToken(String token);
	
	/**
	 * Resets the token`s created and expired time of the user with the given email
	 * @param email The email of the user
	 */
	public void resetToken(String email) throws UserNotFoundException, TokenNotFoundException;
}

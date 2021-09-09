package com.adventureseekers.adventurewebapi.service;

import java.util.Optional;

import com.adventureseekers.adventurewebapi.entity.ConfirmationTokenEntity;
import com.adventureseekers.adventurewebapi.exception.TokenNotFoundException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

public interface ConfirmationTokenService {
	
	public void saveConfirmationToken(ConfirmationTokenEntity token);
	
	/**
	 * Confirms the token and enables the user account
	 * @param token The token to be confirmed
	 */
	public void confirmUser(String token);
	
	public Optional<ConfirmationTokenEntity> findByToken(String token);
	
	/**
	 * Resets the token`s created and expired time of the user with the given email
	 * @param email The email of the user
	 */
	public void resetToken(String email) throws UserNotFoundException, TokenNotFoundException;
}

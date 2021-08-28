package com.adventureseekers.adventurewebapi.service;

import java.util.Optional;

import com.adventureseekers.adventurewebapi.entity.ConfirmationToken;

public interface ConfirmationTokenService {
	
	public void saveConfirmationToken(ConfirmationToken token);
	
	/**
	 * Confirms the token and enables the user account
	 * @param token The token to be confirmed
	 */
	public void confirmUser(String token);
	
	public Optional<ConfirmationToken> findByToken(String token);
}

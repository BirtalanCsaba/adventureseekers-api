package com.adventureseekers.adventurewebapi.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adventureseekers.adventurewebapi.entity.ConfirmationToken;

@Repository
public interface ConfirmationTokenDAO 
	extends JpaRepository<ConfirmationToken, Integer> {
	
	/**
	 * Gets a ConfirmationToken object by the token
	 * @param token The token of the ConfirmationToken object
	 * @return The ConfirmationToken object
	 */
	Optional<ConfirmationToken> findByToken(String token);
}

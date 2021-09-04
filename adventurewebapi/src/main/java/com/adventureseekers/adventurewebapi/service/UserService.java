package com.adventureseekers.adventurewebapi.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.adventureseekers.adventurewebapi.entity.User;

public interface UserService extends UserDetailsService {
	
	/**
	 * Finds a use by the user name
	 * @param userName The user name of the user
	 * @return The use with the given user name
	 */
	Optional<User> findByUserName(String userName);
	
	/**
	 * Finds a use by the email
	 * @param email The email of the user
	 * @return The use with the given email
	 */
	Optional<User> findByEmail(String email);
	
	/**
	 * Check if there is a user with the given email
	 * @param email The email to be checked
	 * @return True if a user exists with the email, otherwise false
	 */
	Boolean existsByEmail(String email);
	
	/**
	 * Check if there is a user with the given user name
	 * @param username The user name of the user
	 * @return True if a user exists with the given user name, otherwise false
	 */
	Boolean existsByUsername(String username);
	
	/**
	 * Saves the given user to the database.
	 * @param adventureUser The user to be saved
	 * @exception If the user already exists. Email and user name must be unique.
	 */
	void save(User newUser);
}

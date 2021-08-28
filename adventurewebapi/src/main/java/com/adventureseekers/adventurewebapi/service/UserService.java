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
	 * Saves the given user to the database.
	 * @param adventureUser The user to be saved
	 * @exception If the user already exists. Email and user name must be unique.
	 */
	void save(User newUser);
}

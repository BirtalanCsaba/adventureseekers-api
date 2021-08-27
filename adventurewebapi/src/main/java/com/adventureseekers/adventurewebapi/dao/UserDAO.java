package com.adventureseekers.adventurewebapi.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adventureseekers.adventurewebapi.entity.User;

public interface UserDAO
		extends JpaRepository<User, UUID> {
	
	/**
	 * Gets a user by its user name
	 * @param userName The user name of the user
	 * @return The user object with the given user name
	 */
	@Query("SELECT u FROM User u WHERE u.userName =:uName")
	public Optional<User> findByUsername(@Param("uName") String userName);
}

package com.adventureseekers.adventurewebapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adventureseekers.adventurewebapi.entity.User;
import com.adventureseekers.adventurewebapi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/**
	 * Registers a new user
	 * @param user The user to be registered
	 */
	@PostMapping("/register")
	private void register(@RequestBody User user) {
		
		// TODO: Check for duplicate users
		
		// encrypt the password
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		
		// register the user
		this.userService.save(user);
	}
}

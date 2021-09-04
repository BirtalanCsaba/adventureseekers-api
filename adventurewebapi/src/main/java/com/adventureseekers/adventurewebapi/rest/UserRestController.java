package com.adventureseekers.adventurewebapi.rest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adventureseekers.adventurewebapi.entity.ConfirmationToken;
import com.adventureseekers.adventurewebapi.entity.User;
import com.adventureseekers.adventurewebapi.helpers.EmailHelper;
import com.adventureseekers.adventurewebapi.service.ConfirmationTokenService;
import com.adventureseekers.adventurewebapi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	
	private Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private ConfirmationTokenService confirmationTokenService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailHelper emailHelper;
	
	/**
	 * Registers a new user
	 * @param user The user to be registered
	 */
	@PostMapping("/register")
	private StringResponse register(@Valid @RequestBody User user) {
		// register the user
		this.userService.save(user);
		
		// create a confirmation token
		
		// retrieve the new user
        User theUser = this.userService.findByUserName(user.getUserName()).get();
        
        // create and save a confirmation token
        String token = UUID.randomUUID().toString();
        
        ConfirmationToken confirmationToken = new ConfirmationToken(
	        		token,
	        		LocalDateTime.now(),
	        		LocalDateTime.now().plusMinutes(30),
	        		theUser
        		);
        
		this.confirmationTokenService.saveConfirmationToken(confirmationToken);
		
		this.emailHelper.sendVerificationEmail(theUser, token);
		
		return new StringResponse("Account created successfuly");
	}
	
	/**
	 * User token confirmation for account activation
	 */
	@GetMapping("/confirmation")
	public StringResponse confirmation(@RequestParam("token") String token) {
		// confirm the user`s token
		this.confirmationTokenService.confirmUser(token);
		return new StringResponse("Account confirmed");
	}
	
	/**
	 * Resends the email with the verification token
	 * @return
	 */
	@GetMapping("/resend")
	public StringResponse resend(@RequestParam("email") String email) {
		try {
			this.confirmationTokenService.resetToken(email);
			this.emailHelper.sendVerificationEmail(email);
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			throw new IllegalStateException("Cannot send the token");
		}
		
		
		return new StringResponse("Verification email sent");
	}
	
	@GetMapping("/checkEmail")
	public Map<String, Boolean> checkEmailExists(@RequestParam("email") String email) {
		Boolean emailFound = this.userService.existsByEmail(email);
		return Collections.singletonMap("success", emailFound);
	}
	
	@GetMapping("/checkUsername")
	public Map<String, Boolean> checkUsernameExists(@RequestParam("username") String username) {
		Boolean usernameFound = this.userService.existsByUsername(username);
		return Collections.singletonMap("success", usernameFound);
	}
}










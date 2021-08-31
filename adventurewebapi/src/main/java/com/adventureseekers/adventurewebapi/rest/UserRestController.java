package com.adventureseekers.adventurewebapi.rest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.adventureseekers.adventurewebapi.entity.ConfirmationToken;
import com.adventureseekers.adventurewebapi.entity.User;
import com.adventureseekers.adventurewebapi.service.ConfirmationTokenService;
import com.adventureseekers.adventurewebapi.service.EmailService;
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
	private SpringTemplateEngine thymeleafTemplateEngine;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
    private ServletContext servletContext;
	
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
	        		LocalDateTime.now().plusMinutes(15),
	        		theUser
        		);
        
		this.confirmationTokenService.saveConfirmationToken(confirmationToken);
		
		this.sendVerificationEmail(theUser, token);
		
		return new StringResponse("Account created successfuly");
	}
	
	/**
	 * User token confirmation for account activation
	 */
	@PostMapping("/confirmation/{token}")
	public StringResponse confirmation(@PathVariable String token) {
		// confirm the user`s token
		this.confirmationTokenService.confirmUser(token);
		return new StringResponse("Account confirmed");
	}
	
	/**
	 * Resends the email with the verification token
	 * @return
	 */
	@PostMapping("/resend/{email}")
	public StringResponse resend(@PathVariable String email) {
		try {
			this.confirmationTokenService.resetToken(email);
			this.sendVerificationEmail(email);
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			throw new IllegalStateException("Cannot send the token");
		}
		
		
		return new StringResponse("Verification email sent");
	}
	
	private void sendVerificationEmail(User theUser, String token) {
		// send the confirmation token via email
		try {
			this.emailService.send(
					theUser.getEmail(), 
					this.createConfirmationEmail(
							theUser.getFirstName() + " " + theUser.getLastName(), token),
					"Confirm your email");
		} catch (MessagingException e) {
			this.logger.error(e.toString());
		}
	}
	
	private void sendVerificationEmail(String email) {
		Optional<User> theUser = this.userService.findByEmail(email);
		String token = theUser.get().getConfirmationTokens().get(0).getToken();
		// send the confirmation token via email
		try {
			this.emailService.send(
					theUser.get().getEmail(), 
					this.createConfirmationEmail(
							theUser.get().getFirstName() + " " + theUser.get().getLastName(), token),
					"Confirm your email");
		} catch (MessagingException e) {
			this.logger.error(e.toString());
		}
	}
	
	/**
	 * Creates an email confirmation HTML page
	 * @param name The name of the user
	 * @param token The confirmation token
	 * @return The HTML body as a string
	 */
	private String createConfirmationEmail(String name, String token) throws MessagingException {
		Context thymeleafContext = new Context();
		thymeleafContext.setVariable("name", name);
		thymeleafContext.setVariable("confirmationLink", 
				"http://localhost:4200" + "/confirm/" + token );
		String htmlBody = 
				thymeleafTemplateEngine.process("email/email-confirmation.html", thymeleafContext);
		return htmlBody;
	}
}

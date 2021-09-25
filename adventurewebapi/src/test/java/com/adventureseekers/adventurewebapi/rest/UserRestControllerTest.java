package com.adventureseekers.adventurewebapi.rest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserRestController userRestController;
	
	private UserEntity userEntity;
	
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setup(){
		this.userEntity = new UserEntity(
	    		"user.test", 
	    		"test123", 
	    		"user@test.com", 
	    		"firstName", 
	    		"lastName", 
	    		new Date(), 
	    		false, 
	    		new UserDetailEntity(), 
	    		null);
	   mockMvc = MockMvcBuilders.standaloneSetup(this.userRestController).build();
	}
	
	@AfterEach
	void tearDown() {
		this.userService.delete(this.userEntity.getUserName());
		this.userEntity = null;
	}
	
	@Test
	public void getUserDetailsByUsernameTest() throws Exception {
		// check that there is no user with the given user name
		boolean response = false;
		when(this.userService.existsByUsername(this.userEntity.getUserName()))
		.thenReturn(response);
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/api/users/checkUsername")
				.param("username", this.userEntity.getUserName()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print());
		assertFalse(response);
		
		this.userService.save(this.userEntity);
		
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/api/users/checkUsername")
				.param("username", this.userEntity.getUserName()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print());
		assertTrue(response);
	}
}




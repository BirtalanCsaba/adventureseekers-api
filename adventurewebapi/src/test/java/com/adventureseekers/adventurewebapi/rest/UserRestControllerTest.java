package com.adventureseekers.adventurewebapi.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.UUID;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.adventureseekers.adventurewebapi.dto.UserModel;
import com.adventureseekers.adventurewebapi.service.UserService;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserRestController userRestController;
	
	private UserModel userModel;
	
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setup(){
	   this.userModel = new UserModel(
			   UUID.randomUUID(),
			   "user.test",
			   "test123",
			   "test@test.com",
			   "firstName",
			   "lastName",
			   new Date(),
			   true);
	   mockMvc = MockMvcBuilders.standaloneSetup(this.userRestController).build();
	}
	
	@AfterEach
	void tearDown() {
	   this.userModel = null;
	}
	
	@Test
	public void getUserDetailsByUsernameTest() throws Exception {
		// check that there is no user with the given user name
		boolean response = false;
		when(this.userService.existsByUsername(this.userModel.getUserName()))
		.thenReturn(response);
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/api/users/checkUsername")
				.param("username", this.userModel.getUserName()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print());
		assertEquals(false, response);
	}
}




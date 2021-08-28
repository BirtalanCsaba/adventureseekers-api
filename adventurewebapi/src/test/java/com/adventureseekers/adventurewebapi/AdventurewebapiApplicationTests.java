package com.adventureseekers.adventurewebapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import com.adventureseekers.adventurewebapi.rest.UserRestController;

@SpringBootTest
class AdventurewebapiApplicationTests {
	
	@Autowired
	private UserRestController controller;
	
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}

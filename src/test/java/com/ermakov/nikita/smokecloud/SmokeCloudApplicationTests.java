package com.ermakov.nikita.smokecloud;

import com.ermakov.nikita.smokecloud.controller.MainController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = MainController.class)
@ExtendWith(SpringExtension.class)
class SmokeCloudApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithUserDetails("user")
	void authenticatedMainControllerRequest_ReturnsOk() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/welcome"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void notAuthenticatedMainControllerRequest_ReturnsNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/welcome"))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
}

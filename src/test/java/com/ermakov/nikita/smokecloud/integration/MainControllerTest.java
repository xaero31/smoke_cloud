package com.ermakov.nikita.smokecloud.integration;

import com.ermakov.nikita.smokecloud.controller.MainController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MainController.class)
@ExtendWith(SpringExtension.class)
class MainControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithUserDetails("user")
	void welcomePageTest() throws Exception {
		mockMvc.perform(get("/welcome"))
				.andExpect(status().isOk());
	}

	@Test
	void notAuthenticatedMainControllerRequest_RedirectToLogin() throws Exception {
		mockMvc.perform(get("/welcome"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
}

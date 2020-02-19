package com.ermakov.nikita.security;

import com.ermakov.nikita.smokecloud.SmokeCloudApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SmokeCloudApplication.class)
@AutoConfigureMockMvc
class SecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockUser
	void welcomePageTest() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

	@Test
	void notAuthenticatedMainControllerRequest_RedirectToLogin() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("http://localhost/login"));
	}
}
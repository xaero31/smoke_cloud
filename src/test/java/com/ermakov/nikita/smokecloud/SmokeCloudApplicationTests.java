package com.ermakov.nikita.smokecloud;

import com.ermakov.nikita.smokecloud.controller.MainController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.ModelAndView;

@WebMvcTest(controllers = MainController.class)
@ExtendWith(SpringExtension.class)
class SmokeCloudApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Value("${secret.property}")
	private String secret;

	@Test
	@WithUserDetails("user")
	void authenticatedMainControllerRequest_ReturnsOk() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/welcome"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("secret", secret));
	}

	@Test
	void notAuthenticatedMainControllerRequest_ReturnsNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/welcome"))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@Test
	@Disabled
	void stubTest() {
		MainController controller = new MainController();
		Assertions.assertEquals(controller.getWelcomePage(new ExtendedModelMap()), "index");
	}
}

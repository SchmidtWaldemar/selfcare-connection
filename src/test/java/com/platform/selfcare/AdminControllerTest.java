package com.platform.selfcare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.service.IUserService;
import com.platform.selfcare.service.UserService;

import jakarta.servlet.http.HttpSession;

@WithMockUser(username = "admin@your-domain.net", roles = { "USER", "ADMIN" })
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {
	
	final static String testMail = "test@user.com";
	final static String testPassword = "Test1234...";
	
	private MockMvc mvc;
	private final User user = new User(testMail, testPassword);

	@Autowired
	WebApplicationContext context;
	
    @BeforeEach
    public void setUp() {
    	this.user.setEnabled(false);
        this.mvc = MockMvcBuilders
        		.webAppContextSetup(context)
        		.apply(springSecurity())
            .build();
    }

    @Test
	public void testStartpage() throws Exception {
    	HttpSession session = this.mvc.perform(get("/admin"))
    			.andExpect(status().is2xxSuccessful())
    			.andReturn()
    			.getRequest()
    			.getSession();
    	
    	Thread.sleep(1000);
    	
    	this.mvc.perform(MockMvcRequestBuilders.post("/enableStatusUser")
    			.session((MockHttpSession) session)
    			.content(asJsonString(user))
    			.param("enableStatus", "true")
    			.with(csrf())
    			.accept(MediaType.APPLICATION_JSON))
    				.andExpect(view().name("admin"))
    				.andExpect(status().is2xxSuccessful());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

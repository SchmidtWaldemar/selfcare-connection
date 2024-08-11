package com.platform.selfcare;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.selfcare.dto.RegisterUserDto;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.repository.UserRepository;
import com.platform.selfcare.service.ISendMailService;
import com.platform.selfcare.service.IUserService;
import com.platform.selfcare.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
	
	final static String testMail = "test@user.com";
	final static String testPassword = "Test1234...";
	
	@TestConfiguration
	static class UserServiceImplTestContextConfiguration {
		@Bean
		IUserService userServiceTest() {
			return new UserService();
		}
	}
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private IUserService userServiceTest;
	
	@MockBean
	private ISendMailService sendMailService;
	
	@MockBean
	private UserRepository userRepository;
	
	private final RegisterUserDto userDto = new RegisterUserDto();
	private final User user = new User(testMail, testPassword);
	
	@BeforeEach
	public void setUp() throws Exception {
		this.userDto.setEmail(testMail);
		this.userDto.setPassword(testPassword);
		this.userDto.setMatchingPassword(testPassword);
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}
	
	@Test
	public void whenRegisterUser_thenCreateNewUser() throws Exception {
		given(this.userServiceTest.registerNewUser(this.userDto)).willReturn(this.user);
		Mockito.verify(this.userRepository, VerificationModeFactory.times(1)).findByEmail(testMail);
		Mockito.reset(this.userRepository);
	}
	
	@Test
	public void whenPostRestRegister_thenCreateNewUser() throws Exception {
		given(this.userServiceTest.registerNewUser(this.userDto)).willReturn(this.user);
		this.mvc.perform(post("/register/new").contentType(MediaType.APPLICATION_JSON).content(toJson(this.userDto)))
		//.andExpect(jsonPath("$.newAccount", is("success")));
		.andReturn();
	}
	
	private static byte[] toJson(Object obj) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsBytes(obj);
	}
}

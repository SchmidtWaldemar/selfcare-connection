package com.platform.selfcare;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.platform.selfcare.dto.GroupDto;
import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.repository.GroupRepository;
import com.platform.selfcare.service.CustomUserDetails;
import com.platform.selfcare.service.GroupService;
import com.platform.selfcare.service.IGroupService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@user.com", roles = { "USER", "ADMIN" })
public class GroupControllerTest {
	
	final static String testMail = "test@user.com";
	final static String testPassword = "changeit";
	
	private final User user = new User(testMail, testPassword);
	
	@Autowired
	private WebApplicationContext context;
	
	@TestConfiguration
	static class GroupServiceImplTestContextConfiguration {
		@Bean
		IGroupService groupServiceTest() {
			return new GroupService();
		}
	}
	
	@InjectMocks
	MockHttpSession session;
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	GroupRepository groupRepository;
	
	GroupDto newGroup;
	Group group;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.group = new Group();
		this.group.setCreator(this.user);
		this.group.setTitle("test");
		this.group.setDescription("test");
		
		CustomUserDetails userDetails = new CustomUserDetails(user);
		this.session.setAttribute("user", userDetails);
		
		this.newGroup = new GroupDto();
		this.newGroup.setTitle("test");
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}
	
	@Test
	public void whenCallRESTgroup_thenReturnSuccessfulStatus() throws Exception {
		this.mvc.perform(get("/group/")
			.session(this.session))
			.andExpect(status().is2xxSuccessful())
		.andReturn();
	}
	
	@Test
	public void whenCallRESTnewGroup_thenReturnDto() throws Exception {
		this.mvc.perform(get("/group/new")
			.session(this.session))
			.andExpect(status().is2xxSuccessful())
		.andReturn();
	}
	
	@Test
	public void whenCreatenewGroup_thenReturnSuccessfulMessage() throws Exception {
		this.mvc.perform(MockMvcRequestBuilders.post("/group/createNew")
				.session((MockHttpSession) session)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
    			.param("title", "test")
    			.with(csrf()))
    				.andExpect(view().name("newGroup"))
    				//.andExpect(model().attribute("message", "Gruppe erfolgreich angelegt!"))
    				.andExpect(status().is2xxSuccessful())
    			.andReturn();
	}
}

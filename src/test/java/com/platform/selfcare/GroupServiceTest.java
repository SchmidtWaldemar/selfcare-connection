package com.platform.selfcare;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import com.platform.selfcare.entity.Blacklist;
import com.platform.selfcare.entity.Candidate;
import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.repository.BlacklistRepository;
import com.platform.selfcare.repository.CandidateRepository;
import com.platform.selfcare.repository.GroupRepository;
import com.platform.selfcare.service.GroupService;
import com.platform.selfcare.service.IGroupService;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupServiceTest {
	
	final static String testMail = "test@user.com";
	final static String testPassword = "Test1234...";

	final String title = "Testgruppe";
	final String description = "Testgruppe Beschreibung";
	
	final User creator = new User(testMail, testPassword);
	
	@TestConfiguration
	static class GroupServiceImplTestContextConfiguration {
		@Bean
		IGroupService mockitoGroupService() {
			return new GroupService();
		}
	}
	
	@Autowired
	private IGroupService mockitoGroupService;
	
	@MockBean
	GroupRepository groupRepository;
	
	@MockBean
	CandidateRepository candidateRepository;
	
	@MockBean
	BlacklistRepository blacklistRepository;
	
	Group group;
	Candidate candidate;
	Blacklist blacklist;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.group = new Group();
		this.group.setCreator(this.creator);
		this.group.setTitle(this.title);
		this.group.setDescription(this.description);
		
		this.candidate = new Candidate();
		this.candidate.setUser(creator);
		this.candidate.setGroup(group);
		this.candidate.setId(1L);
		
		this.blacklist = new Blacklist();
		this.blacklist.setGroup(group);
		this.blacklist.setUser(creator);
		
		given(blacklistRepository.findAll()).willReturn(asList(this.blacklist));
	}
	
	@Test
	public void whenCreateNewGroup_thenGetGroupObject() throws Exception {
		given(mockitoGroupService.createNewGroup(this.creator, this.title, this.description)).willReturn(this.group);
		Mockito.when(groupRepository.findByCreator(this.creator)).thenReturn(asList(this.group));
		Mockito.reset(this.groupRepository);
	}
	
	@Test
	public void whenCreateCandidate_thenReturnTrue() throws Exception {
		this.group.setActive(true);
		given(mockitoGroupService.createCandidate(this.candidate)).willReturn(true);
		Mockito.when(candidateRepository.findCandidatures(this.creator)).thenReturn(asList(this.candidate));
		Mockito.reset(this.candidateRepository);
	}
	
	@Test
	public void whenChangeCandidateFalseStatus_thenSetToBlacklistAndReturnTrue() throws Exception {
		this.creator.setEnabled(true);
		given(mockitoGroupService.changeCandidateStatus(this.candidate, false)).willReturn(true);
		Mockito.when(blacklistRepository.findAll()).thenReturn(asList(this.blacklist));
		Mockito.reset(this.blacklistRepository);
	}
}

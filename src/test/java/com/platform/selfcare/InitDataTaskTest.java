package com.platform.selfcare;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.platform.selfcare.entity.Role;
import com.platform.selfcare.enums.RoleType;
import com.platform.selfcare.repository.RoleRepository;

@SpringBootTest
public class InitDataTaskTest {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@BeforeEach
    void setUp() {
		Role role = new Role(RoleType.USER);
		//roleRepository.save(role);
    }

    @AfterEach
    void tearDown() {
    	//roleRepository.deleteAll();
    }
}

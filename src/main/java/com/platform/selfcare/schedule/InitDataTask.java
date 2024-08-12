package com.platform.selfcare.schedule;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.platform.selfcare.entity.Role;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.enums.RoleType;
import com.platform.selfcare.repository.RoleRepository;
import com.platform.selfcare.repository.UserRepository;
import com.platform.selfcare.service.ConfigService;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

/**
 * create required init data for the start application
 */
@Component
public class InitDataTask {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ConfigService configService;
	
	@PostConstruct
	public void onApplicationEvent() {
		createRoleIfNotExists(RoleType.USER);
		createRoleIfNotExists(RoleType.ADMIN);
		
		createAdminUserIfNotExists();
	}

	@Transactional
	private void createRoleIfNotExists(RoleType type) {
		Optional<Role> role = this.roleRepository.findByName(type.getName());
		if (!role.isPresent()) {
			this.roleRepository.save(new Role(type));
		}
	}
	
	@Transactional
	private void createAdminUserIfNotExists() {
		Optional<User> admin = this.userRepository.findByEmail(configService.getAdminEmail());
		if (!admin.isPresent()) {
			User adminUser = new User(configService.getAdminEmail(), getEncryptedPassword(configService.getAdminPassword()));
			adminUser.setFirstName("Super");
			adminUser.setLastName("Admin");
			
			Optional<Role> uRole = this.roleRepository.findByName(RoleType.USER.getName());
			Optional<Role> aRole = this.roleRepository.findByName(RoleType.ADMIN.getName());
			if (uRole.isPresent() && aRole.isPresent()) {
				Set<Role> roles = new HashSet<Role>();
				roles.add(uRole.get());
				roles.add(aRole.get());
				adminUser.setRoles(roles);
				// change default password with password forget or update password after login for admin!
				// admin users should not be disabled
				adminUser.setEnabled(true);
				this.userRepository.save(adminUser);
			}
			else {
				System.err.println("admin role could not be found");
			}
		}
	}
	
	private String getEncryptedPassword(final String clearPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return "{BCRYPT}" + encoder.encode(configService.getAdminPassword());
	}
}

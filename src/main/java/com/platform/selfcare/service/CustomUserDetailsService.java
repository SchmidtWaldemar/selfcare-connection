package com.platform.selfcare.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.platform.selfcare.entity.User;
import com.platform.selfcare.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = this.userRepository.findByEmail(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("User not found");
		}
		
		return new CustomUserDetails(user.get());
	}

}

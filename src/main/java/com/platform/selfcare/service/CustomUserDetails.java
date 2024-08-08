package com.platform.selfcare.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.platform.selfcare.entity.User;
import com.platform.selfcare.enums.RoleType;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = -9029685781627374028L;

	private User user;
	
	private boolean enabled;
		
	public CustomUserDetails(User user) {
		this.user = user;
		this.enabled = this.user.isEnabled();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.user.getAuthorities();
	}

	@Override
	public String getPassword() {
		return this.user.getPassword().replaceFirst("\\{BCRYPT\\}", "");
	}

	@Override
	public String getUsername() {
		return this.getUser().getEmail();
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return this.user.isEnabled();
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isAdmin() {
		return this.user.hasRole(RoleType.ADMIN.getName());	
	}
}

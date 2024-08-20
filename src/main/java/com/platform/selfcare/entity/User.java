package com.platform.selfcare.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.platform.selfcare.validation.ValidEmail;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User implements Serializable {
	
	private static final long serialVersionUID = 6844055507692576899L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ValidEmail
	@Column(nullable = false, unique = true, length = 55)
	private String email;
	
	@Column(nullable = false, length = 70)
	private String password;
	
	@Column(name="firstName", nullable = true, length = 20)
	private String firstName;
	
	@Column(name="lastName", nullable = true, length = 20)
	private String lastName;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "member_groups", 
    	joinColumns = 
    		@JoinColumn(name = "user_id"), 
    	inverseJoinColumns = 
    		@JoinColumn(name = "group_id")
    )	
	private Set<Group> groups = new HashSet<>();
	
	@Column(name = "enabled", nullable = false, columnDefinition = "boolean default false")
	private boolean enabled;

	User() {}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean hasRole(String role) {
		if (this.roles == null) {
			return false;
		}
		else {
			String roleCheck = role.startsWith("ROLE_") ? role : "ROLE_" + role;
			return this.roles.stream().anyMatch(r -> r.getName().equals(roleCheck));
		}
	}

	/**
	 * fetched all authorities by role of user
	 * 
	 * @return list of collection
	 */
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object item) {
		if (this == item) {
			return true;
		}
		if (item == null) {
			return false;
		}
		if (getClass() != item.getClass()) {
			return false;
		}
		final User user = (User) item;
		if (!this.email.equals(user.getEmail())) {
			return false;
		}
		return true;
	}
}

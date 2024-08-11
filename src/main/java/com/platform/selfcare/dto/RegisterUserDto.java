package com.platform.selfcare.dto;

import com.platform.selfcare.validation.PasswordMatches;
import com.platform.selfcare.validation.ValidEmail;
import com.platform.selfcare.validation.ValidPassword;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@PasswordMatches(matchingPassword = "matchingPassword", password = "password")
public class RegisterUserDto {

	@ValidPassword
	private String password;
	
	private String matchingPassword;
	
	private String firstName;
	
	private String lastName;
	
	@ValidEmail
	@NotNull
	@Size(min = 5, message = "E-Mail Länge kann nicht stimmen")
	private String email;
	
	public RegisterUserDto() {}

	public String getPassword() {
		return this.password;
	}

	public String getMatchingPassword() {
		return this.matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
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
}

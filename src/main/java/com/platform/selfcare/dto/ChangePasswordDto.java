package com.platform.selfcare.dto;

import com.platform.selfcare.validation.PasswordMatches;
import com.platform.selfcare.validation.ValidPassword;

@PasswordMatches(password = "password", matchingPassword = "matchingPassword")
public class ChangePasswordDto {

	private String oldPassword;
	
	@ValidPassword
	private String password;
	
	private String matchingPassword;
	
	private String token;
	
	public ChangePasswordDto() {}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return this.password;
	}

	public String getMatchingPassword() {
		return this.matchingPassword;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}

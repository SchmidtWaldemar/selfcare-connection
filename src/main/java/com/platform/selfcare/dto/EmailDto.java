package com.platform.selfcare.dto;

import com.platform.selfcare.validation.ValidEmail;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EmailDto {

	@ValidEmail
	@NotNull
	@Size(min = 5, message = "E-Mail Länge kann nicht stimmen")
	private String email;
	
	public EmailDto() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

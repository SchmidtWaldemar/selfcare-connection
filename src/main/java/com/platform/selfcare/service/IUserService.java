package com.platform.selfcare.service;

import java.util.Locale;

import com.platform.selfcare.dto.RegisterUserDto;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.entity.VerificationToken;
import com.platform.selfcare.enums.TokenType;

/**
 * implemented in @UserService
 */
public interface IUserService {
	/**
	 * registration trigger of new account
	 * 
	 * @param userDto user form data
	 * 
	 * @return registered and persisted new entity
	 */
	User registerNewUser(RegisterUserDto userDto);

	/**
	 * sent mail to verificate email
	 * 
	 * @param appUrl url to redirect
	 * @param locale locale to fetch country
	 * @param user user entity to sent mail
	 */
	void sendConfirmationMailByUser(String appUrl, Locale locale, User user);

	/**
	 * check if token is valid
	 * 
	 * @param token token to validate
	 * @param type type to validate
	 * 
	 * @return valid token
	 */
	VerificationToken validateVerificationToken(String token, TokenType type);
}

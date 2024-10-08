package com.platform.selfcare.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.platform.selfcare.dto.RegisterUserDto;
import com.platform.selfcare.entity.Candidate;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.entity.VerificationToken;
import com.platform.selfcare.enums.RoleType;
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
	 * sent mail to verify registration password forgotten request by email
	 * 
	 * @param appUrl url to redirect
	 * @param locale locale to fetch country
	 * @param user user entity to sent mail
	 */
	void sendConfirmationMailByUserAndType(String appUrl, Locale locale, User user, TokenType type);
	
	/**
	 * check if token is valid
	 * 
	 * @param token token to validate
	 * @param type type to validate
	 * @param devaluate remove token
	 * 
	 * @return valid token
	 */
	VerificationToken validateVerificationToken(String token, TokenType type, boolean devaluate);

	/**
	 * fetch persisted user by email
	 * 
	 * @param email searched email
	 * 
	 * @return existing or not
	 */
	Optional<User> findUserByEmail(String email);
	
	/**
	 * change password after password forgotten or after login
	 * 
	 * @param user to change password
	 * @param newPassword new password data from user
	 */
	void changePassword(User user, final String newPassword);

	/**
	 * check password match with that from user
	 * 
	 * @param user actual user entity
	 * @param oldPassword password to match
	 * @return status
	 */
	boolean checkPasswordMatched(User user, String oldPassword);

	/**
	 * fetch users last token if exists
	 * 
	 * @param user users entity
	 * @param token type of token
	 * 
	 * @return token if exists
	 */
	Optional<VerificationToken> findTokenByUser(User user, TokenType type);
	
	/**
	 * fetch all users by role
	 * 
	 * @param role role to fetch
	 * 
	 * @return list of result
	 */
	List<User> findAllUserByRole(RoleType role);

	/**
	 * enable user and set user role if required
	 * 
	 * @param user user to enable
	 * @param enableStatus status to change
	 */
	void setUserEnableStatus(User user, boolean enableStatus);
	
	/**
	 * fetch all candidate entries
	 * 
	 * @param user reference of candidatures
	 * 
	 * @return list of candidate entities
	 */
	List<Candidate> getCandidatures(User user);
}

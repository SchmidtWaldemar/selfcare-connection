package com.platform.selfcare.service;

import java.io.IOException;
import java.util.Locale;

import com.platform.selfcare.entity.User;

import jakarta.mail.MessagingException;


/**
 * implemented within @SendMailService
 */
public interface ISendMailService {

	/**
	 * sent mail after registration for 2F authentication 
	 * @param appUrl your-domain.net
	 * @param locale locale for mail handling
	 * @param user user to sent message
	 * @param token token to confirm
	 * @return status
	 * @throws MessagingException
	 * @throws IOException
	 */
	boolean sendRegistrationEmail(final String appUrl, final Locale locale, final User user, final String token) throws MessagingException, IOException;
	
	/**
	 * 
	 * sent mail by request of password change by 2F verification 
	 * 
	 * @param appUrl your-domain.net
	 * @param locale locale for mail handling
	 * @param user user to sent message
	 * @param token token to confirm
	 * @return status
	 * @throws MessagingException
	 * @throws IOException
	 */
	boolean sendPasswordForgotten(final String appUrl, final Locale locale, final User user, final String token) throws MessagingException, IOException;

	/**
	 * send status of candidates to creator of group
	 * 
	 * @param user to sent message
	 * @param candidateCount info to candidates 
	 * @return mail sending status
	 * @throws MessagingException
	 * @throws IOException
	 */
	boolean sendCandidateStatus(final User user, final int candidateCount) throws MessagingException, IOException;
}

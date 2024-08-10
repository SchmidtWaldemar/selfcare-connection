package com.platform.selfcare.service;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.platform.selfcare.config.ConfigProperties;

@Service
@EnableConfigurationProperties(ConfigProperties.class)
public class ConfigService {
	
	private final ConfigProperties configProperties;
	
	/**
	 * inject self made properties
	 *  
	 * @param configProperties injected property class
	 */
	public ConfigService(ConfigProperties configProperties) {
		this.configProperties = configProperties;
	}
	
	/**
	 * change credential data in application.properties
	 * 
	 * @return admin email
	 */
	public String getAdminEmail() {
		return this.configProperties.getAdminEmail();
	}

	/**
	 * change the password within application.properties
	 * 
	 * @return clear text password
	 */
	public String getAdminPassword() {
		return this.configProperties.getAdminPassword();
	}
	
	/**
	 * credentials of mail account for sending mails
	 * 
	 * @return username
	 */
	public String getMailUsername() {
		return this.configProperties.getMailUsername();
	}
	
	/**
	 * credentials of mail account for sending mails
	 * 
	 * @return password in cleartext
	 */
	public String getMailPassword() {
		return this.configProperties.getMailPassword();
	}
	
	/**
	 * smtp host address of mail account
	 * 
	 * @return hostname
	 */
	public String getMailSmtpHost() {
		return this.configProperties.getMailSmtpHost();
	}
	
	/**
	 * smtp port of mail account -> use secure like 587 
	 * 
	 * @return port as int
	 */
	public Integer getMailSmtpPort() {
		return this.configProperties.getMailSmtpPort();
	}
	
	/**
	 * no reply mail after sending mail
	 * 
	 * @return support mail
	 */
	public String getSupportMail() {
		return this.configProperties.getSupportMail();
	}
	
	/**
	 * status of mail sending
	 * 
	 * @return status if mail should be sent or not
	 */
	public Boolean getMailSenderActive() {
		return this.configProperties.getMailSenderActive();
	}
}

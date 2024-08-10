package com.platform.selfcare.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * used from application.properties
 */
@ConfigurationProperties(prefix = "config.service")
@ConfigurationPropertiesScan
public class ConfigProperties {
	
	private String adminEmail;
	
	private String adminPassword;
	
	private String supportMail;
	
	private String mailUsername;
	
	private String mailPassword;
	
	private String mailSmtpHost;
	
	private Integer mailSmtpPort;
	
	private Boolean mailSenderActive;

	public String getAdminEmail() {
		return adminEmail;
	}
	
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getSupportMail() {
		return supportMail;
	}

	public void setSupportMail(String supportMail) {
		this.supportMail = supportMail;
	}

	public String getMailUsername() {
		return mailUsername;
	}

	public void setMailUsername(String mailUsername) {
		this.mailUsername = mailUsername;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public String getMailSmtpHost() {
		return mailSmtpHost;
	}

	public void setMailSmtpHost(String mailSmtpHost) {
		this.mailSmtpHost = mailSmtpHost;
	}

	public Integer getMailSmtpPort() {
		return mailSmtpPort;
	}

	public void setMailSmtpPort(Integer mailSmtpPort) {
		this.mailSmtpPort = mailSmtpPort;
	}

	public Boolean getMailSenderActive() {
		return mailSenderActive;
	}

	public void setMailSenderActive(Boolean mailSenderActive) {
		this.mailSenderActive = mailSenderActive;
	}
}

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
}

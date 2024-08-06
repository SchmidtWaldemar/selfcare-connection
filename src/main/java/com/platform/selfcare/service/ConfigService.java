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
}

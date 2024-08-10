package com.platform.selfcare.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.platform.selfcare.service.ConfigService;

@Configuration
public class AppConfig {
	
	@Autowired
	ConfigService configService;
	
	@Bean
	JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(this.configService.getMailSmtpHost());
		mailSender.setPort(this.configService.getMailSmtpPort());
		mailSender.setUsername(this.configService.getMailUsername());
		mailSender.setPassword(this.configService.getMailPassword());
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtps.timeout", "8000");
		props.put("mail.smtp.ssl.trust", "*");
		
		return mailSender;
	}
}

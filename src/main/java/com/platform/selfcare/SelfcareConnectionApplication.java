package com.platform.selfcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.platform.selfcare.config.ConfigProperties;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.platform.selfcare.repository"})
@EnableConfigurationProperties(ConfigProperties.class)
public class SelfcareConnectionApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SelfcareConnectionApplication.class, args);
	}
}

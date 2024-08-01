package com.platform.selfcare.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.platform.selfcare.*")
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	/**
	 * set location for public accessible contents like CSS  
	 */
	@Override
	public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    	 registry.addResourceHandler("/**")
         	.addResourceLocations("classpath:/resources/", "classpath:/static/");
	}

	/**
	 * view controller registries
	 */
	public void addViewControllers(@NonNull ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/home");
		registry.addViewController("/home").setViewName("home");
	}
}
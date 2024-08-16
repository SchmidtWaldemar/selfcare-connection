package com.platform.selfcare.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/logout").setViewName("logout");
		registry.addViewController("/admin").setViewName("admin");
		registry.addViewController("/overview").setViewName("overview");
		registry.addViewController("/register/").setViewName("register");
		registry.addViewController("/error").setViewName("error");
		
		registry.addViewController("/saveUpdatedPassword").setViewName("saveUpdatedPassword");
		registry.addViewController("/forgottenPassword").setViewName("forgottenPassword");
		registry.addViewController("/changeForgottenPassword").setViewName("changeForgottenPassword");
		
		registry.addViewController("/group/").setViewName("group");
		registry.addViewController("/group/new").setViewName("newGroup");
		registry.addViewController("/group/createNew").setViewName("newGroup");
		registry.addViewController("/group/register").setViewName("registerByGroup");
	}
	
	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("GET", "POST");
	}
}
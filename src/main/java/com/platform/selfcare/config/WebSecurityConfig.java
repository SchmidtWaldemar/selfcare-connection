package com.platform.selfcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	/**
	 * set MVC content filter for specific namespaces
	 * 
	 * @param http configure secure requests
	 * @param introspector mapping inspector
	 * @return filter
	 * @throws Exception thrown non specific exception 
	 */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http.authorizeHttpRequests(authManagerReqMatcherRegistry -> authManagerReqMatcherRegistry.requestMatchers(mvcMatcherBuilder.pattern("/**")).permitAll());
        return http.build();
    }
}

package com.platform.selfcare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import com.platform.selfcare.filter.CsrfCookieFilter;
import com.platform.selfcare.security.AuthFailureHandler;
import com.platform.selfcare.security.AuthSuccessHandler;
import com.platform.selfcare.security.CustomLogoutSuccessHandler;
import com.platform.selfcare.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
	private CustomLogoutSuccessHandler logoutSuccessHandler;
	
	@Autowired
	private AuthSuccessHandler authSuccessHandler;
	
	@Autowired
	private AuthFailureHandler authFailureHandler;
	
	/**
	 * set MVC content filter for specific namespaces
	 * 
	 * @param http configure secure requests
	 * @return filter
	 * @throws Exception thrown non specific exception 
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authHttpRequest) -> 
				authHttpRequest
					.requestMatchers("/admin", "/enableStatusUser").hasRole("ADMIN")
					.requestMatchers("/overview", "/saveUpdatedPassword", "/group/*").hasRole("USER")
					.requestMatchers("/**").permitAll()
			)
			.csrf(csrf -> 
				csrf
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
					.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
			)
			.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
			.formLogin(login ->
				login
					.loginPage("/login")
					.failureUrl("/login?error=true")
					.usernameParameter("email")
					.defaultSuccessUrl("/", true)
					.successHandler(authSuccessHandler)
					.failureHandler(authFailureHandler)
					.permitAll()
			)
			.logout(logout ->
				logout
					.logoutUrl("/logout")
					.logoutSuccessHandler(logoutSuccessHandler)
					.invalidateHttpSession(false)
					.deleteCookies("JSESSIONID")
					.permitAll()
			);
		return http.build();
	}
    
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    	authProvider.setUserDetailsService(userDetailsService());
    	authProvider.setPasswordEncoder(passwordEncoder());
    	return authProvider;
    }

    @Bean
	UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
    
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}

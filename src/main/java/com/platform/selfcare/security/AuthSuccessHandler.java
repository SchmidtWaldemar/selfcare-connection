package com.platform.selfcare.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.platform.selfcare.service.CustomUserDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("authSuccessHandler")
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
			Authentication authentication) throws IOException, ServletException {
		
		setDefaultTargetUrl("/");
		
		super.onAuthenticationSuccess(request, response, authentication);
		
		try {
			CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
			if (principal != null) {
				request.getSession().setAttribute("user", principal);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}

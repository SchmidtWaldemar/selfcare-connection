package com.platform.selfcare.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("authenticationFailureHandler")
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		setDefaultFailureUrl("/login?error=true");
		
		super.onAuthenticationFailure(request, response, exception);
		
		request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, "error");
	}
}

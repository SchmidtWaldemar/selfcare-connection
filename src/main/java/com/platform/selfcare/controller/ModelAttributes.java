package com.platform.selfcare.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.platform.selfcare.service.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

public abstract class ModelAttributes {
	/**
	 * use for checking details status after login
	 * 
	 * @param request session set
	 * 
	 * @return userDetails
	 */
	@ModelAttribute("me")
	CustomUserDetails getAuthenticationStatus(HttpServletRequest request) {
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		if (userDetails != null) {
			return userDetails;
		}
		return null;
	}
	
	/**
	 * to check actual page name
	 * 
	 * @param request for check
	 * 
	 * @return page name as string
	 */
	@ModelAttribute("requestURI")
	String getRequestServletPath(HttpServletRequest request) {
		return request.getServletPath();
	}
}

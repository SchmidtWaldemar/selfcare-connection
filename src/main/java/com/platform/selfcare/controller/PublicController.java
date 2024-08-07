package com.platform.selfcare.controller;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.platform.selfcare.service.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PublicController {
	
	/**
	 * main root page  
	 * 
	 * @param name optional parameter for public name view on page
	 * @param model model set for attributes and more
	 * 
	 * @return mapped page to view
	 */
	@GetMapping("/home")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="Anonym") String name, 
			Model model, HttpServletRequest request) {
		
		try {
			Principal principal = request.getUserPrincipal();
			if (principal != null && principal instanceof UsernamePasswordAuthenticationToken) {
				UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
				CustomUserDetails userDetails = (CustomUserDetails) token.getPrincipal();
				model.addAttribute("me", userDetails);
			}
		} catch (Exception e) {
			System.err.println("failed to cast principal");
		}
		
		model.addAttribute("name", name);
		
		return "home";
	}
}

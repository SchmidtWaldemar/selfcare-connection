package com.platform.selfcare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("logout")
	public String logout(final HttpServletRequest request) {
		return "logout";
	}
}

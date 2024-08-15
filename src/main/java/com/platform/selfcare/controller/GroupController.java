package com.platform.selfcare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GroupController {
	
	@GetMapping("/group")
	public String group(Model model, HttpServletRequest request) {
		
		return "group";
	}
}

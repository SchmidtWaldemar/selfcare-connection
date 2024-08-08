package com.platform.selfcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.platform.selfcare.entity.User;
import com.platform.selfcare.repository.UserRepository;
import com.platform.selfcare.service.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/admin")
	public String admin(Model model, final HttpServletRequest request) {
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		if (userDetails != null && userDetails.isAdmin()) {
			// show all users out from DB
			List<User> listUser = this.userRepository.findAll();
			model.addAttribute("users", listUser);
			
			return "admin";
		}
		
		return null;
	}
}

package com.platform.selfcare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.platform.selfcare.entity.User;
import com.platform.selfcare.enums.RoleType;
import com.platform.selfcare.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

	@Autowired
	IUserService userService;
	
	@GetMapping("/admin")
	public String admin(Model model, final HttpServletRequest request) {
		// show all users by role out from DB
		List<User> users = this.userService.findAllUserByRole(RoleType.USER);
		model.addAttribute("users", users);
		List<User> admins = this.userService.findAllUserByRole(RoleType.ADMIN);
		model.addAttribute("admins", admins);
		
		return "admin";
	}
	
	@PostMapping("/enableStatusUser")
	public String enableStatusUser(Model model, HttpServletRequest request, User user, boolean enableStatus) {
		Optional<User> uCheck = userService.findUserByEmail(user.getEmail());
		
		if (uCheck.isPresent() 
				&& !uCheck.get().hasRole(RoleType.ADMIN.getName())) {
			this.userService.setUserEnableStatus(user, enableStatus);
		}
		
		return admin(model, request);
	}
}

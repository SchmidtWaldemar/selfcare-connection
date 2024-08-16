package com.platform.selfcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.platform.selfcare.entity.Group;
import com.platform.selfcare.service.CustomUserDetails;
import com.platform.selfcare.service.IGroupService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OverviewController {

	@Autowired
	private IGroupService groupService;
	
	@GetMapping("/overview")
	public String overview(Model model, HttpServletRequest request) {
		
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		if (userDetails != null) {
			List<Group> groups = this.groupService.getAll();
			groups.stream().forEach(g -> g.setAuthorized(g.isAuthorized(userDetails.getUser())));
			model.addAttribute("groups", groups);
		}
		
		return "overview";
	}
}

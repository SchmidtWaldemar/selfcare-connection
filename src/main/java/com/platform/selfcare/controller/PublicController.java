package com.platform.selfcare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicController extends ModelAttributes {
	
	/**
	 * main root page  
	 * 
	 * @param name optional parameter for public name view on page
	 * @param model model set for attributes and more
	 * 
	 * @return mapped page to view
	 */
	@GetMapping("/home")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="Anonym") String name, Model model) {
		model.addAttribute("name", name);
		return "home";
	}
	
	@GetMapping("/impress")
	public String impress(Model model) {
		return "impress";
	}
	
	@GetMapping("/privacy")
	public String privacy(Model model) {
		return "privacy";
	}
}

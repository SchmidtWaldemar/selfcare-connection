package com.platform.selfcare.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestController {
	
@GetMapping("/ping")
	String ping() {
		return "pong";
	}
}

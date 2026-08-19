package com.nt.__welcome_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MyController {

	@GetMapping("/wish")
	public String wish() {
		return "Good MOring!!!";
	}
}

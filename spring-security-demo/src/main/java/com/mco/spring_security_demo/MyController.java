package com.mco.spring_security_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/get")
    public String getWish() {
	return "Hello";
    }
}

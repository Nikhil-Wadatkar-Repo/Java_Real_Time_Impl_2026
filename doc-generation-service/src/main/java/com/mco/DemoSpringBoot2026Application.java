package com.mco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableCaching
@EnableTransactionManagement
public class DemoSpringBoot2026Application {
	

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBoot2026Application.class, args);
	}

}

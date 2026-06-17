package com.mco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class EmployeeManagementService {
	

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementService.class, args);
	}

}

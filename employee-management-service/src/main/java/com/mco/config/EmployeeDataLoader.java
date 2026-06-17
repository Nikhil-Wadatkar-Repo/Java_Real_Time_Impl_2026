package com.mco.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mco.entity.Employee;
import com.mco.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EmployeeDataLoader {

    private final EmployeeRepository employeeRepository;

    @Bean
    public CommandLineRunner seedEmployeeData() {
        System.out.println("seedEmployeeData method called =================");
        return args -> {
            if (employeeRepository.count() == 0) {
                employeeRepository.save(Employee.builder()
                        .firstName("Rahul")
                        .lastName("Mehta")
                        .email("rahul.mehta1@example.com")
                        .phoneNumber(9760797269L)
                        .hireDate(LocalDate.parse("2018-11-26"))
                        .jobId(105)
                        .salary(new BigDecimal("153744"))
                        .localStatus("ACTIVE")
                        .managerId(25)
                        .departmentId(15)
                        .build());
                System.out.println("One record inserted");
            }else{
                System.out.println("Already records are there");
            }
        };
    }
}

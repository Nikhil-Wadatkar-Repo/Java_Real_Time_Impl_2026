package com.mco.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employeeId;

    @Column
    private String address;

    @Column
    private BigDecimal bonus;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private LocalDate dateOfBirth;

//    @Column
//    private String department;

    @Column
    private String designation;

//    @NotBlank(message = "firstName is required")
    @Column(nullable = false)
    private String firstName;

    @Column
    private String gender;

    @NotBlank(message = "lastName is required")
    @Column(nullable = false)
    private String lastName;

    @Column
    private LocalDate lastWorkingDate;

    @Column
    private String managerName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String employmentType;

    @NotNull(message = "phoneNumber is required")
    @Positive(message = "phoneNumber must be positive")
    @Column(nullable = false)
    private Long phoneNumber;

    @NotNull(message = "hireDate is required")
    @Column(nullable = false)
    private LocalDate hireDate;

    @NotNull(message = "jobId is required")
    @Column(nullable = false)
    private Integer jobId;

    @NotNull(message = "salary is required")
    @Positive(message = "salary must be positive")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal salary;

    @Column
    private String state;

    @Column
    private String status;

    @NotNull(message = "managerId is required")
    @Column(nullable = false)
    private Integer managerId;

    @NotNull(message = "departmentId is required")
    @Column(nullable = false)
    private Integer departmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_FK_id", insertable = false, updatable = false)
    private Department department;
}

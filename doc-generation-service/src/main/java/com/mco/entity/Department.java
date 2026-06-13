package com.mco.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue
    @Column(name = "department_id")
    private Long id;

    private String departmentName;
    @OneToMany(mappedBy = "department")
    private Collection<Employee> employee;

    public Collection<Employee> getEmployee() {
        return employee;
    }

    public void setEmployee(Collection<Employee> employee) {
        this.employee = employee;
    }
}

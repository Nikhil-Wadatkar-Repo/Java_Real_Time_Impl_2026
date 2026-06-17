package com.mco;

import com.mco.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
public class Utility {
    @Autowired
    private Executor taskExecutor;

    public Employee processEmployee(Employee employee) {
        updateFirstName(employee);
        updateManager(employee);
        updateLocalStatus(employee);
        return employee;
    }

    private void updateFirstName(Employee employee) {
        System.out.println("Updated name for EmpId: "+employee.getEmployeeId());
        if ("MALE".equalsIgnoreCase(
                employee.getGender())) {

            employee.setFirstName(
                    "Mr. " + employee.getFirstName());

        } else if ("FEMALE".equalsIgnoreCase(
                employee.getGender())) {

            employee.setFirstName(
                    "Mrs. " + employee.getFirstName());
        }
    }

    private void updateManager(Employee employee) {
        System.out.println("Updated manager info for EmpId: "+employee.getEmployeeId());
        if ("INTERN".equalsIgnoreCase(
                employee.getEmploymentType())) {

            employee.setManagerName(
                    "Mr. Ankur");
        }
    }

    private void updateLocalStatus(Employee employee) {
        System.out.println("Updated local status for EmpId: "+employee.getEmployeeId());
        if ("Maharashtra".equalsIgnoreCase(
                employee.getState())) {

            employee.setLocalStatus(
                    "MH");
        }
    }

}

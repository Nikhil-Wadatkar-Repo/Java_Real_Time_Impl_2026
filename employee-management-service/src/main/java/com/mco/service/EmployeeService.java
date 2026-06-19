package com.mco.service;

import java.util.List;
import com.mco.entity.Employee;
import com.mco.exception.DuplicateResourceException;
import com.mco.exception.ResourceNotFoundException;
import com.mco.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Cacheable(cacheNames = "employees", key = "#employeeId")
    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));
    }

    @Transactional
    @CacheEvict(cacheNames = "employees", key = "#employeeId")
    public Employee update(Long employeeId, Employee employee) {
        Employee existingEmployee = findById(employeeId);

        if (!existingEmployee.getEmail().equals(employee.getEmail()) && employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateResourceException("Employee already exists with email " + employee.getEmail());
        }

        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhoneNumber(employee.getPhoneNumber());
        existingEmployee.setHireDate(employee.getHireDate());
        existingEmployee.setJobId(employee.getJobId());
        existingEmployee.setSalary(employee.getSalary());
        existingEmployee.setManagerId(employee.getManagerId());
        existingEmployee.setDepartmentId(employee.getDepartmentId());

        return employeeRepository.save(existingEmployee);
    }

    @Transactional
    @CacheEvict(cacheNames = "employees", key = "#employeeId")
    public void delete(Long employeeId) {
        Employee existingEmployee = findById(employeeId);
        employeeRepository.delete(existingEmployee);
    }
}

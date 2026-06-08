package com.mco.service;

import java.util.ArrayList;
import java.util.List;

import com.mco.dto.BulkEmployeeSaveResponse;
import com.mco.dto.EmployeeDepartmentView;
import com.mco.entity.Employee;
import com.mco.exception.DuplicateResourceException;
import com.mco.exception.ResourceNotFoundException;
import com.mco.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public Employee create(Employee employee) {
        if (employeeRepository.existsById(employee.getEmployeeId())) {
            throw new DuplicateResourceException("Employee already exists with id " + employee.getEmployeeId());
        }
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateResourceException("Employee already exists with email " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    public BulkEmployeeSaveResponse createBulk(List<Employee> employees) {
        List<Employee> savedEmployees = new ArrayList<>();
        List<String> skippedReasons = new ArrayList<>();

        for (Employee employee : employees) {
            if (employeeRepository.existsById(employee.getEmployeeId())) {
                skippedReasons.add("Skipped employeeId " + employee.getEmployeeId() + " because it already exists");
                continue;
            }
            if (employeeRepository.existsByEmail(employee.getEmail())) {
                skippedReasons.add("Skipped email " + employee.getEmail() + " because it already exists");
                continue;
            }
            savedEmployees.add(employeeRepository.save(employee));
        }

        return BulkEmployeeSaveResponse.builder()
                .requestedCount(employees.size())
                .savedCount(savedEmployees.size())
                .skippedCount(skippedReasons.size())
                .savedEmployees(savedEmployees)
                .skippedReasons(skippedReasons)
                .build();
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<EmployeeDepartmentView> findAllWithDepartmentNameNPlusOneDemo() {
        List<Employee> employees = employeeRepository.findAll();
        log.info("Loaded {} employees. Accessing lazy department relation inside the loop to demonstrate N+1.", employees.size());
        List<EmployeeDepartmentView> views = new ArrayList<>();

        for (Employee employee : employees) {
            // Intentionally triggers lazy loading per employee to demonstrate the N+1 problem.
            String departmentName = employee.getDepartment() == null ? null : employee.getDepartment().getDepartmentName();
            log.info("Employee {} -> department '{}'", employee.getEmployeeId(), departmentName);
            views.add(EmployeeDepartmentView.builder()
                    .employeeId(employee.getEmployeeId())
                    .employeeName(employee.getFirstName() + " " + employee.getLastName())
                    .departmentName(departmentName)
                    .build());
        }

        log.info("N+1 demo completed for {} employees.", views.size());
        return views;
    }

    @Transactional(readOnly = true)
    public List<EmployeeDepartmentView> findAllWithDepartmentNameJoinFetch() {
        List<Employee> employees = employeeRepository.findAllWithDepartmentJoinFetch();
        log.info("Loaded {} employees using JOIN FETCH. Department relation is initialized in one query.", employees.size());

        List<EmployeeDepartmentView> views = new ArrayList<>();
        for (Employee employee : employees) {
            String departmentName = employee.getDepartment() == null ? null : employee.getDepartment().getDepartmentName();
            log.info("JOIN FETCH employee {} -> department '{}'", employee.getEmployeeId(), departmentName);
            views.add(EmployeeDepartmentView.builder()
                    .employeeId(employee.getEmployeeId())
                    .employeeName(employee.getFirstName() + " " + employee.getLastName())
                    .departmentName(departmentName)
                    .build());
        }

        log.info("JOIN FETCH demo completed for {} employees.", views.size());
        return views;
    }

    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));
    }

    @Transactional
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
    public void delete(Long employeeId) {
        Employee existingEmployee = findById(employeeId);
        employeeRepository.delete(existingEmployee);
    }
}

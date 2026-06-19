package com.mco.service;

import com.mco.Utility;
import com.mco.dto.EmployeeDepartmentView;
import com.mco.entity.Employee;
import com.mco.exception.DuplicateResourceException;
import com.mco.exception.ResourceNotFoundException;
import com.mco.repo.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionService {

    private final EmployeeRepository employeeRepository;

    public Page<Employee> findPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "employeeId"));
        return employeeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDepartmentView> findAllWithDepartmentNameNPlusOneDemo() {
        List<Employee> employees = employeeRepository.findAll();
        log.info("Loaded {} employees. Accessing lazy department relation inside the loop to demonstrate N+1.", employees.size());
        List<EmployeeDepartmentView> views = new ArrayList<>();

        for (Employee employee : employees) {
            // Intentionally triggers lazy loading per employee to demonstrate the N+1
            // problem.
            String departmentName = employee.getDepartment() == null ? null : employee.getDepartment().getDepartmentName();
            log.info("Employee {} -> department '{}'", employee.getEmployeeId(), departmentName);
            views.add(EmployeeDepartmentView.builder().employeeId(employee.getEmployeeId()).employeeName(employee.getFirstName() + " " + employee.getLastName()).departmentName(departmentName).build());
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
            views.add(EmployeeDepartmentView.builder().employeeId(employee.getEmployeeId()).employeeName(employee.getFirstName() + " " + employee.getLastName()).departmentName(departmentName).build());
        }

        log.info("JOIN FETCH demo completed for {} employees.", views.size());
        return views;
    }

    @Cacheable(cacheNames = "employees", key = "#employeeId")
    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));
    }

    @Autowired
    public Utility processor;
    @Autowired
    private Executor taskExecutor;

    public String processAllEmployees() {
        if (true) throw new RuntimeException("Fekla");
        long startTime = System.currentTimeMillis();
        Pageable pageable = PageRequest.of(0, 1000);
        Page<Employee> page;
        page = employeeRepository.findAll(pageable);
        List<Employee> employees = page.getContent();
        List<CompletableFuture<Employee>> futures = employees.stream().map(employee -> CompletableFuture.supplyAsync(() -> processor.processEmployee(employee), taskExecutor)).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        List<Employee> updatedEmployees = futures.stream().map(CompletableFuture::join).toList();
        String message = "";
        try {
            employeeRepository.saveAll(updatedEmployees);
            message = "All employees are saved and updated.";
        } catch (Exception e) {
//            throw new RuntimeException(e);
            message = "Errorrrrr";
        }

        long totalTimeMs = System.currentTimeMillis() - startTime;
        log.info("Processed {} employees in {} ms.", employees.size(), totalTimeMs);
        return message;
    }
}

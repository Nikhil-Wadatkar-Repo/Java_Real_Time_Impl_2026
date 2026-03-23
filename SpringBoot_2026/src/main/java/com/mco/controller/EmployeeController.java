package com.mco.controller;

import com.mco.entity.EmployeeDetails;
import com.mco.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeDetails createEmployee(@RequestBody EmployeeDetails employeeDetails) {
        return employeeService.createEmployee(employeeDetails);
    }

    @GetMapping
    public List<EmployeeDetails> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeDetails getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public EmployeeDetails updateEmployee(@PathVariable Long id, @RequestBody EmployeeDetails employeeDetails) {
        return employeeService.updateEmployee(id, employeeDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted successfully";
    }

    @PostMapping("/upload")
    public List<EmployeeDetails> uploadEmployeeFile(@RequestParam("file") MultipartFile file) {
        return employeeService.parseEmployeesFromFile(file);
    }

    @PostMapping("/upload-and-save")
    public List<EmployeeDetails> uploadAndSaveEmployeeFile(@RequestParam("file") MultipartFile file) {
        List<EmployeeDetails> employeeDetailsList = employeeService.parseEmployeesFromFile(file);
        return employeeService.saveAllEmployees(employeeDetailsList);
    }
}

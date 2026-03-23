package com.mco.service;


import com.mco.entity.EmployeeDetails;
import com.mco.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final tools.jackson.databind.ObjectMapper objectMapper;

    @Transactional
    public EmployeeDetails createEmployee(EmployeeDetails employeeDetails) {
        return employeeRepository.save(employeeDetails);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDetails> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EmployeeDetails getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Transactional
    public EmployeeDetails updateEmployee(Long id, EmployeeDetails employeeDetails) {
        EmployeeDetails existingEmployee = getEmployeeById(id);

        existingEmployee.setFirstName(employeeDetails.getFirstName());
        existingEmployee.setLastName(employeeDetails.getLastName());
        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setPhoneNumber(employeeDetails.getPhoneNumber());
        existingEmployee.setDateOfBirth(employeeDetails.getDateOfBirth());
        existingEmployee.setGender(employeeDetails.getGender());
        existingEmployee.setAddress(employeeDetails.getAddress());
        existingEmployee.setCity(employeeDetails.getCity());
        existingEmployee.setState(employeeDetails.getState());
        existingEmployee.setCountry(employeeDetails.getCountry());
        existingEmployee.setDepartment(employeeDetails.getDepartment());
        existingEmployee.setDesignation(employeeDetails.getDesignation());
        existingEmployee.setSalary(employeeDetails.getSalary());
        existingEmployee.setBonus(employeeDetails.getBonus());
        existingEmployee.setHireDate(employeeDetails.getHireDate());
        existingEmployee.setLastWorkingDate(employeeDetails.getLastWorkingDate());
        existingEmployee.setEmploymentType(employeeDetails.getEmploymentType());
        existingEmployee.setStatus(employeeDetails.getStatus());
        existingEmployee.setManagerName(employeeDetails.getManagerName());

        return employeeRepository.save(existingEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        EmployeeDetails employeeDetails = getEmployeeById(id);
        employeeRepository.delete(employeeDetails);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDetails> parseEmployeesFromFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Employee file is empty");
        }

        try {
            return objectMapper.readValue(file.getInputStream(), new tools.jackson.core.type.TypeReference<List<EmployeeDetails>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse employee file", e);
        }
    }

    @Transactional
    public List<EmployeeDetails> saveAllEmployees(List<EmployeeDetails> employeeDetailsList) {
        employeeDetailsList.forEach(employee -> employee.setId(null));
        return employeeRepository.saveAll(employeeDetailsList);
    }
}

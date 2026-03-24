package com.mco.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mco.entity.EmployeeDetails;
import com.mco.repo.EmployeeRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public EmployeeDetails createEmployee(EmployeeDetails employeeDetails) {
        EmployeeDetails sanitizedEmployee = prepareForPersistence(employeeDetails, true);
        return employeeRepository.save(sanitizedEmployee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDetails> getAllEmployees() {
        List<EmployeeDetails> employees = employeeRepository.findAll();
        return employees.isEmpty() ? Collections.emptyList() : employees;
    }

    @Transactional(readOnly = true)
    public EmployeeDetails getEmployeeById(Long id) {
        validateEmployeeId(id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    public EmployeeDetails updateEmployee(Long id, EmployeeDetails employeeDetails) {
        validateEmployeeId(id);
        validateEmployeePayload(employeeDetails);

        EmployeeDetails existingEmployee = getEmployeeById(id);
        EmployeeDetails sanitizedEmployee = prepareForPersistence(employeeDetails, false);

        existingEmployee.setFirstName(sanitizedEmployee.getFirstName());
        existingEmployee.setLastName(sanitizedEmployee.getLastName());
        existingEmployee.setEmail(sanitizedEmployee.getEmail());
        existingEmployee.setPhoneNumber(sanitizedEmployee.getPhoneNumber());
        existingEmployee.setDateOfBirth(sanitizedEmployee.getDateOfBirth());
        existingEmployee.setGender(sanitizedEmployee.getGender());
        existingEmployee.setAddress(sanitizedEmployee.getAddress());
        existingEmployee.setCity(sanitizedEmployee.getCity());
        existingEmployee.setState(sanitizedEmployee.getState());
        existingEmployee.setCountry(sanitizedEmployee.getCountry());
        existingEmployee.setDepartment(sanitizedEmployee.getDepartment());
        existingEmployee.setDesignation(sanitizedEmployee.getDesignation());
        existingEmployee.setSalary(sanitizedEmployee.getSalary());
        existingEmployee.setBonus(sanitizedEmployee.getBonus());
        existingEmployee.setHireDate(sanitizedEmployee.getHireDate());
        existingEmployee.setLastWorkingDate(sanitizedEmployee.getLastWorkingDate());
        existingEmployee.setEmploymentType(sanitizedEmployee.getEmploymentType());
        existingEmployee.setStatus(sanitizedEmployee.getStatus());
        existingEmployee.setManagerName(sanitizedEmployee.getManagerName());

        return employeeRepository.save(existingEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        validateEmployeeId(id);
        EmployeeDetails employeeDetails = getEmployeeById(id);
        employeeRepository.delete(employeeDetails);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDetails> parseEmployeesFromFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Employee file must not be null or empty");
        }

        try {
            List<EmployeeDetails> employeeDetailsList = objectMapper.readValue(
                    file.getInputStream(),
                    new TypeReference<>() {
                    }
            );
            return sanitizeEmployeeList(employeeDetailsList);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse employee file", e);
        }
    }

    @Transactional
    public List<EmployeeDetails> saveAllEmployees(List<EmployeeDetails> employeeDetailsList) {
        List<EmployeeDetails> sanitizedEmployees = sanitizeEmployeeList(employeeDetailsList);
        return employeeRepository.saveAll(sanitizedEmployees);
    }

    private void validateEmployeeId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Employee id must be a positive number");
        }
    }

    private void validateEmployeePayload(EmployeeDetails employeeDetails) {
        if (employeeDetails == null) {
            throw new IllegalArgumentException("Employee details must not be null");
        }
    }

    private List<EmployeeDetails> sanitizeEmployeeList(List<EmployeeDetails> employeeDetailsList) {
        if (employeeDetailsList == null || employeeDetailsList.isEmpty()) {
            throw new IllegalArgumentException("Employee list must not be null or empty");
        }

        List<EmployeeDetails> sanitizedEmployees = new ArrayList<>(employeeDetailsList.size());
        for (EmployeeDetails employeeDetails : employeeDetailsList) {
            sanitizedEmployees.add(prepareForPersistence(employeeDetails, true));
        }
        return sanitizedEmployees;
    }

    private EmployeeDetails prepareForPersistence(EmployeeDetails employeeDetails, boolean resetId) {
        validateEmployeePayload(employeeDetails);
        validateRequiredFields(employeeDetails);
        validateBusinessRules(employeeDetails);

        if (resetId) {
            employeeDetails.setId(null);
        }

        normalizeTextFields(employeeDetails);
        return employeeDetails;
    }

    private void validateRequiredFields(EmployeeDetails employeeDetails) {
        if (isBlank(employeeDetails.getFirstName())) {
            throw new IllegalArgumentException("Employee first name must not be blank");
        }
    }

    private void validateBusinessRules(EmployeeDetails employeeDetails) {
        if (employeeDetails.getSalary() != null && employeeDetails.getSalary() < 0) {
            throw new IllegalArgumentException("Employee salary must not be negative");
        }
        if (employeeDetails.getBonus() != null && employeeDetails.getBonus() < 0) {
            throw new IllegalArgumentException("Employee bonus must not be negative");
        }
        if (employeeDetails.getHireDate() != null
                && employeeDetails.getLastWorkingDate() != null
                && employeeDetails.getLastWorkingDate().isBefore(employeeDetails.getHireDate())) {
            throw new IllegalArgumentException("Employee last working date must not be before hire date");
        }
        if (employeeDetails.getDateOfBirth() != null
                && employeeDetails.getHireDate() != null
                && employeeDetails.getHireDate().isBefore(employeeDetails.getDateOfBirth())) {
            throw new IllegalArgumentException("Employee hire date must not be before date of birth");
        }
    }

    private void normalizeTextFields(EmployeeDetails employeeDetails) {
        employeeDetails.setFirstName(normalize(employeeDetails.getFirstName()));
        employeeDetails.setLastName(normalize(employeeDetails.getLastName()));
        employeeDetails.setEmail(normalize(employeeDetails.getEmail()));
        employeeDetails.setPhoneNumber(normalize(employeeDetails.getPhoneNumber()));
        employeeDetails.setGender(normalize(employeeDetails.getGender()));
        employeeDetails.setAddress(normalize(employeeDetails.getAddress()));
        employeeDetails.setCity(normalize(employeeDetails.getCity()));
        employeeDetails.setState(normalize(employeeDetails.getState()));
        employeeDetails.setCountry(normalize(employeeDetails.getCountry()));
        employeeDetails.setDepartment(normalize(employeeDetails.getDepartment()));
        employeeDetails.setDesignation(normalize(employeeDetails.getDesignation()));
        employeeDetails.setEmploymentType(normalize(employeeDetails.getEmploymentType()));
        employeeDetails.setStatus(normalize(employeeDetails.getStatus()));
        employeeDetails.setManagerName(normalize(employeeDetails.getManagerName()));
    }

    private String normalize(String value) {
        if (Objects.isNull(value)) {
            return null;
        }

        String trimmedValue = value.trim();
        return trimmedValue.isEmpty() ? null : trimmedValue;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

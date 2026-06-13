package com.mco.controller;

import com.mco.dto.BulkEmployeeSaveResponse;
import com.mco.dto.BulkSeedResponse;
import com.mco.entity.Employee;
import com.mco.service.EmployeeDocumentService;
import com.mco.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/docAPI")
@Validated
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequiredArgsConstructor
public class DocumentController {

    private final EmployeeService employeeService;
    private final EmployeeDocumentService employeeDocumentService;


    @GetMapping("/{employeeId}/pdf")
    public ResponseEntity<byte[]> getEmployeePdf(@PathVariable Long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        byte[] pdfBytes = employeeDocumentService.generateEmployeePdf(employee);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=\"employee-" + employeeId + ".pdf\"")
                .body(pdfBytes);
    }

    @GetMapping("/pdfs")
    public ResponseEntity<byte[]> getAllEmployeePdfs() {
        List<Employee> employees = employeeService.findAll();
        byte[] zipBytes = employeeDocumentService.generateEmployeesZip(employees);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header("Content-Disposition", "attachment; filename=\"employee-pdfs.zip\"")
                .body(zipBytes);
    }
}

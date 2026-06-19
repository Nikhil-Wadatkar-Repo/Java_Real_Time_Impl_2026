package com.mco.controller;

import com.mco.dto.BulkEmployeeSaveResponse;
import com.mco.dto.BulkSeedResponse;
import com.mco.entity.Employee;
import com.mco.service.DataLoadService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class DataLoadController {

    private final DataLoadService dataLoadService;

    @PostMapping("/bulk")
    public ResponseEntity<BulkEmployeeSaveResponse> createBulk(@Valid @RequestBody List<@Valid Employee> employees) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataLoadService.createBulk(employees));
    }

    @PostMapping("/seed-real-time")
    public ResponseEntity<BulkSeedResponse> seedRealTimeEmployees(@RequestParam(defaultValue = "20000") @Min(1) int count) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataLoadService.seedRealTimeEmployees(count));
    }

    @PostMapping("/seed-another-30000")
    public ResponseEntity<BulkSeedResponse> seedAnother30000Employees() {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataLoadService.seedAnother30000Employees());
    }

}

package com.mco.service;

import com.mco.Utility;
import com.mco.constants.CommonConstants;
import com.mco.dto.BulkEmployeeSaveResponse;
import com.mco.dto.BulkSeedResponse;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataLoadService {

    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public BulkEmployeeSaveResponse createBulk(List<Employee> employees) {
        List<Employee> savedEmployees = new ArrayList<>();
        List<String> skippedReasons = new ArrayList<>();

        for (Employee employee : employees) {
            employee.setEmployeeId(null);
            if (employeeRepository.existsByEmail(employee.getEmail())) {
                skippedReasons.add("Skipped email " + employee.getEmail() + " because it already exists");
                continue;
            }
            savedEmployees.add(employeeRepository.save(employee));
        }

        return BulkEmployeeSaveResponse.builder().requestedCount(employees.size()).savedCount(savedEmployees.size()).skippedCount(skippedReasons.size()).savedEmployees(savedEmployees).skippedReasons(skippedReasons).build();
    }

    @Transactional
    public BulkSeedResponse seedRealTimeEmployees(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        }

        long start = System.currentTimeMillis();
        String runToken = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        int batchSize = 500;

        for (int i = 0; i < count; i++) {
            Employee employee = buildRealTimeEmployee(i, runToken);
            entityManager.persist(employee);

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();

        long duration = System.currentTimeMillis() - start;
        log.info("Inserted {} synthetic employee records in {} ms.", count, duration);

        return BulkSeedResponse.builder().requestedCount(count).insertedCount(count).durationMs(duration).message("Inserted " + count + " employee records with realistic generated data").build();
    }

    @Transactional
    public BulkSeedResponse seedAnother30000Employees() {
        return seedUniqueSyntheticEmployees(30000, "Inserted another 30000 employee records with unique emails");
    }

    private BulkSeedResponse seedUniqueSyntheticEmployees(int count, String messagePrefix) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        }

        long start = System.currentTimeMillis();
        String runToken = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        int batchSize = 500;
        Set<String> existingEmails = new HashSet<>(employeeRepository.findAll().stream().map(Employee::getEmail).filter(email -> email != null && !email.isBlank()).toList());
        Set<String> generatedEmails = new HashSet<>();

        int insertedCount = 0;
        int seedIndex = 0;
        while (insertedCount < count) {
            Employee employee = buildRealTimeEmployee(seedIndex++, runToken);
            String email = employee.getEmail();

            if (existingEmails.contains(email) || !generatedEmails.add(email)) {
                continue;
            }

            entityManager.persist(employee);
            insertedCount++;

            if (insertedCount % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();

        long duration = System.currentTimeMillis() - start;
        log.info("Inserted {} synthetic employee records in {} ms.", insertedCount, duration);

        return BulkSeedResponse.builder().requestedCount(count).insertedCount(insertedCount).durationMs(duration).message(messagePrefix).build();
    }

    private Employee buildRealTimeEmployee(int index, String runToken) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String firstName = CommonConstants.FIRST_NAMES.get(random.nextInt(CommonConstants.FIRST_NAMES.size()));
        String lastName = CommonConstants.LAST_NAMES.get(random.nextInt(CommonConstants.LAST_NAMES.size()));
        String email = (firstName + "." + lastName + "." + runToken + "." + (index + 1) + "@example.com").toLowerCase();

        return Employee.builder().firstName(firstName).lastName(lastName).email(email).phoneNumber(9000000000L + random.nextLong(100000000L)).hireDate(LocalDate.now().minusDays(random.nextInt(30, 3650))).jobId(100 + random.nextInt(900)).salary(BigDecimal.valueOf(random.nextDouble(30000.0, 250000.0)).setScale(2, RoundingMode.HALF_UP)).managerId(10 + random.nextInt(90)).departmentId(1 + random.nextInt(25)).address(random.nextInt(100, 9999) + " " + lastName + " Street").city(CommonConstants.CITIES.get(random.nextInt(CommonConstants.CITIES.size()))).state(CommonConstants.STATES.get(random.nextInt(CommonConstants.STATES.size()))).country(CommonConstants.COUNTRIES.get(random.nextInt(CommonConstants.COUNTRIES.size()))).designation(CommonConstants.DESIGNATIONS.get(random.nextInt(CommonConstants.DESIGNATIONS.size()))).employmentType(CommonConstants.EMPLOYMENT_TYPES.get(random.nextInt(CommonConstants.EMPLOYMENT_TYPES.size()))).gender(random.nextBoolean() ? "Male" : "Female").status("ACTIVE").localStatus("ACTIVE").bonus(BigDecimal.valueOf(random.nextDouble(1000.0, 25000.0)).setScale(2, RoundingMode.HALF_UP)).build();
    }
}

package com.mco.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

import com.mco.Utility;
import com.mco.dto.BulkSeedResponse;
import com.mco.dto.BulkEmployeeSaveResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final List<String> FIRST_NAMES = Arrays.asList("Aarav", "Vivaan", "Aditya", "Arjun", "Ishaan", "Vihaan", "Kabir", "Rohan", "Rahul", "Neha", "Priya", "Ananya", "Kavya", "Sneha", "Pooja", "Meera");

    private static final List<String> LAST_NAMES = Arrays.asList("Sharma", "Verma", "Gupta", "Mehta", "Patel", "Singh", "Iyer", "Nair", "Kapoor", "Joshi", "Reddy", "Chopra", "Mishra", "Malhotra", "Bansal", "Bhat");

    private static final List<String> CITIES = Arrays.asList("Bengaluru", "Hyderabad", "Pune", "Chennai", "Mumbai", "Delhi", "Ahmedabad", "Kolkata");

    private static final List<String> STATES = Arrays.asList("Karnataka", "Telangana", "Maharashtra", "Tamil Nadu", "Delhi", "Gujarat", "West Bengal", "Rajasthan");

    private static final List<String> COUNTRIES = Arrays.asList("India", "India", "India", "India");

    private static final List<String> DESIGNATIONS = Arrays.asList("Software Engineer", "Senior Software Engineer", "QA Engineer", "Business Analyst", "DevOps Engineer", "Data Analyst", "Product Analyst", "Support Engineer");

    private static final List<String> EMPLOYMENT_TYPES = Arrays.asList("FULL_TIME", "CONTRACT", "INTERN", "PART_TIME");

    @Transactional
    public Employee create(Employee employee) {
        //// employee.setEmployeeId(null);
        // if ( null != employee.getEmail() &&
        //// employeeRepository.existsByEmail(employee.getEmail())) {
        // throw new DuplicateResourceException("Employee already exists with email " +
        //// employee.getEmail());
        // }
        return employeeRepository.save(employee);
    }

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

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

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

    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));
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
        String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
        String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
        String email = (firstName + "." + lastName + "." + runToken + "." + (index + 1) + "@example.com").toLowerCase();

        return Employee.builder().firstName(firstName).lastName(lastName).email(email).phoneNumber(9000000000L + random.nextLong(100000000L)).hireDate(LocalDate.now().minusDays(random.nextInt(30, 3650))).jobId(100 + random.nextInt(900)).salary(BigDecimal.valueOf(random.nextDouble(30000.0, 250000.0)).setScale(2, RoundingMode.HALF_UP)).managerId(10 + random.nextInt(90)).departmentId(1 + random.nextInt(25)).address(random.nextInt(100, 9999) + " " + lastName + " Street").city(CITIES.get(random.nextInt(CITIES.size()))).state(STATES.get(random.nextInt(STATES.size()))).country(COUNTRIES.get(random.nextInt(COUNTRIES.size()))).designation(DESIGNATIONS.get(random.nextInt(DESIGNATIONS.size()))).employmentType(EMPLOYMENT_TYPES.get(random.nextInt(EMPLOYMENT_TYPES.size()))).gender(random.nextBoolean() ? "Male" : "Female").status("ACTIVE").localStatus("ACTIVE").bonus(BigDecimal.valueOf(random.nextDouble(1000.0, 25000.0)).setScale(2, RoundingMode.HALF_UP)).build();
    }

    public Page<Employee> getCustomers(int page, int size, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employeePage = null;
        try {
            employeePage = employeeRepository.findAll(pageable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);

        }
        return employeePage;
    }

    @Autowired
    public Utility processor;
    @Autowired
    private Executor taskExecutor;

    public String processAllEmployees() {
        if (true)
            throw new RuntimeException("Fekla");
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

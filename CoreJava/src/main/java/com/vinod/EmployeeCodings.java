package com.vinod;

import com.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EmployeeCodings {
    private static List<Employee> list = Employee.getEmployees();

    public static void main(String[] args) {
        printAllEmployees();
        printHighestPaidEmployee();
        printEmployeesByDepartment();
        printCountByDepartment();
        printAverageSalaryByDepartment();
        printHighestSalaryByDepartment();
        printEmployeeNamesByDepartment();
        printTotalSalary();
        printDepartmentWiseTotalSalary();
    }

    private static void printAllEmployees() {
        printSeparator();
        System.out.println("All Employees:");
        list.forEach(System.out::println);
    }

    private static void printHighestPaidEmployee() {
        printSeparator();
        System.out.println("Highest Paid Employee:");

        list.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .findFirst()
                .ifPresent(System.out::println);

        Employee highestPaidEmployee = list.stream()
                .max(Comparator.comparing(Employee::getSalary))
                .orElse(null);
        System.out.println(highestPaidEmployee);
    }

    private static void printEmployeesByDepartment() {
        printSeparator();
        System.out.println("Group Employees By Department:");

        Map<String, List<Employee>> employeesByDept = list.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
        employeesByDept.forEach((dept, employees) -> {
            System.out.println("Department: " + dept);
            employees.forEach(emp -> System.out.print(emp.getName() + " "));
            System.out.println();
        });
    }

    private static void printCountByDepartment() {
        printSeparator();
        System.out.println("Count Employees By Department:");

        Map<String, Long> countByDept = list.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
        System.out.println(countByDept);
    }

    private static void printAverageSalaryByDepartment() {
        printSeparator();
        System.out.println("Average Salary By Department:");

        Map<String, Double> avgSalaryByDept = list.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)));
        System.out.println(avgSalaryByDept);
    }

    private static void printHighestSalaryByDepartment() {
        printSeparator();
        System.out.println("Highest Salary By Department:");

        Map<String, Optional<Employee>> highestSalaryByDept = list.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))));
        System.out.println(highestSalaryByDept);
    }

    private static void printEmployeeNamesByDepartment() {
        printSeparator();
        System.out.println("Employee Names By Department:");

        Map<String, List<String>> empNamesByDept = list.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.mapping(Employee::getName, Collectors.toList())));
        System.out.println(empNamesByDept);
    }

    private static void printTotalSalary() {
        printSeparator();
        System.out.println("Total Salary:");

        double totalSalary = list.stream()
                .map(Employee::getSalary)
                .reduce(0.0, Double::sum);
        System.out.println("Total Salary: " + totalSalary);

        double totalSalarySum = list.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
        System.out.println("Total Salary (using sum): " + totalSalarySum);
    }

    private static void printDepartmentWiseTotalSalary() {
        printSeparator();
        System.out.println("Department Wise Total Salary:");

        Map<String, Double> deptWiseTotalSalary = list.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.summingDouble(Employee::getSalary)));
        System.out.println(deptWiseTotalSalary);
    }

    private static void printSeparator() {
        System.out.println("=========================================================");
    }
}


package com.vinod;

import com.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class Employee_Coding_Questions {
        private static List<Employee> list = Employee.getEmployees();

        private static void printAllEmployees() {
                System.out.println("=========================================================");
                System.out.println("All Employees:");
                list.forEach(System.out::println);
                System.out.println("=========================================================");
        }

        static {
                printAllEmployees();
        }

        public static void main(String[] args) {

                // Find all employees whose salary is greater than 50,000.
                // Find employees belonging to "IT" department.
                // Find employees older than 30.
                // Find the employee with the highest salary.
                // Find the employee with the lowest salary.

                // Find the second-highest salary.
                // Find the second-lowest salary.
                // Find the third-highest salary.
                // printSecondHighestPaidEmployee();
                // printNthHighestPaidEmployee(2);

                // Find the highest-paid employee in each department.
                // Find the lowest-paid employee in each department.
                // printHighestPaidEmployeeInEachDepartment();

                // Find the average salary of employees.
                // printAvgSalary();
                // Find the average salary of each department.
                // printAvgSalaryDeptWise();

                // Find the total salary paid by each department.
                // printTotalSalaryByDepartment();
                // Find the employee count in each department.
                // Find the employee count by gender.
                // Find the average salary by gender.
                // Find the highest salary by gender.
                // Find the youngest employee.
                // Find the oldest employee.

                // Find the youngest employee from each department.
                // printYoungestEmployeeFromEachDepartment();
                // Find the oldest employee from each department.

                // Find employees whose salary is greater than the average salary.
                // employeeSalaryGreaterThanAvgSalary();
                // Find employees whose salary is greater than their department's average
                // salary.
                // findEmployeeSalaryGTDeptAvgSalary();
                // Find the department having the highest average salary.
                // findHighestAverageSalaryDepartment();
                // Find the department having the lowest average salary.
                // findLowestAvgSalaryDeptWise();

                // Find the department having the highest number of employees.
                // Find the department having the lowest number of employees.
                // highestNumberWisedept();

                // Sort employees by salary.Find the department having the highest average
                // salary.
                // deptHavingHighestSalary();

                // Sort employees by salary descending.

                // Sort employees by department and then salary.
                // Sort employees by salary and then name.

                // Find the top 3 highest-paid employees.
                // Find the top 3 highest-paid employees from each department.
                find3HighestPaidEmployeesDeptWise();

                // Find duplicate employee names.

                // Find employees having the same salary.
                // Find employees whose names start with "A".
                // Find the average salary of employees whose age is greater than 30.
                // Find the sum of salaries of employees from the IT department.

        }

        // Find the second-highest salary.
        private static void printSecondHighestPaidEmployee() {
                // list.stream().sorted(Comparator.comparingDouble(Employee::getSalary).reversed()).map(Employee::getSalary).forEach(item->
                // System.out.println(item+ "|"));

                list.stream().sorted(Comparator.comparingDouble(Employee::getSalary).reversed()).skip(1).findFirst()
                                .ifPresent(System.out::println);
        }

        private static void printNthHighestPaidEmployee(int n) {
                // list.stream().sorted(Comparator.comparingDouble(Employee::getSalary).reversed()).map(Employee::getSalary).forEach(item->
                // System.out.println(item+ "|"));
                list.stream().sorted(Comparator.comparingDouble(Employee::getSalary).reversed()).skip(n - 1).findFirst()
                                .ifPresent(System.out::println);
        }

        // Find the highest-paid employee in each department.
        private static void printHighestPaidEmployeeInEachDepartment() {
                list.stream()
                                .collect(Collectors.groupingBy(Employee::getDepartment,
                                                Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))))
                                .forEach((department, employee) -> System.out
                                                .println(department + ": " + employee.orElse(null)));
        }

        // Find the average salary of employees.
        private static void printAvgSalary() {
                list.stream().mapToDouble(Employee::getSalary).average()
                                .ifPresent(avg -> System.out.println("Average Salary: " + avg));
        }

        // Find the average salary of each department.
        private static void printAvgSalaryDeptWise() {
                System.out.println("\nDepartment wise average salary");
                System.out.println("---------------------------------------");
                Map<String, Double> collect = list.stream().collect(
                                Collectors.groupingBy(Employee::getDepartment,
                                                Collectors.averagingDouble(Employee::getSalary)));

                collect.forEach((department, avgSalary) -> System.out.println(department + ": " + avgSalary));
        }

        // Find the total salary paid by each department.
        public static void printTotalSalaryByDepartment() {
                System.out.println("Total salary paid by each department:");
                System.out.println("--------------------------------------");
                list.stream()
                                .collect(Collectors.groupingBy(Employee::getDepartment,
                                                Collectors.summingDouble(Employee::getSalary)))
                                .forEach((department, totalSalary) -> System.out
                                                .println(department + ": " + totalSalary));
        }

        // Find the youngest employee from each department.
        public static void printYoungestEmployeeFromEachDepartment() {
                System.out.println("Youngest employee from each department:");
                System.out.println("---------------------------------------");
                list.stream()
                                .collect(Collectors.groupingBy(Employee::getDepartment,
                                                Collectors.minBy(Comparator.comparingInt(Employee::getAge))))
                                .forEach((department, employee) -> System.out
                                                .println(department + ": " + employee.orElse(null)));
        }

        // Find employees whose salary is greater than the average salary.
        public static void employeeSalaryGreaterThanAvgSalary() {
                list.stream().filter(employee -> {
                        double v = list.stream().mapToDouble(Employee::getSalary).average().orElse(0);
                        return employee.getSalary() > v;
                }).forEach(System.out::println);
        }

        // Find employees whose salary is greater than their department's average
        // salary.
        public static void findEmployeeSalaryGTDeptAvgSalary() {
                list.stream().filter(employee -> employee.getDepartment().startsWith("Dept"))
                                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                                .forEach(System.out::println);
        }

        // Find the department having the highest average salary.
        public static void findHighestAverageSalaryDepartment() {
                // department wise average salaries
                Map<String, Double> highestDeptSalary = list.stream().collect(
                                Collectors.groupingBy(Employee::getDepartment,
                                                Collectors.averagingDouble(Employee::getSalary)));
                System.out.println(highestDeptSalary);
                highestDeptSalary.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                                .ifPresent(entry -> System.out
                                                .println("Department with highest average salary: " + entry.getKey()
                                                                + " with average salary: " + entry.getValue()));

                /////////////////// OR
                // list.stream()
                // .collect(Collectors.groupingBy(Employee::getDepartment,
                /////////////////// Collectors.averagingDouble(Employee::getSalary)))
                // .entrySet()
                // .stream()
                // .max(Comparator.comparingDouble(Map.Entry::getValue))
                // .ifPresent(entry -> System.out.println("Department with highest average
                /////////////////// salary: " + entry.getKey() + " with average salary: " +
                /////////////////// entry.getValue()));

        }

        // Find the department having the lowest average salary.
        public static void findLowestAvgSalaryDeptWise() {
                list.stream()
                                .collect(
                                                Collectors.groupingBy(Employee::getDepartment,
                                                                Collectors.averagingDouble(Employee::getSalary)))
                                .entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue))
                                .ifPresent(mapEntry -> System.out.println(
                                                "Departmentwise lowest average salary : " + mapEntry.getKey() + " ==>"
                                                                + mapEntry.getValue()));
        }

        // Find the department having the highest number of employees.
        public static void highestNumberWisedept() {

                Map<String, Long> deptWiseCount = list.stream()
                                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));

                Optional<Entry<String, Long>> maxDeptWiseEmployeeCount = deptWiseCount.entrySet().stream()
                                .max(Comparator.comparingDouble(Map.Entry::getValue));

                System.out.println(maxDeptWiseEmployeeCount.get());
        }

        // Sort employees by salary.Find the department having the highest average
        // salary.
        public static void deptHavingHighestSalary() {
                Map<String, Optional<Employee>> collect = list.stream()
                                .sorted(Comparator.comparing(Employee::getSalary))
                                .collect(Collectors.groupingBy(Employee::getDepartment,
                                                Collectors.maxBy(Comparator.comparing(Employee::getSalary))));
                collect.entrySet().stream().forEach(map -> {
                        System.out.println("dept: " + map.getKey() + "                 "
                                        + map.getValue().get().getSalary());
                });
                System.out.println("************** === **************");

                list.stream()
                                .collect(
                                                Collectors.groupingBy(Employee::getDepartment,
                                                                Collectors.averagingDouble(Employee::getSalary)))
                                .entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue))
                                .ifPresent(mapEntry -> System.out.println(
                                                "Departmentwise highest average salary : " + mapEntry.getKey() + " ==>"
                                                                + mapEntry.getValue()));
        }

        // Sort employees by salary descending.
        public static void sortEmpBySalaDesc() {
                list.stream().sorted(Comparator.comparing(Employee::getSalary).reversed()).collect(Collectors.toList())
                                .forEach(System.out::println);

        }

        // Sort employees by department and then salary.
        public static void sortEmpByDeptAndSal() {
                list.stream().sorted(Comparator.comparing(Employee::getDepartment).thenComparing(Employee::getSalary))
                                .collect(Collectors.toList()).forEach(System.out::println);
        }

        // Sort employees by salary and then name.
        public static void sortEmpBySalAndName() {
                list.stream().sorted(Comparator.comparing(Employee::getSalary).thenComparing(Employee::getName))
                                .collect(Collectors.toList()).forEach(System.out::println);
        }

        // Find the top 3 highest-paid employees.
        public static void find3HighestEmployees() {
                list.stream().sorted(Comparator.comparing(Employee::getSalary).reversed()).limit(3)
                                .forEach(System.out::println);
        }

        // Find the top 3 highest-paid employees from each department.
        public static void find3HighestPaidEmployeesDeptWise() {
                list.stream()
                                .collect(Collectors.groupingBy(Employee::getDepartment,
                                                Collectors.collectingAndThen(Collectors.toList(),
                                                                empList -> empList.stream()
                                                                                .sorted(Comparator.comparing(
                                                                                                Employee::getSalary)
                                                                                                .reversed())
                                                                                .limit(3)
                                                                                .collect(Collectors.toList()))))
                                .forEach((department, topEmployees) -> {
                                        System.out.println("Department: " + department);
                                        topEmployees.forEach(System.out::println);
                                });
        }

        // Find duplicate employee names.
        public static void findDuplicateEmps() {
                list.stream().collect(Collectors.groupingBy(Employee::getName, Collectors.counting())).entrySet()
                                .stream()
                                .filter(entry -> entry.getValue() > 1).forEach(entry -> System.out.println(
                                                "Duplicate Name: " + entry.getKey() + ", Count: " + entry.getValue()));
        }

        // Find employees having the same salary.
        public static void findEmpHavingSameSal() {
                list.stream().collect(Collectors.groupingBy(Employee::getSalary, Collectors.counting())).entrySet()
                                .stream()
                                .filter(entry -> entry.getValue() > 1).forEach(entry -> System.out.println(
                                                "Duplicate Salary: " + entry.getKey() + ", Count: "
                                                                + entry.getValue()));

        }

        // Find employees whose names start with "A".
        public static void findEmpNameStartWithA() {
                list.stream().filter(emp -> emp.getName().startsWith("A")).forEach(System.out::println);
        }

        // Find the average salary of employees whose age is greater than 30.
        public static void findEmpAvgSalAgeGT30() {
                double average = list.stream().filter(emp -> emp.getAge() > 30).mapToDouble(Employee::getSalary)
                                .average().orElse(0.0);
                System.out.println("Average Salary of Employees (Age > 30): " + average);
        }

        // Find the sum of salaries of employees from the IT department.
        public static void findSalSumFromITDept() {
                double sum = list.stream().filter(emp -> "IT".equals(emp.getDepartment()))
                                .mapToDouble(Employee::getSalary).sum();
                System.out.println("Sum of Salaries from IT Department: " + sum);
        }

}

package com.mco;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stream_API_Demo {

    public static void main(String[] args) {
        List<Employee> employees = Employee.getEmployees();

        // How many male and female employees are there in the organization?
        Map<String, Long> genderWiseCount = employees.stream()
                .collect(Collectors.groupingBy(Employee::getGender, Collectors.counting()));
        System.out.println("Gender wise count: " + genderWiseCount);

        // print the name of all departments in the organization
        List<String> names = employees.stream().map(Employee::getDepartment).distinct().collect(Collectors.toList());
        System.out.println(names);

        // What is the average age of male and female employees
        Map<String, Double> genderwiseAvgAge = employees.stream()
                .collect(Collectors.groupingBy(Employee::getGender, Collectors.averagingInt(Employee::getAge)));
        System.out.println(genderwiseAvgAge);

        // Get the details of highest paid employee in the organization
        Employee highestPaidEmployee = employees.stream().sorted(Comparator.comparing(Employee::getSalary).reversed())
                .findFirst().orElse(null);
        System.out.println(highestPaidEmployee.getName() + "  " + highestPaidEmployee.getSalary());

        // Get the names of all employees who have joined after 2015?
        employees.stream().filter(item -> item.getYearOfJoining() > 2015).forEach(System.out::println);

        // Count the number of employees in each department?
        Map<String, Long> deptWiseCount = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
        System.out.println(deptWiseCount);
        // What is the average salary of each department?
        Map<String, Double> deptWiseAvgSalary = employees.stream().collect(
                Collectors.groupingBy(Employee::getDepartment, Collectors.averagingDouble(Employee::getSalary)));
        System.out.println(deptWiseAvgSalary);

        // Get the details of youngest male employee in the product development
        // department
        Employee youngestMaleEmployee = employees.stream()
                .filter(item -> item.getGender().equalsIgnoreCase("male")
                        && item.getDepartment().equalsIgnoreCase("product development"))
                .sorted(Comparator.comparing(Employee::getAge).reversed()).findFirst().orElse(null);
        System.out.println(youngestMaleEmployee);

        // Who has the most working experience in the organization
        Employee experiencedEmployee = employees.stream().sorted(Comparator.comparing(Employee::getYearOfJoining))
                .findFirst().orElse(null);
        System.out.println(experiencedEmployee.name + " " + experiencedEmployee.getYearOfJoining());

        // How many male and female employees are there in the sales and marketing team?
        Map<String, Long> sam = employees.stream()
                .filter(item -> item.getDepartment().equalsIgnoreCase("sales and marketing"))
                .collect(Collectors.groupingBy(Employee::getGender, Collectors.counting()));
        System.out.println(sam);

        // What is the average salary of male and female employees
        Map<String, Double> avgDeptwiseSalary = employees.stream()
                .collect(Collectors.groupingBy(Employee::getGender, Collectors.averagingDouble(Employee::getSalary)));
        System.out.println(avgDeptwiseSalary);

        // List down the names of all employees in each department
        Map<String, List<Employee>> collect = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
        collect.entrySet().stream().forEach(item -> {
            System.out.println(item.getKey() + "   " + item.getValue());
        });

        // What is the average salary and total salary of the whole organization
        DoubleSummaryStatistics avgSalTotSal = employees.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println(avgSalTotSal.getAverage() + "    " + avgSalTotSal.getSum());


        //Separate the employees who are younger or equal to 25 years from those employees who are older than 25 years.
        Map<Boolean, List<Employee>> collect1 = employees.stream().collect(Collectors.partitioningBy(item -> item.getAge() > 25));
        System.out.println(collect1.get(true));

    }

}

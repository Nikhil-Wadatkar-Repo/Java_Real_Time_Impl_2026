package com.nt.Logic2026;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Employee {
	// final: equals/hashCode are derived from these fields, so they must never
	// change after construction - mutable fields here would silently break any
	// hash-based collection (HashSet/LinkedHashSet/distinct()) an Employee is
	// stored in.
	final String name, department;
	final double salary;

	Employee(String name, String department, double salary) {
		this.name = name;
		this.department = department;
		this.salary = salary;
	}

	public static List<Employee> getEmployees() {
		List<Employee> employees = Arrays.asList(new Employee("Alice", "HR", 50000), new Employee("Bob", "IT", 80000),
				new Employee("Charlie", "IT", 75000), new Employee("Dave", "HR", 60000),
				new Employee("Eve", "Finance", 70000));
		return employees;

	}

	// Two employees are considered duplicates when name, department and salary
	// all match.
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Employee))
			return false;
		Employee other = (Employee) o;
		return Double.compare(salary, other.salary) == 0 && Objects.equals(name, other.name)
				&& Objects.equals(department, other.department);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, department, salary);
	}

	@Override
	public String toString() {
		return "Employee{name=" + name + ", department=" + department + ", salary=" + salary + "}";
	}
}

//Find Employees with Highest Salary in Each Department
//34. Find sum of all digits of a number in Java 8?
//36. How do you find common elements between two arrays?
//44. Missing number from 1 to N using Stream + sum
//47. Merge two unsorted lists into one sorted list
//Check if all elements are unique
//Find duplicate employee names

public class App {

	/**
	 * Concatenates two employee lists into one, removing duplicates (as defined
	 * by {@link Employee#equals(Object)}/{@link Employee#hashCode()}) while
	 * preserving first-seen order - list1's elements come first, followed by any
	 * elements of list2 not already present.
	 *
	 * Edge cases handled:
	 * <ul>
	 * <li>Either or both lists being {@code null} - treated as empty, no NPE.</li>
	 * <li>{@code null} elements inside either list - silently skipped, so a
	 * single {@code null} never appears in the result and two {@code null}
	 * entries are not conflated as "duplicates".</li>
	 * <li>Duplicates within the same list (not just across the two lists).</li>
	 * <li>Empty lists / both lists empty - returns an empty (not null) list.</li>
	 * <li>Input lists are never mutated.</li>
	 * <li>The returned list is unmodifiable, so callers can't corrupt internal
	 * state or break the no-duplicates invariant after the fact.</li>
	 * </ul>
	 *
	 * Not thread-safe to call concurrently with mutation of the *input* lists
	 * (standard List semantics), but the method itself holds no shared state and
	 * is safe to call concurrently from multiple threads. Runs in O(n + m) time
	 * and space, where n and m are the sizes of list1 and list2.
	 *
	 * @param list1 first list, may be {@code null} or contain {@code null}s
	 * @param list2 second list, may be {@code null} or contain {@code null}s
	 * @return a new unmodifiable, duplicate-free list; never {@code null}
	 */
	public static List<Employee> concatWithoutDuplicates(List<Employee> list1, List<Employee> list2) {
		List<Employee> safeList1 = list1 == null ? Collections.emptyList() : list1;
		List<Employee> safeList2 = list2 == null ? Collections.emptyList() : list2;

		Set<Employee> merged = new LinkedHashSet<>(safeList1.size() + safeList2.size());
		Stream.concat(safeList1.stream(), safeList2.stream()).filter(Objects::nonNull).forEach(merged::add);

		return Collections.unmodifiableList(new ArrayList<>(merged));
	}

	public static void main(String[] args) {
		List<String> words = Arrays.asList("Java", "Stream", "API");
		Map<String, Integer> wordLengthMap = words.stream()
				.collect(Collectors.toMap(word -> word, wor -> wor.length()));
		System.out.println(wordLengthMap); // Output: {Java=4, Stream=6, API=3}

		int n = 10;
		List<Integer> fibonacci = Stream.iterate(new int[] { 0, 1 }, arr -> new int[] { arr[1], arr[0] + arr[1] })
				.limit(n).map(arr -> arr[0]).collect(Collectors.toList());
		System.out.println(fibonacci);

		List<Employee> list1 = Employee.getEmployees();
		List<Employee> list2 = Arrays.asList(new Employee("Bob", "IT", 80000), // duplicate of list1
				new Employee("Frank", "Finance", 65000));
		List<Employee> merged = concatWithoutDuplicates(list1, list2);
		System.out.println(merged);
	}
}



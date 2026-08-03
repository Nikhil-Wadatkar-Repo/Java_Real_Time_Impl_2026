package com.realtime_30.basic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//8. Sort a list of integers in both ascending and descending order
public class Test8 {

	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(5, 12, 18, 7, 25, 3);

		List<Integer> ascending = numbers.stream().sorted().collect(Collectors.toList());
		System.out.println("ascending: " + ascending);

		List<Integer> descending = numbers.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		System.out.println("descending : " + descending);
	}
}

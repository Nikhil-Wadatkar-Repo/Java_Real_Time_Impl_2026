package com.realtime_30.basic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

//3. From a list, remove all duplicate numbers
public class Test3 {

	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(10, 20, 5, 8, 30, 15);
		numbers.stream()
				.distinct()
				.forEach(System.out::println);
	}
}

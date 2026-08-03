package com.realtime_30.basic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//4. Count how many times each number occurs in a list
public class Test4 {

	public static void main(String[] args) {
		 List<Integer> numbers = Arrays.asList(10, 20, 10, 30, 20, 40, 10);

	        Map<Integer, Long> frequency = numbers.stream()
	        									   .collect(Collectors.groupingBy(n -> n, Collectors.counting()));
	        System.out.println("Frequency Count: " + frequency);
	}
}

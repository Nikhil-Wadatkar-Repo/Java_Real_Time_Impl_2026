package com.realtime_30.basic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//5. Calculate the sum of all numbers in a collection
public class Test5 {

	public static void main(String[] args) {
		 List<Integer> numbers = Arrays.asList(5, 10, 15, 20);

	        int sum = numbers.stream()
	                         .mapToInt(Integer::intValue)
	                         .sum();

	        System.out.println("Sum: " + sum);	
	        }
}

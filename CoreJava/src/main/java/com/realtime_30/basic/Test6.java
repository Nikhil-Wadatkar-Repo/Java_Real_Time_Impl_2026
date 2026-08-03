package com.realtime_30.basic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//6. Find the average value from a list of numbers
public class Test6 {

	public static void main(String[] args) {
		 List<Integer> numbers = Arrays.asList(5, 10, 15, 20);

	        double average= numbers.stream()
	                         .mapToInt(Integer::intValue)
	                         .average().getAsDouble();

	        System.out.println("Average: " + average);	
	        }
}

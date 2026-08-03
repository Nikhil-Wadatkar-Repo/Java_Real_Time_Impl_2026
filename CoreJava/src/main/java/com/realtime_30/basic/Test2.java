package com.realtime_30.basic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

//2. Find the second-highest and second-lowest numbers in a list
public class Test2 {

	public static void main(String[] args) {
		 List<Integer> numbers = Arrays.asList(10, 20, 5, 8, 30, 15);

	        int secondHighest = numbers.stream()
	                                   .sorted(Comparator.reverseOrder())
	                                   .skip(1)
	                                   .findFirst()
	                                   .get();

	        int secondLowest = numbers.stream()
	                                  .sorted()
	                                  .skip(1)
	                                  .findFirst()
	                                  .get();

	        System.out.println("Second Highest: " + secondHighest);
	        System.out.println("Second Lowest: " + secondLowest);

	}

}

package com.realtime_30.basic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//7. Extract all numbers greater than 10 from a list
public class Test7 {

	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(5, 12, 18, 7, 25, 3);

        List<Integer> result = numbers.stream()
                                      .filter(n -> n > 10)
                                      .collect(Collectors.toList());

        System.out.println("Numbers greater than 10: " + result);
	        }
}

package com.realtime_30.intermediate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// 20. Find the nth Largest Element	
public class Test20 {

    public static void main(String[] args) {
	 List<Integer> numbers = Arrays.asList(10, 30, 20, 50, 40, 60);
	        int n = 3; // 3rd largest

	        Optional<Integer> nthLargest = numbers.stream()
	            .sorted(Comparator.reverseOrder())
	            .skip(n - 1)
	            .findFirst();

	        System.out.println(n + "rd Largest Element: " + nthLargest);

    }

}

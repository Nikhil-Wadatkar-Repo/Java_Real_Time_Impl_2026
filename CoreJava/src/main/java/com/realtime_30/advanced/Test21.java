package com.realtime_30.advanced;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 21. From a list, remove all numbers greater than 100.
public class Test21 {

    public static void main(String[] args) {
	List<Integer> numbers = Arrays.asList(50, 120, 80, 200, 99, 101);
	List<Integer> numbersGT100 = numbers.stream()
					    .filter(num -> num > 100)
					    .collect(Collectors.toList());
	System.out.println(numbersGT100);

    }

}

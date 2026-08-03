package com.realtime_30.advanced;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 22. Given two lists of integers, find the numbers that are present in both.
public class Test22 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
	List<Integer> list2 = Arrays.asList(4, 5, 6, 7, 8);
	List<Integer> common = list1.stream().filter(list2::contains).collect(Collectors.toList());

	System.out.println("Common elements: " + common);

    }

}

package com.realtime_30.basic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//10. From a list of integers, separate even and odd numbers into two different results	
public class Test10 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);

	Map<Boolean, List<Integer>> collect = list1.stream().collect(Collectors.partitioningBy(item -> item % 2 == 0));
	System.out.println(collect.get(true));
	System.out.println(collect.get(false));
    }
}

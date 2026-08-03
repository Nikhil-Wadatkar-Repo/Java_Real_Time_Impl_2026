package com.realtime_30.intermediate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// 18. Identify the Most Frequent Number	
public class Test18 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(2, 3, 4, 5, 3);
	Long value = list1.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
		.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
	System.out.println(value);

    }

}

package com.realtime_30.intermediate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// 19. Divide a List into Even and Odd Groups	
public class Test19 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(2, 3, 4, 5, 3);
	Map<Boolean, List<Integer>> value = list1.stream().collect(Collectors.partitioningBy(ele -> ele % 2 == 0));
	System.out.println(value.get(true));

    }

}

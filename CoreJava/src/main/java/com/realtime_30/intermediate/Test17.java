package com.realtime_30.intermediate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//17. Calculate the Product of All Numbers	
public class Test17 {

    public static void main(String[] args) {
	List<Integer> list1 =  Arrays.asList(2, 3, 4, 5);
	int allMatch = list1.stream().reduce(1, (a, b) -> a * b);
	System.out.println(allMatch);

    }

}

package com.realtime_30.intermediate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//13. calculate squares of all elements	
public class Test14 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(2, 4, 6, 8, 10, 12, 14);
	list1.stream().map(ele -> ele * ele).forEach(System.out::println);

    }

}

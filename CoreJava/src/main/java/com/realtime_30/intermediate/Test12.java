package com.realtime_30.intermediate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//12. Extract the First 5 Even Numbers from a Collection	
public class Test12 {

    public static void main(String[] args) {
	List<Integer> list1 =  Arrays.asList(2, 4, 6, 8, 10, 12, 14);
	list1.stream().filter(ele -> ele % 2 == 0).limit(5).forEach(System.out::println);

    }

}

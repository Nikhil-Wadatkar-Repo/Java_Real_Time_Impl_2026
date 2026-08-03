package com.realtime_30.intermediate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//14. Calculate the Total of All Even Numbers	
public class Test13 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(2, 4, 6, 8, 10, 12, 14);
	int sum = list1.stream().filter(ele -> ele % 2 == 0).mapToInt(ele -> ele * ele).sum();
	System.out.println(sum);

    }

}

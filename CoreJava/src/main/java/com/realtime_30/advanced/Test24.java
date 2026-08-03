package com.realtime_30.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 24. Reverse the order of elements in a list of integers.
public class Test24 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(10, 20, 30, 40);

	List<Integer> collect = IntStream.rangeClosed(0, list1.size()-1).mapToObj(ele -> list1.get(list1.size() - ele -1))
		.collect(Collectors.toList());
	System.out.println(collect);
	
	
	List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> reversed = IntStream.rangeClosed(1, numbers.size())
                                          .mapToObj(i -> numbers.get(numbers.size() - i))
                                          .collect(Collectors.toList());

        System.out.println("Reversed list: " + reversed);
    }

}

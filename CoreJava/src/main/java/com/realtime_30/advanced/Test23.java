package com.realtime_30.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 23. From two lists, identify numbers that appear in one list but not in the other.
public class Test23 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(10, 20, 30, 40);
	List<Integer> list2 = Arrays.asList(30, 40, 50, 60);
	List<Integer> unique = new ArrayList<Integer>(
		list1.stream().filter(ele -> !list2.contains(ele)).collect(Collectors.toList()));
	unique.addAll(list2.stream().filter(ele -> !list1.contains(ele)).collect(Collectors.toList()));
	System.out.println(unique);

    }

}

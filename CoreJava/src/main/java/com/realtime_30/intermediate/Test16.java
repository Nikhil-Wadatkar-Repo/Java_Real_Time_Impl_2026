package com.realtime_30.intermediate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

////16. Verify Whether Any Negative Number Exists	
public class Test16 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(2, 4, 6, 8, 10, 12, -14);
	boolean allMatch = list1.stream().allMatch(ele -> ele > 0);
	System.out.println(allMatch ? "Yes" : "no");

    }

}

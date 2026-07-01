package com.mco;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test3 {

	public static void main(String[] args) {

		// finding occurrence of each character
		String str = "madam";

		Map<Character, Long> collect = str
				.chars()
				.filter(item -> item != ' ')
				.mapToObj(c -> (char) c)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		System.out.println(collect);

		// finding maximum and minimum number from a list
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 15, 6, 7, 8, 9, 10);
		Integer maxNum1 = numbers.stream().max(Comparator.comparing(Integer::valueOf)).get();
		System.out.println(maxNum1);

		OptionalInt maxNum2 = numbers.stream().mapToInt(Integer::valueOf).max();
		System.out.println(maxNum2.getAsInt());

		Integer minNum1 = numbers.stream().min(Comparator.comparing(Integer::valueOf)).get();
		System.out.println(minNum1);

		OptionalInt minNum2 = numbers.stream().mapToInt(Integer::valueOf).min();
		System.out.println(minNum2.getAsInt());
		
		//Find the Second Largest Number
//		numbers

	}

}

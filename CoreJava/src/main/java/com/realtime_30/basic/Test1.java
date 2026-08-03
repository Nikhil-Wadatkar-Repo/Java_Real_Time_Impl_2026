package com.realtime_30.basic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//1. Given a list of integers, find the maximum and minimum values
public class Test1 {

    public static void main(String[] args) {
	List<Integer> numbers = Arrays.asList(5, 12, 8, 3, 21, 7);

	byIntegerCompare(numbers);
	byComparator(numbers);
	usingMaxMinBy(numbers);
    }

    private static void usingMaxMinBy(List<Integer> numbers) {
	Optional<Integer> max = numbers.stream().collect(Collectors.maxBy(Comparator.comparing(item->item)));
	Optional<Integer> min = numbers.stream().collect(Collectors.minBy(Comparator.comparing(item->item)));
	System.out.println("Max: " + max);
	System.out.println("Min: " + min);
    }

    private static void byComparator(List<Integer> numbers) {
	Integer max = numbers.stream().max(Comparator.comparingInt(item -> Integer.valueOf(item))).get();
	Integer min = numbers.stream().min(Comparator.comparingInt(item -> Integer.valueOf(item))).get();

	System.out.println("Max: " + max);
	System.out.println("Min: " + min);
    }

    private static void byIntegerCompare(List<Integer> numbers) {
	int max = numbers.stream().max(Integer::compare).get();
	int min = numbers.stream().min(Integer::compare).get();

	System.out.println("Max: " + max);
	System.out.println("Min: " + min);
    }

}

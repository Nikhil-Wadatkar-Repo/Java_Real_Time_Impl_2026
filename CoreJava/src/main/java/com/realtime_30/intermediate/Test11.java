package com.realtime_30.intermediate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//11. From a List of Integers, Find All Prime Numbers	
public class Test11 {

    public static void main(String[] args) {
	List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
	list1.stream().filter(ele -> isPrime1(ele)).forEach(System.out::println);

    }

    public static boolean isPrime1(int num) {
	return IntStream.range(2, num - 1).allMatch(ele -> ele % num != 0);
    }

    private static boolean isPrime(int num) {
	if (num <= 1)
	    return false;
	return IntStream.rangeClosed(2, (int) Math.sqrt(num)).allMatch(n -> num % n != 0);
    }

}

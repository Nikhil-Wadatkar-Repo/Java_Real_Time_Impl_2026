package com.realtime_30.advanced;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

// 25. Given a sequence of numbers from 1 to N with one missing, find the missing number.
public class Test25 {

    public static void main(String[] args) {
	List<Integer> numbers = Arrays.asList(1, 2, 3, 5, 6);
	int num = 6;
	int expectedSum = IntStream.rangeClosed(1, num).sum();
	int actualSum = numbers.stream().mapToInt(Integer::valueOf).sum();
	System.out.println(expectedSum - actualSum);
    }
}

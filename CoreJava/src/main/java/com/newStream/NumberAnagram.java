package com.newStream;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NumberAnagram {
	public static boolean isAnagram(int num1, int num2) {
		// Convert numbers to char arrays
		char[] arr1 = String.valueOf(num1).toCharArray();
		char[] arr2 = String.valueOf(num2).toCharArray();

		// Sort using streams
//		String sorted1 = Arrays.stream(String.valueOf(num1).split(""))
//				.sorted()
//				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
//
//		String sorted2 = Arrays.stream(String.valueOf(num2).split("")).sorted()
//				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();

		String sorted1 = Arrays.stream(String.valueOf(num1).split("")).sorted().collect(Collectors.joining());

		String sorted2 = Arrays.stream(String.valueOf(num2).split("")).sorted().collect(Collectors.joining());
		return sorted1.equals(sorted2);
	}

	public static void main(String[] args) {
		int num1 = 12345;
		int num2 = 54321;
		int num3 = 12435;
		int num4 = 12344;

		System.out.println(num1 + " & " + num2 + " are anagrams? " + isAnagram(num1, num2));
		System.out.println(num1 + " & " + num3 + " are anagrams? " + isAnagram(num1, num3));
		System.out.println(num1 + " & " + num4 + " are anagrams? " + isAnagram(num1, num4));
	}
}

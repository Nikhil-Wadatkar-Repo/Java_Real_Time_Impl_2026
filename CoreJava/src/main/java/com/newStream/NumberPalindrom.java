package com.newStream;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberPalindrom {

	public static void main(String[] args) {
		int num = 1271;
		String str = String.valueOf(num);
		boolean isPalindrome = IntStream.range(0, str.length() / 2)
				.allMatch(i -> str.charAt(i) == str.charAt(str.length() - 1 - i));
		System.out.println(isPalindrome ? "Palindrome" : "Not");

		long count1 = IntStream.range(0, str.split("").length).count();
		System.out.println(count1);

		long count2 = Arrays.stream(str.split("")).count();
		System.out.println(count2);

		// reverse a String using streams
		String name = "ankur";
		String reversedString = IntStream.range(0, name.length()).mapToObj(i -> name.charAt(str.length() - i))
				.map(String::valueOf).collect(Collectors.joining());
		System.out.println(reversedString);

		// String anagram
		String str1 = "listen", str2 = "silent";

		boolean isAnagram = str1.chars().mapToObj(item -> String.valueOf((char) item)).sorted()
				.collect(Collectors.joining()).equals(str2.chars().mapToObj(item1 -> String.valueOf((char) item1))
						.sorted().collect(Collectors.joining()));
		System.out.println(isAnagram ? "Anagram" : "Not");

		// Number anagram
		int num1 = 123, num2 = 321;
		char[] arr1 = String.valueOf(num1).toCharArray();
		String arr1Str=Arrays.stream(arr1)
				.sorted()
				.mapToObj(item->String.valueOf((char)item))
				.collect(Collectors.joining());
//		System.out.println(arr1Str);)

	}
}

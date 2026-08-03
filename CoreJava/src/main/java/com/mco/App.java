package com.mco;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class App {

    public void findNonRepeatedChacters() {
// 1. Find First Non-Repeated Character in a String
	String input = "swiss";
	System.out.println(input);
	Optional<Character> findFirst = input.chars().mapToObj(c -> (char) c)
		.filter(c -> input.indexOf(c) == input.lastIndexOf(c)).findFirst();
	System.out.println(findFirst.get());
    }

// 2. Group Strings by Length
    public void groupStringByLength() {
	List<String> words = Arrays.asList("Java", "Stream", "API", "Code", "Fun");
	Map<Integer, List<String>> groupingByLength = words.stream().collect(Collectors.groupingBy(String::length));

	groupingByLength.entrySet().stream().forEach(item -> {
	    System.out.println(item.getKey() + " ==> " + item.getValue());
	});
    }

// 3. Creating one string out of list of strings
    public void combineStringFromList() {
	List<String> words = Arrays.asList("Stream", "API", "is", "powerful");
	String collect = words.stream().collect(Collectors.joining(" ", "", ""));
	System.out.println(collect);
	// OR another option
	String concatenated = words.stream().reduce("", (s1, s2) -> s1 + " " + s2).trim();
	System.out.println(concatenated);// Output: Stream API is powerful

    }

// 4. Find the Longest String
    public void findLongestString() {
	List<String> words = Arrays.asList("Stream", "API", "is", "powerful");

	// find the longest word without using max()
	String longestWithoutMax = words.stream()
		.reduce((currentLongest, word) -> currentLongest.length() >= word.length() ? currentLongest : word)
		.orElse(null);
	System.out.println("Longest word without max(): " + longestWithoutMax);

	String collect = words.stream().max(Comparator.comparingInt(item -> item.length())).orElse(null);
	System.out.println(collect);

	// another approach to find the longest word
	String collect1 = words.stream().collect(Collectors.maxBy(Comparator.comparingInt(String::length)))
		.orElse(null);
	System.out.println(collect1);

	// another approach to find the longest word
	Map<Integer, List<String>> collect2 = words.stream()
		.collect(Collectors.groupingBy(String::length, Collectors.toList()));
    }

// 5. Reverse string
    public void reverseString() {
	String name = "ankur";
	String reversedString1 = IntStream.range(0, name.length()).mapToObj(i -> name.charAt(name.length() - i - 1))
		.map(String::valueOf).collect(Collectors.joining());
	System.out.println(reversedString1);
    }

// 6. Find palindrom strings from string list
    public void palidromStringFromList() {
	List<String> words = Arrays.asList("Java", "Stream", "API", "Code", "Fun");
	// iterating each characters
	LinkedList<String> palindromList = new LinkedList<>();
	words.stream().forEach(word -> {
	    boolean isPlaindrom = IntStream.range(0, word.length())
		    .allMatch(i -> word.charAt(i) == word.charAt(word.length() - i - 1));
	    if (isPlaindrom) {
		palindromList.add(word);
	    }
	});
	System.out.println(palindromList);
	// OR
	// using reverse()
	List<String> palindromes = words.stream()
		.filter(word -> word.equalsIgnoreCase(new StringBuilder(word).reverse().toString()))
		.collect(Collectors.toList());
	System.out.println(palindromes);
    }

// 7. Find maximum occurred character from string
    public void findMaximumOccuredCharacterInString() {
	String input = "success";
	Map<Character, Long> collect = input.chars().mapToObj(c -> (char) c)
		.collect(Collectors.groupingBy(c -> c, Collectors.counting()));
	Optional<Entry<Character, Long>> maxOccuredValue = collect.entrySet().stream()
		.max(Map.Entry.comparingByValue());
	System.out.println(maxOccuredValue.get());
    }

    public void partitionStringByPalindrom() {
	List<String> words = Arrays.asList("Java", "Stream", "API", "Code", "madam");

	Map<Boolean, List<String>> partitioningByPalindrom = words.stream()
		.collect(Collectors.partitioningBy(item -> item.equals(new StringBuffer(item).reverse().toString())));
	System.out.println(partitioningByPalindrom);
    }

// 8. Find the Longest Word from a Sentence
    public void findLongestWordFromSentence() {
	String sentence = "I love my India";
	Optional<String> maxLengthWord = Arrays.stream(sentence.split(" ")).max(Comparator.comparing(String::length));
	System.out.println(maxLengthWord.get());
    }
 // 9. Find the Longest string with length
    public void findLongestStringWithLength() {
	List<Employee> employees = Employee.getEmployees();

//	Entry<String,Long> max = employees
//		.stream()
//		.collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()))
//		.entrySet()
//		.stream()
//		.max(Map.Entry.comparingByValue())
//		.get();
//	System.out.println(max);

	String sentence = "I I love my India";
	Map<Object, Integer> collect = Arrays
		.stream(sentence.split(" "))
		.distinct().map(item -> String.valueOf(item))
		.collect(Collectors.toMap(String::valueOf, String::length));
	
	Entry<Object, Integer> entry = collect
		.entrySet()
		.stream()
		.max(Map.Entry.comparingByValue())
		.get();
	
	System.out.println(entry);
    }
    
    
//10. Group a list of strings by their first character.
    public void groupListByFirstcharacter() {
	List<String> words = Arrays.asList("Java", "Stream1","Stream2","API3" ,"API", "Code", "madam");
	
	Map<Character, List<String>> groupByFirstLetter = words.stream().collect(Collectors.groupingBy(item-> item.charAt(0)));
	System.out.println(groupByFirstLetter);
    }

//11. Find the Word with Maximum Vowels
    public void findWordWithMaxVowels() {
	List<String> words = Arrays.asList("stream", "java", "programming", "awesome");
	String wordWithMostVowels = words
		.stream()
	        .max(Comparator.comparingInt(word -> (int) word.chars().filter(c -> "aeiou".indexOf(c) != -1).count()))
	        .orElse(null);
	System.out.println(wordWithMostVowels); // Output: programming

    }
//  main method
    public static void main(String[] args) {
	new App().groupListByFirstcharacter();
    }
}

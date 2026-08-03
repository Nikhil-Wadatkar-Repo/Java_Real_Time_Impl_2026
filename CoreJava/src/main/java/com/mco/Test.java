package com.mco;

public class Test {

    public static void main(String[] args) {
	String str = "swiss";
	Character character = str.chars().mapToObj(c -> (char)c)
	.filter(c -> str.indent(c) == str.lastIndexOf(c))
	.findFirst()
		.get();

    }

}

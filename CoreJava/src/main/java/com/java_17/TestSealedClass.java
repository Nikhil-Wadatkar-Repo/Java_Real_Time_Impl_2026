package com.java_17;

sealed class Vehicle permits Bike, Car, Luna {
    public void printName() {
	System.out.println("Default");
    }
}

final class Bike extends Vehicle {
    @Override
    public void printName() {
	System.out.println("Bike");
    }
}

non-sealed class Car extends Vehicle {
    @Override
    public void printName() {
	System.out.println("Car");
    }
}

final class Luna extends Vehicle {
    @Override
    public void printName() {
	System.out.println("Luna");
    }
}

// Not allowed
//class Truck extends Vehicle {   }

public class TestSealedClass {
    public static void main(String[] args) {
	String json = """
	        {
	            "name": "Alice",
	            "age": 30
	        }
	        
	        jewla ka
	        """;
	
	System.out.println(json);


    }
}

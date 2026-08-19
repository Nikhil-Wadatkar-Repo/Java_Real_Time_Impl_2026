package com;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoughClass {
    public static void main(String[] args) {
        Map<String, List<Integer>> departmentSalaries = new HashMap<>();
        departmentSalaries.put("Department 1", java.util.Arrays.asList(1,2,3,4,5));
        departmentSalaries.put("Department 2", java.util.Arrays.asList(1,2,1,2,3,4,5));
        departmentSalaries.put("Department 3", java.util.Arrays.asList(3));
        departmentSalaries.put("Department 4", java.util.Arrays.asList(4,5));

        //finding highest quantity department wise
//        departmentSalaries.entrySet().stream().max(Comparator.comparingInt())
    }
}

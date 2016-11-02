package com.example;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by architdate on 3/11/2016.
 */

public class FastAlgorithm {

    public static ArrayList<ArrayList<String>> fastPath(String[] location) {
        HashMap<String, Character> codes = new HashMap<String, Character>();
        codes.put("Marina Bay Sands", 'a');
        codes.put("Singapore Flyer", 'b');
        codes.put("Vivo City", 'c');
        codes.put("Resorts World Sentosa", 'd');
        codes.put("Buddha Tooth Relic Temple", 'e');
        codes.put("Zoo", 'f');
        codes.put("Botanic Gardens", 'g');
        codes.put("Peranakan Museum", 'h');
        codes.put("ION Orchard", 'i');

        HashMap<String, double[]> data = MapData.generateCostTimeMap();

        ArrayList<String> path = new ArrayList<String>(); 
        String places = "";
        for (String i : location) {                          //standard encoding using codes dictionary
            places = places + codes.get(i);
        }

        int index = 0;
        int len = places.length(); //original length of places to go to 
        String minSubpath = "";  //the quickly calculated path

        /*
        This code below finds out the shortest path using the following algorithm.
        Starting from the origin, it checks for the least time it can take to walk to any of the planned destinations
        Then once the end point is found, it will remove the origin from the places string
        The new origin will then be the endpoint that we previously found.
        This process will be repeated till every single destination is covered
        We can then apply the result function from MyClass.java to find out our solution
        */

        for (int j = 0; j < len; j++) {
            int minimumTime = Integer.MAX_VALUE;
            String a;
            for (int i = 0; i < places.length(); i++) {
                if (j == 0) {
                    a = "a" + String.valueOf(places.charAt(i));
                } else {
                    a = String.valueOf(path.get(j - 1).charAt(1)) + String.valueOf(places.charAt(i));
                }
                int parseTime = (int) data.get(a)[5];
                if (parseTime < minimumTime) {
                    minimumTime = parseTime;
                    minSubpath = a;
                    index = i;
                }
            }
            path.add(minSubpath);
            places = places.substring(0, index) + places.substring(index + 1);
        }
        path.add(String.valueOf(path.get(len - 1).charAt(1)) + "a");
        ArrayList<ArrayList<String>> ans = new ArrayList<ArrayList<String>>();
        ans.add(path);

        //pass the below answer as the path argument in the result function.
        return ans;
    }
}

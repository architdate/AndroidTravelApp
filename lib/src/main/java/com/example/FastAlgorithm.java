package com.example;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by test on 3/11/2016.
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
        for (String i : location) {
            places = places + codes.get(i);
        }

        int pointer = 0;
        int placeNumber = places.length();
        String minSubpath = "";

        for (int j = 0; j < placeNumber; j++) {
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
                    pointer = i;
                }
            }
            path.add(minSubpath);
            places = places.substring(0, pointer) + places.substring(pointer + 1);
        }
        path.add(String.valueOf(path.get(placeNumber - 1).charAt(1)) + "a");
        ArrayList<ArrayList<String>> ans = new ArrayList<ArrayList<String>>();
        ans.add(path);

        return ans;
    }
}

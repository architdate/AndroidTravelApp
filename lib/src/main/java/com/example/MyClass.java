package com.example;


// standard Imports

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MyClass {


    public static void main(String[] args) {  // for testing purposes only
        String[] places =new String[3];
        places[0]="Vivo City";
        places[1]="Zoo";
        places[2]="Botanic Gardens";
        System.out.println(FastAlgorithm.fastPath(places));
        System.out.println(encodePlaces(places));
//        System.out.println(possiblePaths(encodePlaces(places)));
        ArrayList<ArrayList<String>> route = possiblePaths(encodePlaces(places));
        System.out.println("/////////// slow algo //////////////");
        System.out.println(convertResultToString(result(route,30)));
        System.out.println("//////////// fast algo /////////////");
        System.out.println(convertResultToString(result(FastAlgorithm.fastPath(places),30)));

    }
    public static ArrayList<String> encodePlaces(String[] location) {   //encode the places in the form of letters
        HashMap<String, Character> codes = new HashMap<String, Character>();
        codes.put("SUTD", 'a');  //begin and end here
        codes.put("Singapore Flyer", 'b');
        codes.put("Vivo City", 'c');
        codes.put("Resorts World Sentosa", 'd');
        codes.put("Buddha Tooth Relic Temple", 'e');
        codes.put("Zoo", 'f');
        codes.put("Botanic Gardens", 'g');
        codes.put("Peranakan Museum", 'h');
        codes.put("ION Orchard", 'i');
        //add any more places to the hashmap and also update the MapData file
        String places = "";
        for (String i : location) {
            places = places + codes.get(i);
        }
        //all possible permutations of the travel places
        ArrayList<String> results = new ArrayList<String>();
        char[] chars = places.toCharArray();
        results.add(new String("" + chars[0]));
        for (int j = 1; j < chars.length; j++) {
            char c = chars[j];
            int cur_size = results.size();
            //create new permutations combing char 'c' with each of the existing permutations
            for (int i = cur_size - 1; i >= 0; i--) {
                String str = results.remove(i);
                for (int l = 0; l <= str.length(); l++) {
                    results.add(str.substring(0, l) + c + str.substring(l));
                }
            }
        }
        //start and end from the hostel
        ArrayList<String> encodedPlaces = new ArrayList<>();
        for(String s: results){
            encodedPlaces.add("a"+s+"a");
        }
        return encodedPlaces; //all possible permutations of travel routes in encoded format. Do u even dijkstra bro?
    }

    public static ArrayList<ArrayList<String>> possiblePaths(ArrayList<String> encodedPlaces){ //splitting each encoded string into an array of strings with startpoint and endpoint location
        ArrayList<ArrayList<String>> ans = new ArrayList<ArrayList<String>>();
        for(String encodedPlace: encodedPlaces){
            ArrayList<String> subPath = new ArrayList<>();
            for(int i=0;i<encodedPlace.length()-1;i++){
                subPath.add(String.valueOf(encodedPlace.charAt(i)) + String.valueOf(encodedPlace.charAt(i + 1)));
            }
            ans.add(subPath);
        }
        return ans;
    }

    private static int toBase3(int num) { //to find out the transportation mode from the minTime index
        int ret = 0, factor = 1;
        while (num > 0) {
            ret = ret + num % 3 * factor;
            num = num/3;
            factor *= 10;
        }
        return ret;
    }

    public static String[][] result(ArrayList<ArrayList<String>> ans, double budget) {
        HashMap<String,double[]> data = MapData.generateCostTimeMap();
        ArrayList<String> transport = new ArrayList<String>();
        ArrayList<Integer> minimumTime = new ArrayList<Integer>();
        ArrayList<Double> minimumCost = new ArrayList<Double>();

        for (ArrayList<String> subPath : ans) {
            int possibilities = (int) Math.pow(3.0, (double) subPath.size()); //3 transport modes for every travel string in subPath
            double[] allCosts = new double[possibilities];
            double[] allTimes = new double[possibilities];

            for (int i = 0; i < subPath.size(); i++) { //getting all costs and timings for all transport modes for one subPath using MapData
                int k = 0;

                while (k < possibilities) {

                    for (int j = 0; j < 6; j += 2) {
                        int count = 0;

                        while (count < possibilities / ((int) Math.pow(3.0, (double) (i + 1)))) {
                            allCosts[k] += data.get(subPath.get(i))[j];
                            allTimes[k] += data.get(subPath.get(i))[j + 1];
                            count++;
                            k++;
                        }
                    }
                }
            }
            //finding the least time as long as it is under budget
            double minTime = Double.MAX_VALUE;
            double minCost = 0;
            int finalIndex = 0;
            for (int j = 0; j < allTimes.length; j++) {
                if (allTimes[j] < minTime) {
                    if (allCosts[j] <= budget) {
                        minTime = allTimes[j];
                        minCost = allCosts[j];
                        finalIndex = j;
                    }
                }
            }
            //encoding transport routes as a function of the final index
            String encodedTransport = Integer.toString(toBase3(finalIndex));
            while (encodedTransport.length() < subPath.size()) {
                encodedTransport = "0" + encodedTransport;
            }
            transport.add(encodedTransport);
            minimumTime.add((int) minTime);
            minimumCost.add(minCost);
        }

        int outputTime = Integer.MAX_VALUE;
        for (int temp : minimumTime) {
            if (temp < outputTime) {
                outputTime = temp;
            }
        }
        int k = minimumTime.indexOf(outputTime); //our final index with minimum time overall!! WOOHOO!!
        //decoding the encodedTransport string
        String[] modes = {"bus", "taxi", "walk"};
        String[] transit = new String[transport.get(k).length()];
        for (int i = 0; i < transport.get(k).length(); i++) {
            transit[i] = modes[Integer.parseInt(String.valueOf((transport.get(k).charAt(i))))];
        }

        String[] costAndTime = new String[2];
        costAndTime[0] = Double.toString(minimumCost.get(k));     //cost
        costAndTime[1] = Integer.toString(outputTime);            //time

        String[][] answer = {ans.get(k).toArray(new String[ans.get(k).size()]), transit, costAndTime}; //array of path taken, mode of transit , cost and time
        return answer;
    }

    public static String convertResultToString(String[][] result){ //String representation for testing
        String ans = "Optimal Path is: " + Arrays.toString(result[0]) + "\nRespective Transit Modes are: " + Arrays.toString(result[1]) + "\nCost for the Trip is: " + result[2][0] + " dollars" + "\nTime needed for the Trip is: " + result[2][1] + " minutes";
        return ans;
    }
}

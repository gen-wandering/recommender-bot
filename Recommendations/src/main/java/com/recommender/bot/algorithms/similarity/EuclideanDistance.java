package com.recommender.bot.algorithms.similarity;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EuclideanDistance {
    private int MIN_MUTUAL_ELEMENTS;
    private int MAX_MUTUAL_ELEMENTS; // item-based = amountOfUsers; user-based = amountOfItems

    public double calculate(Map<Integer, Double> ratingsA, Map<Integer, Double> ratingsB) {
        int mutualCount = 0;
        double squareSum = 0;
        Double value;

        for (Map.Entry<Integer, Double> rating : ratingsA.entrySet()) {

            if ((value = ratingsB.get(rating.getKey())) != null) {
                squareSum += Math.pow(value - rating.getValue(), 2);
                mutualCount++;
            }
        }
        if (mutualCount < MIN_MUTUAL_ELEMENTS) return 0.0;

        double sim = ((double) mutualCount / MAX_MUTUAL_ELEMENTS) / (1 + Math.sqrt(squareSum));

        if (sim > 1) return 1;

        return sim;
    }

    public int getMIN_MUTUAL_ELEMENTS() {
        return MIN_MUTUAL_ELEMENTS;
    }

    public void setMIN_MUTUAL_ELEMENTS(int MIN_MUTUAL_ELEMENTS) {
        this.MIN_MUTUAL_ELEMENTS = MIN_MUTUAL_ELEMENTS;
    }

    public int getMAX_MUTUAL_ELEMENTS() {
        return MAX_MUTUAL_ELEMENTS;
    }

    public void setMAX_MUTUAL_ELEMENTS(int MAX_MUTUAL_ELEMENTS) {
        this.MAX_MUTUAL_ELEMENTS = MAX_MUTUAL_ELEMENTS;
    }
}
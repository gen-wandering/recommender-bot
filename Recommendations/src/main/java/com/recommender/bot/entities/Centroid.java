package com.recommender.bot.entities;

import java.util.HashMap;
import java.util.Map;

public class Centroid {
    // User-based: id - movieId
    // Item-based: id - viewerId
    //                id,      coordinate
    private final Map<Integer, Double> coordinates = new HashMap<>();

    public void putCoordinate(int id, double value) {
        coordinates.put(id, value);
    }

    public Map<Integer, Double> getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(", (");
        for (Integer id : coordinates.keySet()) {
            s.append(coordinates.get(id)).append(", ");
        }
        int start = s.lastIndexOf(", ");
        return s.replace(start, start + 2, ")").toString();
    }
}

package com.recommender.bot.service.connection;

import org.springframework.stereotype.Component;

@Component
public class PathFinder {
    private final String API_REC = "http://localhost:8080/api/recommendations";
    private final String API_DATA = "http://localhost:8080/api/data";

    public String recommendation_GET() {
        return API_REC + "/";
    }

    public String topViews_GET() {
        return API_REC + "/views";
    }

    public String topRatings_GET() {
        return API_REC + "/ratings";
    }

    public String saveOrUpdateRating_POST() {
        return API_DATA + "/rating";
    }

    public String deleteRating_DELETE() {
        return API_DATA + "/rating/";
    }

    public String deleteViewer_DELETE() {
        return API_DATA + "/viewer/";
    }
}
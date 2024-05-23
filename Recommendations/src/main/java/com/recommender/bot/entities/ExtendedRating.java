package com.recommender.bot.entities;

public class ExtendedRating {
    private int viewerId;
    private int movieId;
    private double rating;
    private int clusterNumber;

    public ExtendedRating() {
    }

    public ExtendedRating(int viewerId, int movieId, double rating, int clusterNumber) {
        this.viewerId = viewerId;
        this.movieId = movieId;
        this.rating = rating;
        this.clusterNumber = clusterNumber;
    }

    public int getViewerId() {
        return viewerId;
    }

    public int getMovieId() {
        return movieId;
    }

    public double getRating() {
        return rating;
    }

    public int getClusterNumber() {
        return clusterNumber;
    }

    public void setViewerId(int viewerId) {
        this.viewerId = viewerId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    @Override
    public String toString() {
        return "ExtendedRating{" +
                "viewerId=" + viewerId +
                ", movieId=" + movieId +
                ", rating=" + rating +
                ", clusterNumber=" + clusterNumber +
                '}';
    }
}
package com.recommender.bot.entities;

import java.io.Serializable;
import java.util.Objects;

public class RatingId implements Serializable {
    private int viewerId;
    private int movieId;

    public RatingId() {
    }

    public RatingId(int viewerId, int movieId) {
        this.viewerId = viewerId;
        this.movieId = movieId;
    }

    public int getViewerId() {
        return viewerId;
    }

    public int getMovieId() {
        return movieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return viewerId == ratingId.viewerId && movieId == ratingId.movieId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewerId, movieId);
    }

    @Override
    public String toString() {
        return "RatingId{" +
                "viewerId=" + viewerId +
                ", movieId=" + movieId +
                '}';
    }
}

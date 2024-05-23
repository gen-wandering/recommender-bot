package com.recommender.bot.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ratings", schema = "recommender_bot")
@IdClass(RatingId.class)
public class Rating {
    @Id
    @Column(name = "viewer_id")
    private int viewerId;

    @Id
    @Column(name = "movie_id")
    private int movieId;

    @Column(name = "rating")
    private double rating;

    public Rating() {
    }

    public Rating(int viewerId, int movieId, double rating) {
        this.viewerId = viewerId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public int getViewerId() {
        return viewerId;
    }

    public void setViewerId(int viewerId) {
        this.viewerId = viewerId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "{" +
                "vId=" + viewerId +
                ", mId=" + movieId +
                ", r=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating1 = (Rating) o;
        return viewerId == rating1.viewerId && movieId == rating1.movieId && Double.compare(rating1.rating, rating) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewerId, movieId, rating);
    }
}

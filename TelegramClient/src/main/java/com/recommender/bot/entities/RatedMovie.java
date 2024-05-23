package com.recommender.bot.entities;

import java.util.Objects;

public class RatedMovie {
    private Movie movie;
    private Double rating;

    public RatedMovie(Movie movie, Double rating) {
        this.movie = movie;
        this.rating = rating;
    }

    public Movie getMovie() {
        return movie;
    }

    public Double getRating() {
        return rating;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatedMovie that = (RatedMovie) o;
        return Objects.equals(movie, that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie);
    }
}

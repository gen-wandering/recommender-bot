package com.recommender.bot.algorithms.exceptions;

public class NotEnoughSimilarMoviesException extends RuntimeException {
    private final int viewerId;
    private final int movieId;

    public NotEnoughSimilarMoviesException(int viewerId, int movieId) {
        super("Not enough similar movies to make a prediction " +
                "[viewerId=" + viewerId + ", movieId=" + movieId + "]");

        this.movieId = movieId;
        this.viewerId = viewerId;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getViewerId() {
        return viewerId;
    }
}

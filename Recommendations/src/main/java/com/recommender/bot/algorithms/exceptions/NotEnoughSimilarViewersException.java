package com.recommender.bot.algorithms.exceptions;

public class NotEnoughSimilarViewersException extends RuntimeException {
    private final int viewerId;
    private final int movieId;

    public NotEnoughSimilarViewersException(int viewerId, int movieId) {
        super("Not enough similar viewers to make a prediction " +
                "[viewerId=" + viewerId + ", movieId=" + movieId + "]");

        this.viewerId = viewerId;
        this.movieId = movieId;
    }

    public int getViewerId() {
        return viewerId;
    }

    public int getMovieId() {
        return movieId;
    }
}

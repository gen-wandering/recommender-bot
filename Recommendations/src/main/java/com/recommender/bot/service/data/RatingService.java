package com.recommender.bot.service.data;

import com.recommender.bot.entities.Movie;
import com.recommender.bot.entities.Rating;
import com.recommender.bot.entities.Viewer;

import java.util.List;
import java.util.Map;

public interface RatingService {
    //  viewerId,    movieId, rating
    Map<Integer, Map<Integer, Double>> getAllRatingsGropedByViewerId();

    //  movieId,    viewerId, rating
    Map<Integer, Map<Integer, Double>> getAllRatingsGropedByMovieId();

    Integer getAverageRating();

    String saveOrUpdateRating(Rating rating);

    String removeRating(int viewerId, int movieId);

    String removeViewer(int viewerId);

    String removeMovie(int movieId);

    Integer countViewerRatings(int viewerId);

    Integer countMovieRatings(int movieId);

    Integer countRatings();

    Integer getAmountOfViewers();

    Integer getAmountOfMovies();

    Viewer getViewerWithRatings(int viewerId);

    Movie getMovieWithRating(int movieId);

    List<Integer> getAllMovieIds();

    List<Movie> getAllMoviesWithRatings();
}
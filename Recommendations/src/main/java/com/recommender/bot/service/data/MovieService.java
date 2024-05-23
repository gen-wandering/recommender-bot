package com.recommender.bot.service.data;

import com.recommender.bot.entities.Movie;

import java.util.List;

public interface MovieService {
    int saveMovie(Movie movie);

    Movie getMovie(int id);

    void deleteMovie(int movieId);

    List<Integer> getAllMovieIds();

    Integer getAmountOfMovies();

    List<Movie> getAllMoviesWithRatings();
}

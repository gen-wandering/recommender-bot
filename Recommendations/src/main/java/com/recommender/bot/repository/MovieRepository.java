package com.recommender.bot.repository;


import com.recommender.bot.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query(value = "SELECT m FROM Movie m LEFT JOIN FETCH m.ratings")
    List<Movie> getAllMoviesWithRatings();

    @Query(value = "SELECT m FROM Movie m LEFT JOIN FETCH m.ratings WHERE m.id = ?1")
    Movie getMovieWithRatings(int id);

    @Query(value = "SELECT count(*) " +
            "FROM recommendations.recommender_bot.movies",
            nativeQuery = true)
    Integer getAmountOfMovies();
}
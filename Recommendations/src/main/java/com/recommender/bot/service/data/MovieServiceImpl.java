package com.recommender.bot.service.data;

import com.recommender.bot.entities.Movie;
import com.recommender.bot.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public int saveMovie(Movie movie) {
        if (movieRepository.existsById(movie.getId()))
            return -1;
        else
            return movieRepository.save(movie).getId();
    }

    @Override
    public Movie getMovie(int id) {
        return movieRepository.getMovieWithRatings(id);
    }

    @Override
    public void deleteMovie(int movieId) {
        movieRepository.deleteById(movieId);
    }

    @Override
    public List<Integer> getAllMovieIds() {
        return movieRepository.findAll().stream().map(Movie::getId).toList();
    }

    @Override
    public Integer getAmountOfMovies() {
        return movieRepository.getAmountOfMovies();
    }

    @Override
    public List<Movie> getAllMoviesWithRatings() {
        return movieRepository.getAllMoviesWithRatings();
    }
}

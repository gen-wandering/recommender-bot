package com.recommender.bot.service.data;

import com.recommender.bot.entities.Movie;
import com.recommender.bot.repository.MovieRepository;
import com.recommender.bot.service.connection.Connection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final Connection connection;

    private List<Integer> viewsTop;
    private List<Integer> ratingsTop;

    public MovieServiceImpl(MovieRepository movieRepository,
                            Connection connection) {
        this.movieRepository = movieRepository;
        this.connection = connection;
    }

    private long lastViewsTopRequest = 0L;
    private long lastViewsRatingsTop = 0L;

    private Movie findById(int id) {
        return movieRepository.findById(id).orElse(new Movie());
    }

    private List<Integer> getViewsTop() {
        long currentTime = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
        if (currentTime - lastViewsTopRequest >= 60) {
            viewsTop = connection.getViewsTop();
            lastViewsRatingsTop = currentTime;
        }
        return viewsTop;
    }

    private List<Integer> getRatingsTop() {
        long currentTime = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
        if (currentTime - lastViewsRatingsTop >= 60) {
            ratingsTop = connection.getRatingsTop();
            lastViewsRatingsTop = currentTime;
        }
        return ratingsTop;
    }

    @Override
    public Integer getViewsTopSize() {
        return viewsTop.size();
    }

    @Override
    public Integer getRatingsTopSize() {
        return ratingsTop.size();
    }

    @Override
    public Movie getFromViewsTopByPosition(int position) {
        return findById(getViewsTop().get(position - 1));
    }

    @Override
    public Movie getFromRatingsTopByPosition(int position) {
        return findById(getRatingsTop().get(position - 1));
    }

    @Override
    public List<Movie> findAllByIds(List<Integer> ids) {
        return movieRepository.findAllById(ids);
    }
}
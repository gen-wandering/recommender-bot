package com.recommender.bot.algorithms.popularity;

import com.recommender.bot.entities.Movie;
import com.recommender.bot.entities.Rating;
import com.recommender.bot.service.data.MovieService;
import com.recommender.bot.service.data.RatingService;
import com.recommender.bot.service.recommendations.AlgorithmsConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PopularityBased {
    private final RatingService ratingService;
    private final MovieService movieService;
    private final AlgorithmsConfig algorithmsConfig;

    public PopularityBased(RatingService ratingService,
                           MovieService movieService,
                           AlgorithmsConfig algorithmsConfig) {
        this.ratingService = ratingService;
        this.movieService = movieService;
        this.algorithmsConfig = algorithmsConfig;
    }

    private List<Integer> mostRatedMovies;
    private List<Integer> mostViewedMovies;

    private int amountAtLastRankingRatings;
    private int amountAtLastRankingViews;

    private int M;
    private double VIEWS_INCREASE;
    private double RATINGS_INCREASE;

    @PostConstruct
    private void initRatings() {
        loadConfig();
        rankMostRatedMovies();
        rankMostViewedMovies();

        int currentAmountOfRatings = ratingService.countRatings();
        amountAtLastRankingRatings = currentAmountOfRatings;
        amountAtLastRankingViews = currentAmountOfRatings;
    }

    private void loadConfig() {
        M = algorithmsConfig.getMIN_RATING_AMOUNT_FOR_TOP_INCLUSION();
        VIEWS_INCREASE = algorithmsConfig.getVIEWS_INCREASE();
        RATINGS_INCREASE = algorithmsConfig.getRATINGS_INCREASE();
    }

    public List<Integer> getMostViewedMovies() {
        loadConfig();

        int currentAmountOfRatings = ratingService.countRatings();

        if (mostViewedMovies == null || mostViewedMovies.size() == 0
                || currentAmountOfRatings > amountAtLastRankingViews * (1 + VIEWS_INCREASE / 100)) {
            rankMostViewedMovies();
            amountAtLastRankingViews = currentAmountOfRatings;
        }
        return mostViewedMovies;
    }

    public List<Integer> getMostRatedMovies() {
        loadConfig();

        int currentAmountOfRatings = ratingService.countRatings();

        if (mostRatedMovies == null || mostRatedMovies.size() == 0
                || currentAmountOfRatings > amountAtLastRankingRatings * (1 + RATINGS_INCREASE / 100)) {
            rankMostRatedMovies();
            amountAtLastRankingRatings = currentAmountOfRatings;
        }
        return mostRatedMovies;
    }

    private void rankMostViewedMovies() {
        mostViewedMovies = movieService.getAllMoviesWithRatings().stream()
                .filter(movie -> movie.getRatings().size() >= M)
                .sorted((m1, m2) -> m2.getRatings().size() - m1.getRatings().size())
                .limit(100)
                .map(Movie::getId)
                .toList();
    }

    private void rankMostRatedMovies() {
        double C = ratingService.getAverageRating();

        mostRatedMovies = movieService.getAllMoviesWithRatings().stream()
                .filter(movie -> movie.getRatings().size() >= M)
                .limit(100)
                .sorted((m1, m2) -> Double.compare(retrieveRating(m2, C), retrieveRating(m1, C)))
                .map(Movie::getId)
                .toList();
    }

    private double retrieveRating(Movie movie, double C) {
        int V = movie.getRatings().size();
        double R = 0;
        for (Rating rating : movie.getRatings()) {
            R += rating.getRating();
        }
        R = R / V;

        return (R * V + C * M) / (V + M);
    }
}
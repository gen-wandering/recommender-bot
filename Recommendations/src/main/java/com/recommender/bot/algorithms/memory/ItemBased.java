package com.recommender.bot.algorithms.memory;

import com.recommender.bot.algorithms.exceptions.NotEnoughSimilarMoviesException;
import com.recommender.bot.algorithms.similarity.EuclideanDistance;
import com.recommender.bot.entities.Rating;
import com.recommender.bot.entities.RatingId;
import com.recommender.bot.service.data.RatingService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemBased implements PredictionAlgorithm {
    private final RatingService ratingService;
    private final EuclideanDistance euclideanDistance;

    public ItemBased(RatingService ratingService,
                     EuclideanDistance euclideanDistance) {
        this.ratingService = ratingService;
        this.euclideanDistance = euclideanDistance;
    }

    private double MIN_SIMILARITY_LEVEL;
    private int MIN_SIMILAR_MOVIES;
    private int MAX_SIMILAR_MOVIES;

    private int predictionExceptions;

    private double predictOneByAdjustedWeightedAverage(int viewerId, int movieId,
                                                       Map<Integer, Double> targetMovieRatings,
                                                       Map<Integer, Map<Integer, Double>> ratingsGropedByMovieId) {
        int similarMoviesCount = 0;
        double sim, simSum = 0;
        double productsSum = 0;
        double anotherMovieAvg;

        for (Map<Integer, Double> anotherMovieRatings : ratingsGropedByMovieId.values()) {

            if (!anotherMovieRatings.containsKey(viewerId)) continue;

            sim = euclideanDistance.calculate(targetMovieRatings, anotherMovieRatings);
            if (sim <= MIN_SIMILARITY_LEVEL) continue;
            else similarMoviesCount++;

            simSum += sim;
            anotherMovieAvg = anotherMovieRatings.values().stream().reduce(0.0, Double::sum) / anotherMovieRatings.size();
            productsSum += sim * (anotherMovieRatings.get(viewerId) - anotherMovieAvg);

            if (similarMoviesCount >= MAX_SIMILAR_MOVIES) break;
        }
        if (similarMoviesCount < MIN_SIMILAR_MOVIES) {
            throw new NotEnoughSimilarMoviesException(viewerId, movieId);
        }
        double currentMovieAvg = targetMovieRatings.values().stream().reduce(0.0, Double::sum) / targetMovieRatings.size();

        return currentMovieAvg + productsSum / simSum;
    }

    private List<Rating> predictAll(List<RatingId> ratingIds) {

        Map<Integer, Map<Integer, Double>> ratingsGropedByMovieId = ratingService.getAllRatingsGropedByMovieId();

        Map<Integer, List<Integer>> ratingsToPredict = ratingIds.stream().collect(
                Collectors.groupingBy(RatingId::getMovieId,
                        Collectors.mapping(RatingId::getViewerId,
                                Collectors.toList()))
        );
        List<Rating> predictedRatings = new ArrayList<>();

        double predictedRating;

        predictionExceptions = 0;

        for (Map.Entry<Integer, List<Integer>> movieRatingsToPredict : ratingsToPredict.entrySet()) {

            int movieId = movieRatingsToPredict.getKey();
            Map<Integer, Double> targetMovieRatings = ratingsGropedByMovieId.remove(movieId);

            for (int viewerId : movieRatingsToPredict.getValue()) {
                try {
                    predictedRating = predictOneByAdjustedWeightedAverage(
                            viewerId,
                            movieId,
                            targetMovieRatings,
                            ratingsGropedByMovieId
                    );
                } catch (NotEnoughSimilarMoviesException e) {
                    predictionExceptions++;
                    continue;
                }
                predictedRatings.add(new Rating(viewerId, movieId, predictedRating));
            }
            ratingsGropedByMovieId.put(movieId, targetMovieRatings);
        }
        return predictedRatings;
    }

    public List<Rating> predict(List<RatingId> ratingsToPredict) {
        return predictAll(ratingsToPredict);
    }


    public int getPredictionExceptions() {
        return predictionExceptions;
    }

    public double getMIN_SIMILARITY_LEVEL() {
        return MIN_SIMILARITY_LEVEL;
    }

    public void setMIN_SIMILARITY_LEVEL(double MIN_SIMILARITY_LEVEL) {
        this.MIN_SIMILARITY_LEVEL = MIN_SIMILARITY_LEVEL;
    }

    public int getMIN_SIMILAR_MOVIES() {
        return MIN_SIMILAR_MOVIES;
    }

    public void setMIN_SIMILAR_MOVIES(int MIN_SIMILAR_MOVIES) {
        this.MIN_SIMILAR_MOVIES = MIN_SIMILAR_MOVIES;
    }

    public int getMAX_SIMILAR_MOVIES() {
        return MAX_SIMILAR_MOVIES;
    }

    public void setMAX_SIMILAR_MOVIES(int MAX_SIMILAR_MOVIES) {
        this.MAX_SIMILAR_MOVIES = MAX_SIMILAR_MOVIES;
    }
}
package com.recommender.bot.algorithms.memory;

import com.recommender.bot.algorithms.exceptions.NotEnoughSimilarViewersException;
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
public class UserBased implements PredictionAlgorithm {
    private final RatingService ratingService;
    private final EuclideanDistance euclideanDistance;

    public UserBased(RatingService ratingService,
                     EuclideanDistance euclideanDistance) {
        this.ratingService = ratingService;
        this.euclideanDistance = euclideanDistance;
    }

    private double MIN_SIMILARITY_LEVEL;
    private int MIN_SIMILAR_VIEWERS;
    private int MAX_SIMILAR_VIEWERS;

    private int predictionExceptions;

    private double predictOneByAdjustedWeightedAverage(int viewerId, int movieId,
                                                       Map<Integer, Double> targetViewerRatings,
                                                       Map<Integer, Map<Integer, Double>> ratingsGropedByViewerId) {
        int similarViewersCount = 0;
        double sim, simSum = 0;
        double productsSum = 0;
        double anotherViewerAvg;

        for (Map<Integer, Double> anotherViewerRatings : ratingsGropedByViewerId.values()) {

            if (!anotherViewerRatings.containsKey(movieId)) continue;

            sim = euclideanDistance.calculate(targetViewerRatings, anotherViewerRatings);
            if (sim <= MIN_SIMILARITY_LEVEL) continue;
            else similarViewersCount++;

            simSum += sim;
            anotherViewerAvg = anotherViewerRatings.values().stream().reduce(0.0, Double::sum) / anotherViewerRatings.size();
            productsSum += sim * (anotherViewerRatings.get(movieId) - anotherViewerAvg);

            if (similarViewersCount >= MAX_SIMILAR_VIEWERS) break;
        }
        if (similarViewersCount < MIN_SIMILAR_VIEWERS) {
            throw new NotEnoughSimilarViewersException(viewerId, movieId);
        }
        double currentViewerAvg = targetViewerRatings.values().stream().reduce(0.0, Double::sum) / targetViewerRatings.size();

        return currentViewerAvg + productsSum / simSum;
    }

    private List<Rating> predictAll(List<RatingId> ratingIds) {

        Map<Integer, Map<Integer, Double>> ratingsGropedByViewerId = ratingService.getAllRatingsGropedByViewerId();

        Map<Integer, List<Integer>> ratingsToPredict = ratingIds.stream().collect(
                Collectors.groupingBy(RatingId::getViewerId,
                        Collectors.mapping(RatingId::getMovieId,
                                Collectors.toList()))
        );
        List<Rating> predictedRatings = new ArrayList<>();

        double predictedRating;

        predictionExceptions = 0;

        for (Map.Entry<Integer, List<Integer>> viewerRatingsToPredict : ratingsToPredict.entrySet()) {

            int viewerId = viewerRatingsToPredict.getKey();
            Map<Integer, Double> targetViewerRatings = ratingsGropedByViewerId.remove(viewerId);

            for (int movieId : viewerRatingsToPredict.getValue()) {
                try {
                    predictedRating = predictOneByAdjustedWeightedAverage(
                            viewerId,
                            movieId,
                            targetViewerRatings,
                            ratingsGropedByViewerId
                    );
                } catch (NotEnoughSimilarViewersException e) {
                    predictionExceptions++;
                    continue;
                }
                predictedRatings.add(new Rating(viewerId, movieId, predictedRating));
            }
            ratingsGropedByViewerId.put(viewerId, targetViewerRatings);
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

    public int getMIN_SIMILAR_VIEWERS() {
        return MIN_SIMILAR_VIEWERS;
    }

    public void setMIN_SIMILAR_VIEWERS(int MIN_SIMILAR_VIEWERS) {
        this.MIN_SIMILAR_VIEWERS = MIN_SIMILAR_VIEWERS;
    }

    public int getMAX_SIMILAR_VIEWERS() {
        return MAX_SIMILAR_VIEWERS;
    }

    public void setMAX_SIMILAR_VIEWERS(int MAX_SIMILAR_VIEWERS) {
        this.MAX_SIMILAR_VIEWERS = MAX_SIMILAR_VIEWERS;
    }
}
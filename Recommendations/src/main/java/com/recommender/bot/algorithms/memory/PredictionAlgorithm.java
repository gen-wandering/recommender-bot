package com.recommender.bot.algorithms.memory;

import com.recommender.bot.entities.Rating;
import com.recommender.bot.entities.RatingId;

import java.util.List;

@FunctionalInterface
public interface PredictionAlgorithm {
    List<Rating> predict(List<RatingId> ratingsToPredict);
}

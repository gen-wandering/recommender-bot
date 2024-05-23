package com.recommender.bot.controller;

import com.recommender.bot.algorithms.popularity.PopularityBased;
import com.recommender.bot.service.recommendations.Recommendations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/recommendations")
public class RecommendationsController {
    private final Recommendations recommendations;
    private final PopularityBased popularityBasedRecommender;

    public RecommendationsController(Recommendations recommendations,
                                     PopularityBased popularityBasedRecommender) {
        this.recommendations = recommendations;
        this.popularityBasedRecommender = popularityBasedRecommender;
    }

    @GetMapping("/{viewerId}")
    public List<Integer> recommend(@PathVariable int viewerId) {
        return recommendations.recommend(viewerId);
    }

    @GetMapping("/views")
    public List<Integer> getMostViewedMovies() {
        return popularityBasedRecommender.getMostViewedMovies();
    }

    @GetMapping("/ratings")
    public List<Integer> getMostRatedMovies() {
        return popularityBasedRecommender.getMostRatedMovies();
    }

    @GetMapping("/info")
    public String getAlgorithmsInformation() {
        return recommendations.getAlgorithmsInformation();
    }
}

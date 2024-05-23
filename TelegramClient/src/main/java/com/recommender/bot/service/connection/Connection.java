package com.recommender.bot.service.connection;

import com.recommender.bot.entities.Rating;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class Connection {
    private final WebClient webClient;
    private final PathFinder pathFinder;

    public Connection(WebClient webClient,
                      PathFinder pathFinder) {
        this.webClient = webClient;
        this.pathFinder = pathFinder;
    }

    public List<Integer> getViewsTop() {
        return webClient.get()
                .uri(pathFinder.topViews_GET())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Integer>>() {
                })
                .block();
    }

    public List<Integer> getRatingsTop() {
        return webClient.get()
                .uri(pathFinder.topRatings_GET())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Integer>>() {
                })
                .block();
    }

    public List<Integer> getRecommendation(int userId) {
        return webClient.get()
                .uri(pathFinder.recommendation_GET() + userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Integer>>() {
                })
                .block();
    }

    public String saveOrUpdateRating(Rating rating) {
        return webClient.post()
                .uri(pathFinder.saveOrUpdateRating_POST())
                .body(Mono.just(rating), Rating.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String deleteRating(int userId, int movieId) {
        return webClient.delete()
                .uri(pathFinder.deleteRating_DELETE() + userId + "/" + movieId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String deleteUser(int userId) {
        return webClient.delete()
                .uri(pathFinder.deleteViewer_DELETE() + userId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

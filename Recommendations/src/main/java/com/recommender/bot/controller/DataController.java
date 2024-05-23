package com.recommender.bot.controller;

import com.recommender.bot.entities.Movie;
import com.recommender.bot.entities.Rating;
import com.recommender.bot.entities.Viewer;
import com.recommender.bot.service.data.MovieService;
import com.recommender.bot.service.data.RatingService;
import com.recommender.bot.service.data.ViewerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/data")
public class DataController {
    private final MovieService movieService;
    private final ViewerService viewerService;
    private final RatingService ratingService;

    public DataController(MovieService movieService,
                          ViewerService viewerService,
                          RatingService ratingService) {
        this.movieService = movieService;
        this.viewerService = viewerService;
        this.ratingService = ratingService;
    }

    @GetMapping("/viewers")
    public List<Integer> getAllViewerIds() {
        return viewerService.getAllViewerIds();
    }

    @GetMapping("/movies")
    public List<Integer> getAllMovieIds() {
        return movieService.getAllMovieIds();
    }

    @GetMapping("/viewer/{id}")
    public Viewer getViewerRatings(@PathVariable int id) {
        return viewerService.getViewer(id);
    }

    @GetMapping("/movie/{id}")
    public Movie getMovieRatings(@PathVariable int id) {
        return movieService.getMovie(id);
    }

    @PostMapping("/rating")
    public String saveOrUpdateRating(@RequestBody Rating rating) {
        return ratingService.saveOrUpdateRating(rating);
    }

    @DeleteMapping("/rating/{viewerId}/{movieId}")
    public String deleteRating(@PathVariable int viewerId, @PathVariable int movieId) {
        return ratingService.removeRating(viewerId, movieId);
    }

    @DeleteMapping("/viewer/{id}")
    public String deleteViewer(@PathVariable int id) {
        return ratingService.removeViewer(id);
    }

    @DeleteMapping("/movie/{id}")
    public String deleteMovie(@PathVariable int id) {
        return ratingService.removeMovie(id);
    }
}
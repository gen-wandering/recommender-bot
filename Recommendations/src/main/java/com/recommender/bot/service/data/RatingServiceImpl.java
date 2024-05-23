package com.recommender.bot.service.data;

import com.recommender.bot.entities.Movie;
import com.recommender.bot.entities.Rating;
import com.recommender.bot.entities.RatingId;
import com.recommender.bot.entities.Viewer;
import com.recommender.bot.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final ViewerService viewerService;
    private final MovieService movieService;

    public RatingServiceImpl(RatingRepository ratingRepository,
                             ViewerService viewerService,
                             MovieService movieService) {
        this.ratingRepository = ratingRepository;
        this.viewerService = viewerService;
        this.movieService = movieService;
    }

    @Override
    public Map<Integer, Map<Integer, Double>> getAllRatingsGropedByViewerId() {
        return ratingRepository.findAll().stream().collect(
                Collectors.groupingBy(Rating::getViewerId,
                        Collectors.toMap(Rating::getMovieId, Rating::getRating)));
    }

    @Override
    public Map<Integer, Map<Integer, Double>> getAllRatingsGropedByMovieId() {
        return ratingRepository.findAll().stream().collect(
                Collectors.groupingBy(Rating::getMovieId,
                        Collectors.toMap(Rating::getViewerId, Rating::getRating)));
    }

    @Override
    public Integer getAverageRating() {
        return ratingRepository.getAverageRating();
    }

    @Override
    public String saveOrUpdateRating(Rating rating) {
        int viewerId = rating.getViewerId();
        int movieId = rating.getMovieId();

        String viewerRes = "viewerId=" + viewerId;
        String movieRes = "movieId=" + movieId;
        String ratingRes = "rating=";

        if (viewerService.saveViewer(new Viewer(viewerId)) == -1) {
            viewerRes = "[existed]" + viewerRes;
        } else {
            viewerRes = "[new]" + viewerRes;
        }
        if (movieService.saveMovie(new Movie(movieId)) == -1) {
            movieRes = "[existed]" + movieRes;
        } else {
            movieRes = "[new]" + movieRes;
        }

        Rating existed = ratingRepository.findById(new RatingId(viewerId, movieId)).orElse(null);

        if (existed != null) {
            if (rating.getRating() != existed.getRating()) {
                ratingRes += rating.getRating() + " [prev=" + existed.getRating() + "]";
                existed.setRating(rating.getRating());
            } else
                ratingRes += rating.getRating();
            ratingRepository.save(existed);
        } else {
            ratingRes += rating.getRating();
            ratingRepository.save(rating);
        }

        return viewerRes + "; " + movieRes + "; " + ratingRes;
    }

    @Override
    public String removeRating(int viewerId, int movieId) {

        ratingRepository.deleteById(new RatingId(viewerId, movieId));

        if (ratingRepository.countViewerRatings(viewerId) == 0) {
            viewerService.deleteViewer(viewerId);
        }
        if (ratingRepository.countMovieRatings(movieId) == 0) {
            movieService.deleteMovie(movieId);
        }
        return "rating [viewerId=" + viewerId +
                "; movieId=" + movieId + "] has been deleted";
    }

    @Override
    public String removeViewer(int id) {
        Viewer viewer = viewerService.getViewer(id);

        if (viewer != null) {
            ratingRepository.deleteAll(viewer.getRatings());
        }
        viewerService.deleteViewer(id);

        removeEmptyMovies();

        return "viewer [id=" + id + "] has been deleted";
    }

    private void removeEmptyMovies() {
        for (Movie movie : movieService.getAllMoviesWithRatings()) {
            if (movie.getRatings().size() == 0)
                movieService.deleteMovie(movie.getId());
        }
    }

    @Override
    public String removeMovie(int id) {
        Movie movie = movieService.getMovie(id);

        if (movie != null) {
            ratingRepository.deleteAll(movie.getRatings());
        }
        movieService.deleteMovie(id);

        removeEmptyViewers();

        return "movie [id=" + id + "] has been deleted";
    }

    private void removeEmptyViewers() {
        for (Viewer viewer : viewerService.getAllViewersWithRatings()) {
            if (viewer.getRatings().size() == 0)
                viewerService.deleteViewer(viewer.getId());
        }
    }

    @Override
    public Integer countViewerRatings(int id) {
        return ratingRepository.countViewerRatings(id);
    }

    @Override
    public Integer countMovieRatings(int id) {
        return ratingRepository.countMovieRatings(id);
    }

    @Override
    public Integer countRatings() {
        return ratingRepository.countRatings();
    }

    @Override
    public Integer getAmountOfViewers() {
        return viewerService.getAmountOfViewers();
    }

    @Override
    public Integer getAmountOfMovies() {
        return movieService.getAmountOfMovies();
    }

    @Override
    public Viewer getViewerWithRatings(int viewerId) {
        return viewerService.getViewer(viewerId);
    }

    @Override
    public Movie getMovieWithRating(int movieId) {
        return movieService.getMovie(movieId);
    }

    @Override
    public List<Integer> getAllMovieIds() {
        return movieService.getAllMovieIds();
    }

    @Override
    public List<Movie> getAllMoviesWithRatings() {
        return movieService.getAllMoviesWithRatings();
    }
}
package com.recommender.bot.algorithms.model;

import com.recommender.bot.entities.*;
import com.recommender.bot.repository.MovieRepository;
import com.recommender.bot.repository.ViewerRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemClustering {
    private final MovieRepository movieRepository;
    private final ViewerRepository viewerRepository;

    public ItemClustering(MovieRepository movieRepository,
                          ViewerRepository viewerRepository) {
        this.movieRepository = movieRepository;
        this.viewerRepository = viewerRepository;
    }

    private int K;
    private int MAX_ITERATIONS;

    private double sse;
    private int amountOfEmptyClusters;

    private boolean newState = false;

    private final List<ExtendedRating> extendedRatings = new ArrayList<>();
    private Map<Integer, List<ExtendedRating>> ratingsGroupedByMovies;
    private final Map<Integer, Centroid> centroids = new HashMap<>(K);
    private final Map<Integer, Movie> movies = new HashMap<>();


    public void cluster() {
        generateRandomCentroids();

        for (Movie movie : movieRepository.getAllMoviesWithRatings()) {
            for (Rating rating : movie.getRatings()) {
                extendedRatings.add(new ExtendedRating(rating.getViewerId(),
                        rating.getMovieId(),
                        rating.getRating(),
                        0)
                );
            }
        }
        for (Movie movie : movieRepository.findAll()) {
            movies.put(movie.getId(), movie);
        }
        ratingsGroupedByMovies = extendedRatings.stream().collect(Collectors.groupingBy(ExtendedRating::getMovieId));

        for (int i = 0; i < MAX_ITERATIONS; i++) {

            findClustersForMovies();

            System.out.println("Iteration: " + i);
            sse = SSE();
            System.out.println("SSE: " + sse);
            System.out.println();

            if (!newState) break;
            else newState = false;

            shiftCentroids();
        }
        movieRepository.saveAll(movies.values());

        Set<Integer> counter = new HashSet<>(K);
        ratingsGroupedByMovies.forEach((movieId, movieRating) -> counter.add(movieRating.get(0).getClusterNumber()));
        amountOfEmptyClusters = K - counter.size();
        System.out.println("Empty clusters: " + amountOfEmptyClusters);
    }

    private void generateRandomCentroids() {
        List<Viewer> viewers = viewerRepository.getAllViewersWithRatings();
        List<Double> minRatings = new ArrayList<>(viewers.size());
        List<Double> maxRatings = new ArrayList<>(viewers.size());
        Random random = new Random();

        for (Viewer viewer : viewers) {
            minRatings.add(viewer.getRatings().stream()
                    .mapToDouble(Rating::getRating)
                    .min().orElse(0.5));
            maxRatings.add(viewer.getRatings().stream()
                    .mapToDouble(Rating::getRating)
                    .max().orElse(5));
        }

        for (int i = 0; i < K; i++) {
            Centroid centroid = new Centroid();

            for (int j = 0; j < viewers.size(); j++) {
                double min = minRatings.get(j);
                double max = maxRatings.get(j);

                if (min == max)
                    centroid.putCoordinate(viewers.get(j).getId(), min);
                else
                    centroid.putCoordinate(viewers.get(j).getId(), random.nextDouble() * (max - min) + min);
            }
            centroids.put(i + 1, centroid);
        }
    }

    private double plainEuclideanDistance(List<ExtendedRating> ratings, Map<Integer, Double> coordinates) {
        double squareSum = 0;
        for (ExtendedRating rating : ratings) {
            squareSum += Math.pow(coordinates.get(rating.getViewerId()) - rating.getRating(), 2);
        }
        return Math.sqrt(squareSum);
    }

    private int findNearestCentroid(List<ExtendedRating> ratings) {
        double minimumDistance = Double.MAX_VALUE;
        int nearestCentroidNumber = 0;

        for (Map.Entry<Integer, Centroid> centroid : centroids.entrySet()) {
            double currentDistance = plainEuclideanDistance(ratings, centroid.getValue().getCoordinates());

            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearestCentroidNumber = centroid.getKey();
            }
        }
        return nearestCentroidNumber;
    }

    private void findClustersForMovies() {
        for (List<ExtendedRating> ratings : ratingsGroupedByMovies.values()) {
            int newClusterNumber = findNearestCentroid(ratings);

            if (ratings.get(0).getClusterNumber() != newClusterNumber) {
                ratings.forEach(r -> r.setClusterNumber(newClusterNumber));
                movies.get(ratings.get(0).getMovieId()).setClusterNumber(newClusterNumber);

                if (!newState) newState = true;
            }
        }
    }

    private void shiftCentroids() {
        extendedRatings.stream().collect(
                Collectors.groupingBy(ExtendedRating::getClusterNumber,
                        Collectors.groupingBy(ExtendedRating::getViewerId,
                                Collectors.averagingDouble(ExtendedRating::getRating)))
        ).forEach((clusterNumber, coordinate) ->
                coordinate.forEach((movieId, average) ->
                        centroids.get(clusterNumber).putCoordinate(movieId, average))
        );
    }

    private double SSE() {
        double sum = 0.0;
        for (List<ExtendedRating> movieRatings : ratingsGroupedByMovies.values()) {
            int cN = movieRatings.get(0).getClusterNumber();
            sum += Math.pow(plainEuclideanDistance(movieRatings, centroids.get(cN).getCoordinates()), 2);
        }
        return sum;
    }


    public void setK(int k) {
        K = k;
    }

    public void setMAX_ITERATIONS(int MAX_ITERATIONS) {
        this.MAX_ITERATIONS = MAX_ITERATIONS;
    }

    public double getSSE() {
        return sse;
    }

    public int getAmountOfEmptyClusters() {
        return amountOfEmptyClusters;
    }
}
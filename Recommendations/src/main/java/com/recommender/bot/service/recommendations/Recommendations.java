package com.recommender.bot.service.recommendations;

import com.recommender.bot.algorithms.memory.ItemBased;
import com.recommender.bot.algorithms.memory.PredictionAlgorithm;
import com.recommender.bot.algorithms.memory.UserBased;
import com.recommender.bot.algorithms.model.ItemClustering;
import com.recommender.bot.algorithms.popularity.PopularityBased;
import com.recommender.bot.algorithms.similarity.EuclideanDistance;
import com.recommender.bot.entities.Movie;
import com.recommender.bot.entities.Rating;
import com.recommender.bot.entities.RatingId;
import com.recommender.bot.entities.Viewer;
import com.recommender.bot.service.data.RatingService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class Recommendations {
    private final EuclideanDistance simFunction;
    private final UserBased userBasedPredictor;
    private final ItemBased itemBasedPredictor;
    private final ItemClustering clustering;
    private final PopularityBased popularityRecommender;

    private final AlgorithmsConfig algorithmsConfig;

    private final RatingService ratingService;

    public Recommendations(EuclideanDistance simFunction,
                           UserBased userBasedPredictor,
                           ItemBased itemBasedPredictor,
                           ItemClustering clustering,
                           PopularityBased popularityRecommender,
                           AlgorithmsConfig algorithmsConfig,
                           RatingService ratingService) {
        this.simFunction = simFunction;
        this.userBasedPredictor = userBasedPredictor;
        this.itemBasedPredictor = itemBasedPredictor;
        this.clustering = clustering;
        this.popularityRecommender = popularityRecommender;
        this.algorithmsConfig = algorithmsConfig;
        this.ratingService = ratingService;
    }

    private int FILTERING_THRESHOLD;
    private int CLUSTERING_THRESHOLD;

    private PredictionAlgorithm algorithm;

    long lastClusteringTime = 0L;

    public String getAlgorithmsInformation() {

        String info = "Prediction exceptions: ";

        if (algorithmsConfig.isITEM_BASED_FILTERING())
            info += itemBasedPredictor.getPredictionExceptions();
        else
            info += userBasedPredictor.getPredictionExceptions();

        info += "\nSSE: " + clustering.getSSE();
        info += "\nEmpty clusters: " + clustering.getAmountOfEmptyClusters();
        info += "\nHours before next clustering: ";

        long currentTime = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        long res = algorithmsConfig.getCLUSTERING_TIMER() - (currentTime - lastClusteringTime);
        if (res > 0)
            info += res;
        else
            info += ">1 hour";

        return info;
    }

    public void configure() {
        int amountOfViewers = ratingService.getAmountOfViewers();
        int amountOfMovies = ratingService.getAmountOfMovies();

        FILTERING_THRESHOLD = algorithmsConfig.getFILTERING_THRESHOLD();
        CLUSTERING_THRESHOLD = algorithmsConfig.getCLUSTERING_THRESHOLD();

        clustering.setMAX_ITERATIONS(algorithmsConfig.getMAX_ITERATIONS());
        clustering.setK(algorithmsConfig.getAMOUNT_OF_CLUSTERS());
        simFunction.setMIN_MUTUAL_ELEMENTS(algorithmsConfig.getMIN_MUTUAL_ELEMENTS());

        if (algorithmsConfig.isAUTO_FILTERING_MODE()) {
            if (amountOfMovies <= amountOfViewers) setItemBasedConfig(amountOfViewers);
            else setUserBasedConfig(amountOfMovies);
        } else {
            if (algorithmsConfig.isITEM_BASED_FILTERING()) setItemBasedConfig(amountOfViewers);
            else setUserBasedConfig(amountOfMovies);
        }
    }

    private void setUserBasedConfig(int amountOfMovies) {
        algorithmsConfig.setUSER_BASED_FILTERING(true);
        algorithmsConfig.setITEM_BASED_FILTERING(false);

        userBasedPredictor.setMIN_SIMILARITY_LEVEL(algorithmsConfig.getMIN_SIMILARITY_LEVEL());
        userBasedPredictor.setMAX_SIMILAR_VIEWERS(algorithmsConfig.getMAX_SIMILAR_VIEWERS());
        userBasedPredictor.setMIN_SIMILAR_VIEWERS(algorithmsConfig.getMIN_SIMILAR_VIEWERS());

        algorithm = userBasedPredictor;
        simFunction.setMAX_MUTUAL_ELEMENTS(amountOfMovies);
    }

    private void setItemBasedConfig(int amountOfViewers) {
        algorithmsConfig.setUSER_BASED_FILTERING(false);
        algorithmsConfig.setITEM_BASED_FILTERING(true);

        itemBasedPredictor.setMIN_SIMILARITY_LEVEL(algorithmsConfig.getMIN_SIMILARITY_LEVEL());
        itemBasedPredictor.setMAX_SIMILAR_MOVIES(algorithmsConfig.getMAX_SIMILAR_MOVIES());
        itemBasedPredictor.setMIN_SIMILAR_MOVIES(algorithmsConfig.getMIN_SIMILAR_MOVIES());

        algorithm = itemBasedPredictor;
        simFunction.setMAX_MUTUAL_ELEMENTS(amountOfViewers);
    }

    public List<Integer> recommend(int viewerId) {

        if (algorithmsConfig.hasNewConfiguration()) {
            configure();
            algorithmsConfig.setNewConfiguration(false);
        }
        long currentTime = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());

        if (lastClusteringTime == 0 || currentTime - lastClusteringTime >= algorithmsConfig.getCLUSTERING_TIMER()) {
            lastClusteringTime = currentTime;
            clustering.cluster();
        }

        Viewer viewer = ratingService.getViewerWithRatings(viewerId);
        if (viewer == null) return popularityBasedRecommendation();
        int currentViewerRatingAmount = viewer.getRatings().size();

        if (currentViewerRatingAmount < FILTERING_THRESHOLD)
            return popularityBasedRecommendation();
        else if (currentViewerRatingAmount < CLUSTERING_THRESHOLD)
            return filteringBasedRecommendation(viewer);
        else
            return clusteringBasedRecommendation(viewer);
    }

    private List<RatingId> getAllUnratedMovies(Viewer viewer) {

        List<RatingId> unratedMovies = new ArrayList<>();

        List<Integer> movieIds = ratingService.getAllMovieIds();
        Set<Integer> viewerRatings = viewer.getRatings().stream().map(Rating::getMovieId).collect(Collectors.toSet());

        movieIds.forEach(movieId -> {
            if (!viewerRatings.contains(movieId))
                unratedMovies.add(new RatingId(viewer.getId(), movieId));
        });
        return unratedMovies;
    }

    private List<Integer> popularityBasedRecommendation() {
        Random random = new Random();
        List<Integer> ratingCopy;

        if (random.nextInt() % 2 == 0)
            ratingCopy = new ArrayList<>(popularityRecommender.getMostViewedMovies());
        else {
            ratingCopy = new ArrayList<>(popularityRecommender.getMostRatedMovies());
        }
        Collections.shuffle(ratingCopy);

        return ratingCopy.stream().limit(25).toList();
    }

    private List<Integer> filteringBasedRecommendation(Viewer viewer) {
        List<RatingId> ratingsToPredict = getAllUnratedMovies(viewer);

        if (ratingsToPredict.size() == 0) {
            return new ArrayList<>();
        }
        return algorithm.predict(ratingsToPredict).stream()
                .sorted(Comparator.comparingDouble(Rating::getRating).reversed())
                .limit(50)
                .map(Rating::getMovieId)
                .toList();
    }

    private List<Integer> clusteringBasedRecommendation(Viewer viewer) {

        Set<Integer> movieIds = viewer.getRatings().stream()
                .map(Rating::getMovieId)
                .collect(Collectors.toSet());

        Map<Integer, List<Movie>> moviesGroupedByCN = ratingService.getAllMoviesWithRatings()
                .stream()
                .collect(Collectors.groupingBy(Movie::getClusterNumber));

        Map<Integer, Double> averages = new HashMap<>();

        moviesGroupedByCN.forEach((clusterNumber, movies) -> {
            for (Movie movie : movies) {
                if (movieIds.contains(movie.getId())) {
                    double average = movie.getRatings().stream()
                            .filter(rating -> rating.getViewerId() == viewer.getId())
                            .mapToDouble(Rating::getRating)
                            .average().orElse(0);
                    averages.put(clusterNumber, average);
                }
            }
        });

        List<RatingId> ratingsToPredict = getAllUnratedMovies(viewer);
        if (ratingsToPredict.size() == 0) return new ArrayList<>();

        List<Integer> recommendation = new ArrayList<>();

        for (int i = 0; recommendation.size() < 10 && i < algorithmsConfig.getAMOUNT_OF_CLUSTERS(); i++) {
            int cluster = findNextCluster(averages);
            List<Movie> toPredict = moviesGroupedByCN.get(cluster).stream()
                    .filter(movie -> ratingsToPredict.contains(new RatingId(viewer.getId(), movie.getId())))
                    .toList();
            algorithm.predict(toPredict.stream()
                            .map(movie -> new RatingId(viewer.getId(), movie.getId()))
                            .toList()).stream()
                    .sorted(Comparator.comparingDouble(Rating::getRating).reversed())
                    .limit(4)
                    .map(Rating::getMovieId)
                    .forEach(recommendation::add);
        }
        return recommendation;
    }

    public int findNextCluster(Map<Integer, Double> averages) {
        var res = averages.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue));
        if (res.isPresent()) {
            averages.remove(res.get().getKey());
            return res.get().getKey();
        }
        return -1;
    }
}
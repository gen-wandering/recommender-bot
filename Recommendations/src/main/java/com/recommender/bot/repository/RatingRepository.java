package com.recommender.bot.repository;

import com.recommender.bot.entities.Rating;
import com.recommender.bot.entities.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    @Query(value = "SELECT count(*) " +
            "FROM recommendations.recommender_bot.ratings " +
            "WHERE viewer_id = ?1",
            nativeQuery = true)
    Integer countViewerRatings(int viewerId);

    @Query(value = "SELECT count(*) " +
            "FROM recommendations.recommender_bot.ratings " +
            "WHERE movie_id = ?1",
            nativeQuery = true)
    Integer countMovieRatings(int movieId);

    @Query(value = "SELECT count(*) " +
            "FROM recommendations.recommender_bot.ratings",
            nativeQuery = true)
    Integer countRatings();

    @Query(value = "SELECT avg(rating) " +
            "FROM recommendations.recommender_bot.ratings",
            nativeQuery = true)
    Integer getAverageRating();
}

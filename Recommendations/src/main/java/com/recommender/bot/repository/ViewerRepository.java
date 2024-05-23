package com.recommender.bot.repository;


import com.recommender.bot.entities.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViewerRepository extends JpaRepository<Viewer, Integer> {
    @Query(value = "SELECT v FROM Viewer v LEFT JOIN FETCH v.ratings")
    List<Viewer> getAllViewersWithRatings();

    @Query(value = "SELECT v FROM Viewer v LEFT JOIN FETCH v.ratings WHERE v.id = ?1")
    Viewer getViewerWithRatings(int id);

    @Query(value = "SELECT count(*) " +
            "FROM recommendations.recommender_bot.viewers",
            nativeQuery = true)
    Integer getAmountOfViewers();
}
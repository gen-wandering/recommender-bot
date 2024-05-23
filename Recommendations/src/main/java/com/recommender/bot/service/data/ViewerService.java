package com.recommender.bot.service.data;

import com.recommender.bot.entities.Viewer;

import java.util.List;

public interface ViewerService {
    int saveViewer(Viewer viewer);

    Viewer getViewer(int id);

    void deleteViewer(int viewerId);

    List<Integer> getAllViewerIds();

    Integer getAmountOfViewers();

    List<Viewer> getAllViewersWithRatings();
}

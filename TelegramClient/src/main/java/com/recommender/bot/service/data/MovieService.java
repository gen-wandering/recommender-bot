package com.recommender.bot.service.data;

import com.recommender.bot.entities.Movie;

import java.util.List;

public interface MovieService {
    Integer getViewsTopSize();

    Integer getRatingsTopSize();

    Movie getFromViewsTopByPosition(int position);

    Movie getFromRatingsTopByPosition(int position);

    List<Movie> findAllByIds(List<Integer> ids);
}

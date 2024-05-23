package com.recommender.bot.service.data;

import com.recommender.bot.entities.Movie;
import com.recommender.bot.entities.User;

public interface UserService {
    boolean registerUser(Long userId);

    User findUserById(long userId);

    User setMoviesToRecommend(long userId);

    int addMovieToUser(long userId, Movie movie, double rating);

    int deleteMovieFromUser(long userId, int movieIndex);
}

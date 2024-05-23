package com.recommender.bot.service.data;

import com.recommender.bot.entities.Movie;
import com.recommender.bot.entities.RatedMovie;
import com.recommender.bot.entities.Rating;
import com.recommender.bot.entities.User;
import com.recommender.bot.repository.UserRepository;
import com.recommender.bot.service.connection.Connection;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final InnerIdService innerIdService;
    private final MovieService movieService;
    private final Connection connection;

    public UserServiceImpl(UserRepository userRepository,
                           InnerIdService innerIdService,
                           MovieService movieService,
                           Connection connection) {
        this.userRepository = userRepository;
        this.innerIdService = innerIdService;
        this.movieService = movieService;
        this.connection = connection;
    }

    private List<Integer> getRecommendation(int innerId) {
        return connection.getRecommendation(innerId);
    }

    private User setRecommendationAndSave(User user, int nextAt) {
        user.setMoviesToRecommend(movieService.findAllByIds(
                getRecommendation(user.getInnerId())
        ));
        user.setNextRecommendationAt(nextAt);
        return userRepository.save(user);
    }

    @Override
    public boolean registerUser(Long userId) {
        var targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            User user = new User(userId);
            user.setInnerId(innerIdService.next());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User findUserById(long userId) {
        return userRepository.findById(userId).orElse(new User());
    }

    @Override
    public User setMoviesToRecommend(long userId) {
        Optional<User> targetUser = userRepository.findById(userId);
        User user;
        if (targetUser.isPresent()) {
            user = targetUser.get();
        } else {
            user = new User(userId);
            user.setInnerId(innerIdService.next());
        }
        if (user.getRatedMovies() == null || user.getRatedMovies().size() < 10)
            return setRecommendationAndSave(user, 0);
        else if (user.getRatedMovies().size() >= user.getNextRecommendationAt())
            return setRecommendationAndSave(user, user.getRatedMovies().size() + 5);
        return user;
    }

    @Override
    public int addMovieToUser(long userId, Movie movie, double rating) {
        RatedMovie ratedMovie = new RatedMovie(movie, rating);

        Optional<User> targetUser = userRepository.findById(userId);
        User user;
        if (targetUser.isPresent()) {
            user = targetUser.get();
            var ratedMovies = user.getRatedMovies();
            if (ratedMovies != null) {
                int indexOfMovie = ratedMovies.indexOf(ratedMovie);
                if (indexOfMovie == -1) {
                    ratedMovies.add(ratedMovie);
                    userRepository.save(user);
                } else if (rating != -1) {
                    ratedMovies.set(indexOfMovie, ratedMovie);
                    connection.saveOrUpdateRating(new Rating(user.getInnerId(), movie.getId(), rating));
                    userRepository.save(user);
                }
            }
        } else {
            user = new User(userId);
            user.setInnerId(innerIdService.next());
            user.setRatedMovies(new ArrayList<>(List.of(ratedMovie)));
            userRepository.save(user);
        }
        return user.getRatedMovies().size();
    }

    @Override
    public int deleteMovieFromUser(long userId, int movieIndex) {
        User user = userRepository.findById(userId).orElse(new User());
        if (user.getRatedMovies().size() > movieIndex) {
            int movieId = user.getRatedMovies().remove(movieIndex).getMovie().getId();
            connection.deleteRating(user.getInnerId(), movieId);
            userRepository.save(user);
        }
        return user.getRatedMovies().size();
    }
}
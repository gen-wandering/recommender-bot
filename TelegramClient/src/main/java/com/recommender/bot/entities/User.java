package com.recommender.bot.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private Long id;

    @Field
    private Integer innerId;

    @Field
    private Integer nextRecommendationAt;

    @Field
    private List<Movie> moviesToRecommend;

    @Field
    private List<RatedMovie> ratedMovies;

    public User() {
        moviesToRecommend = new ArrayList<>();
        ratedMovies = new ArrayList<>();
    }

    public User(Long id) {
        this.id = id;
        moviesToRecommend = new ArrayList<>();
        ratedMovies = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInnerId() {
        return innerId;
    }

    public void setInnerId(Integer innerId) {
        this.innerId = innerId;
    }

    public Integer getNextRecommendationAt() {
        return nextRecommendationAt;
    }

    public void setNextRecommendationAt(Integer nextRecommendationAt) {
        this.nextRecommendationAt = nextRecommendationAt;
    }

    public List<Movie> getMoviesToRecommend() {
        return moviesToRecommend;
    }

    public void setMoviesToRecommend(List<Movie> moviesToRecommend) {
        this.moviesToRecommend = moviesToRecommend;
    }

    public List<RatedMovie> getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(List<RatedMovie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", innerId=" + innerId +
                ", moviesToRecommend=" + moviesToRecommend +
                ", ratedMovies=" + ratedMovies +
                '}';
    }
}
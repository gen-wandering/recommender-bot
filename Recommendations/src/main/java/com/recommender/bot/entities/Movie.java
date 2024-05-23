package com.recommender.bot.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "movies", schema = "recommender_bot")
public class Movie {
    @Id
    @Column
    private int id;

    @OneToMany
    @JoinColumn(name = "movie_id")
    private List<Rating> ratings;

    @Column(name = "cluster_number")
    private int clusterNumber;

    public Movie() {
    }

    public Movie(int id) {
        this.id = id;
    }

    public Movie(int id, List<Rating> ratings, int clusterNumber) {
        this.id = id;
        this.ratings = ratings;
        this.clusterNumber = clusterNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public int getClusterNumber() {
        return clusterNumber;
    }

    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", clusterNumber=" + clusterNumber +
                '}';
    }
}
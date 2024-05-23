package com.recommender.bot.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "viewers", schema = "recommender_bot")
public class Viewer {
    @Id
    @Column
    private int id;

    @OneToMany
    @JoinColumn(name = "viewer_id")
    private List<Rating> ratings;

    public Viewer() {
    }

    public Viewer(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Viewer{" +
                "id=" + id +
                '}';
    }
}

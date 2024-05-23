package com.recommender.bot.service.recommendations;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AlgorithmsConfig {
    private boolean isNewConfiguration = true;

    private boolean ITEM_BASED_FILTERING = true;
    private boolean USER_BASED_FILTERING = false;
    private boolean AUTO_FILTERING_MODE = false;

    private int MIN_MUTUAL_ELEMENTS = 2;
    private double MIN_SIMILARITY_LEVEL = 0;

    private int MIN_SIMILAR_MOVIES = 5; // test: 2
    private int MAX_SIMILAR_MOVIES = 10;
    private int MIN_SIMILAR_VIEWERS = 5; // test: 2
    private int MAX_SIMILAR_VIEWERS = 10;

    private int AMOUNT_OF_CLUSTERS = 10; // test: 2
    private int MAX_ITERATIONS = 10;
    private int CLUSTERING_TIMER = 24; // hours

    private int FILTERING_THRESHOLD = 10; // test: 4
    private int CLUSTERING_THRESHOLD = 5000; // test 5000

    private int MIN_RATING_AMOUNT_FOR_TOP_INCLUSION = 2000; // test 3
    private double VIEWS_INCREASE = 1;   // %
    private double RATINGS_INCREASE = 1; // %

    public void merge(AlgorithmsConfig algorithmsConfig) {
        if (algorithmsConfig.ITEM_BASED_FILTERING != this.ITEM_BASED_FILTERING)
            this.ITEM_BASED_FILTERING = algorithmsConfig.ITEM_BASED_FILTERING;

        if (algorithmsConfig.USER_BASED_FILTERING != this.USER_BASED_FILTERING)
            this.USER_BASED_FILTERING = algorithmsConfig.USER_BASED_FILTERING;

        if (algorithmsConfig.AUTO_FILTERING_MODE != this.AUTO_FILTERING_MODE)
            this.AUTO_FILTERING_MODE = algorithmsConfig.AUTO_FILTERING_MODE;

        if (algorithmsConfig.MIN_MUTUAL_ELEMENTS != this.MIN_MUTUAL_ELEMENTS)
            this.MIN_MUTUAL_ELEMENTS = algorithmsConfig.MIN_MUTUAL_ELEMENTS;

        if (algorithmsConfig.MIN_SIMILARITY_LEVEL != this.MIN_SIMILARITY_LEVEL)
            this.MIN_SIMILARITY_LEVEL = algorithmsConfig.MIN_SIMILARITY_LEVEL;

        if (algorithmsConfig.MIN_SIMILAR_MOVIES != this.MIN_SIMILAR_MOVIES)
            this.MIN_SIMILAR_MOVIES = algorithmsConfig.MIN_SIMILAR_MOVIES;

        if (algorithmsConfig.MAX_SIMILAR_MOVIES != this.MAX_SIMILAR_MOVIES)
            this.MAX_SIMILAR_MOVIES = algorithmsConfig.MAX_SIMILAR_MOVIES;

        if (algorithmsConfig.MIN_SIMILAR_VIEWERS != this.MIN_SIMILAR_VIEWERS)
            this.MIN_SIMILAR_VIEWERS = algorithmsConfig.MIN_SIMILAR_VIEWERS;

        if (algorithmsConfig.MAX_SIMILAR_VIEWERS != this.MAX_SIMILAR_VIEWERS)
            this.MAX_SIMILAR_VIEWERS = algorithmsConfig.MAX_SIMILAR_VIEWERS;

        if (algorithmsConfig.AMOUNT_OF_CLUSTERS != this.AMOUNT_OF_CLUSTERS)
            this.AMOUNT_OF_CLUSTERS = algorithmsConfig.AMOUNT_OF_CLUSTERS;

        if (algorithmsConfig.MAX_ITERATIONS != this.MAX_ITERATIONS)
            this.MAX_ITERATIONS = algorithmsConfig.MAX_ITERATIONS;

        if (algorithmsConfig.CLUSTERING_TIMER != this.CLUSTERING_TIMER)
            this.CLUSTERING_TIMER = algorithmsConfig.CLUSTERING_TIMER;

        if (algorithmsConfig.FILTERING_THRESHOLD != this.FILTERING_THRESHOLD)
            this.FILTERING_THRESHOLD = algorithmsConfig.FILTERING_THRESHOLD;

        if (algorithmsConfig.CLUSTERING_THRESHOLD != this.CLUSTERING_THRESHOLD)
            this.CLUSTERING_THRESHOLD = algorithmsConfig.CLUSTERING_THRESHOLD;

        if (algorithmsConfig.MIN_RATING_AMOUNT_FOR_TOP_INCLUSION != this.MIN_RATING_AMOUNT_FOR_TOP_INCLUSION)
            this.MIN_RATING_AMOUNT_FOR_TOP_INCLUSION = algorithmsConfig.MIN_RATING_AMOUNT_FOR_TOP_INCLUSION;

        if (algorithmsConfig.VIEWS_INCREASE != this.VIEWS_INCREASE)
            this.VIEWS_INCREASE = algorithmsConfig.VIEWS_INCREASE;

        if (algorithmsConfig.RATINGS_INCREASE != this.RATINGS_INCREASE)
            this.RATINGS_INCREASE = algorithmsConfig.RATINGS_INCREASE;
    }

    public boolean hasNewConfiguration() {
        return isNewConfiguration;
    }

    public void setNewConfiguration(boolean newConfiguration) {
        isNewConfiguration = newConfiguration;
    }

    public boolean isITEM_BASED_FILTERING() {
        return ITEM_BASED_FILTERING;
    }

    public void setITEM_BASED_FILTERING(boolean ITEM_BASED_FILTERING) {
        this.ITEM_BASED_FILTERING = ITEM_BASED_FILTERING;
    }

    public boolean isUSER_BASED_FILTERING() {
        return USER_BASED_FILTERING;
    }

    public void setUSER_BASED_FILTERING(boolean USER_BASED_FILTERING) {
        this.USER_BASED_FILTERING = USER_BASED_FILTERING;
    }

    public boolean isAUTO_FILTERING_MODE() {
        return AUTO_FILTERING_MODE;
    }

    public void setAUTO_FILTERING_MODE(boolean AUTO_FILTERING_MODE) {
        this.AUTO_FILTERING_MODE = AUTO_FILTERING_MODE;
    }

    public int getMIN_MUTUAL_ELEMENTS() {
        return MIN_MUTUAL_ELEMENTS;
    }

    public void setMIN_MUTUAL_ELEMENTS(int MIN_MUTUAL_ELEMENTS) {
        this.MIN_MUTUAL_ELEMENTS = MIN_MUTUAL_ELEMENTS;
    }

    public double getMIN_SIMILARITY_LEVEL() {
        return MIN_SIMILARITY_LEVEL;
    }

    public void setMIN_SIMILARITY_LEVEL(double MIN_SIMILARITY_LEVEL) {
        this.MIN_SIMILARITY_LEVEL = MIN_SIMILARITY_LEVEL;
    }

    public int getMIN_SIMILAR_MOVIES() {
        return MIN_SIMILAR_MOVIES;
    }

    public void setMIN_SIMILAR_MOVIES(int MIN_SIMILAR_MOVIES) {
        this.MIN_SIMILAR_MOVIES = MIN_SIMILAR_MOVIES;
    }

    public int getMAX_SIMILAR_MOVIES() {
        return MAX_SIMILAR_MOVIES;
    }

    public void setMAX_SIMILAR_MOVIES(int MAX_SIMILAR_MOVIES) {
        this.MAX_SIMILAR_MOVIES = MAX_SIMILAR_MOVIES;
    }

    public int getMIN_SIMILAR_VIEWERS() {
        return MIN_SIMILAR_VIEWERS;
    }

    public void setMIN_SIMILAR_VIEWERS(int MIN_SIMILAR_VIEWERS) {
        this.MIN_SIMILAR_VIEWERS = MIN_SIMILAR_VIEWERS;
    }

    public int getMAX_SIMILAR_VIEWERS() {
        return MAX_SIMILAR_VIEWERS;
    }

    public void setMAX_SIMILAR_VIEWERS(int MAX_SIMILAR_VIEWERS) {
        this.MAX_SIMILAR_VIEWERS = MAX_SIMILAR_VIEWERS;
    }

    public int getAMOUNT_OF_CLUSTERS() {
        return AMOUNT_OF_CLUSTERS;
    }

    public void setAMOUNT_OF_CLUSTERS(int AMOUNT_OF_CLUSTERS) {
        this.AMOUNT_OF_CLUSTERS = AMOUNT_OF_CLUSTERS;
    }

    public int getMAX_ITERATIONS() {
        return MAX_ITERATIONS;
    }

    public void setMAX_ITERATIONS(int MAX_ITERATIONS) {
        this.MAX_ITERATIONS = MAX_ITERATIONS;
    }

    public int getCLUSTERING_TIMER() {
        return CLUSTERING_TIMER;
    }

    public void setCLUSTERING_TIMER(int CLUSTERING_TIMER) {
        this.CLUSTERING_TIMER = CLUSTERING_TIMER;
    }

    public int getFILTERING_THRESHOLD() {
        return FILTERING_THRESHOLD;
    }

    public void setFILTERING_THRESHOLD(int FILTERING_THRESHOLD) {
        this.FILTERING_THRESHOLD = FILTERING_THRESHOLD;
    }

    public int getCLUSTERING_THRESHOLD() {
        return CLUSTERING_THRESHOLD;
    }

    public void setCLUSTERING_THRESHOLD(int CLUSTERING_THRESHOLD) {
        this.CLUSTERING_THRESHOLD = CLUSTERING_THRESHOLD;
    }

    public int getMIN_RATING_AMOUNT_FOR_TOP_INCLUSION() {
        return MIN_RATING_AMOUNT_FOR_TOP_INCLUSION;
    }

    public void setMIN_RATING_AMOUNT_FOR_TOP_INCLUSION(int MIN_RATING_AMOUNT_FOR_TOP_INCLUSION) {
        this.MIN_RATING_AMOUNT_FOR_TOP_INCLUSION = MIN_RATING_AMOUNT_FOR_TOP_INCLUSION;
    }

    public double getVIEWS_INCREASE() {
        return VIEWS_INCREASE;
    }

    public void setVIEWS_INCREASE(double VIEWS_INCREASE) {
        this.VIEWS_INCREASE = VIEWS_INCREASE;
    }

    public double getRATINGS_INCREASE() {
        return RATINGS_INCREASE;
    }

    public void setRATINGS_INCREASE(double RATINGS_INCREASE) {
        this.RATINGS_INCREASE = RATINGS_INCREASE;
    }

    @Override
    public String toString() {

        String filteringType;

        if (ITEM_BASED_FILTERING) filteringType = "ItemBased ";
        else filteringType = "UserBased ";

        if (AUTO_FILTERING_MODE) filteringType += "[auto]";
        else filteringType += "[manual]";

        return "AlgorithmsConfig:" +
                "\n\tRatings: " +
                "\n\t\tMIN_RATING_AMOUNT_FOR_TOP_INCLUSION=" + MIN_RATING_AMOUNT_FOR_TOP_INCLUSION +
                "\n\t\tVIEWS_INCREASE=" + VIEWS_INCREASE + "(%)" +
                "\n\t\tRATINGS_INCREASE=" + RATINGS_INCREASE + "(%)" +
                "\n\tFiltration: " +
                "\n\t\tFILTRATION_TYPE: " + filteringType +
                "\n\t\tFILTERING_THRESHOLD=" + FILTERING_THRESHOLD +
                "\n\t\tCLUSTERING_THRESHOLD=" + CLUSTERING_THRESHOLD +
                "\n\tSimilarity function: " +
                "\n\t\tMIN_MUTUAL_ELEMENTS=" + MIN_MUTUAL_ELEMENTS +
                "\n\t\tMIN_SIMILARITY_LEVEL=" + MIN_SIMILARITY_LEVEL +
                "\n\tItem based filtering: " +
                "\n\t\tMIN_SIMILAR_MOVIES=" + MIN_SIMILAR_MOVIES +
                "\n\t\tMAX_SIMILAR_MOVIES=" + MAX_SIMILAR_MOVIES +
                "\n\tUser based filtering: " +
                "\n\t\tMIN_SIMILAR_VIEWERS=" + MIN_SIMILAR_VIEWERS +
                "\n\t\tMAX_SIMILAR_VIEWERS=" + MAX_SIMILAR_VIEWERS +
                "\n\tClustering: " +
                "\n\t\tAMOUNT_OF_CLUSTERS=" + AMOUNT_OF_CLUSTERS +
                "\n\t\tMAX_ITERATIONS=" + MAX_ITERATIONS +
                "\n\tCLUSTERING_AND_TOP_TIMER=" + CLUSTERING_TIMER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgorithmsConfig that = (AlgorithmsConfig) o;
        return ITEM_BASED_FILTERING == that.ITEM_BASED_FILTERING &&
                USER_BASED_FILTERING == that.USER_BASED_FILTERING &&
                AUTO_FILTERING_MODE == that.AUTO_FILTERING_MODE &&
                MIN_MUTUAL_ELEMENTS == that.MIN_MUTUAL_ELEMENTS &&
                Double.compare(that.MIN_SIMILARITY_LEVEL, MIN_SIMILARITY_LEVEL) == 0 &&
                MIN_SIMILAR_MOVIES == that.MIN_SIMILAR_MOVIES &&
                MAX_SIMILAR_MOVIES == that.MAX_SIMILAR_MOVIES &&
                MIN_SIMILAR_VIEWERS == that.MIN_SIMILAR_VIEWERS &&
                MAX_SIMILAR_VIEWERS == that.MAX_SIMILAR_VIEWERS &&
                AMOUNT_OF_CLUSTERS == that.AMOUNT_OF_CLUSTERS &&
                MAX_ITERATIONS == that.MAX_ITERATIONS &&
                CLUSTERING_TIMER == that.CLUSTERING_TIMER &&
                FILTERING_THRESHOLD == that.FILTERING_THRESHOLD &&
                CLUSTERING_THRESHOLD == that.CLUSTERING_THRESHOLD &&
                MIN_RATING_AMOUNT_FOR_TOP_INCLUSION == that.MIN_RATING_AMOUNT_FOR_TOP_INCLUSION &&
                Double.compare(that.VIEWS_INCREASE, VIEWS_INCREASE) == 0 &&
                Double.compare(that.RATINGS_INCREASE, RATINGS_INCREASE) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                ITEM_BASED_FILTERING,
                USER_BASED_FILTERING,
                AUTO_FILTERING_MODE,
                MIN_MUTUAL_ELEMENTS,
                MIN_SIMILARITY_LEVEL,
                MIN_SIMILAR_MOVIES,
                MAX_SIMILAR_MOVIES,
                MIN_SIMILAR_VIEWERS,
                MAX_SIMILAR_VIEWERS,
                AMOUNT_OF_CLUSTERS,
                MAX_ITERATIONS,
                CLUSTERING_TIMER,
                FILTERING_THRESHOLD,
                CLUSTERING_THRESHOLD,
                MIN_RATING_AMOUNT_FOR_TOP_INCLUSION,
                VIEWS_INCREASE,
                RATINGS_INCREASE
        );
    }
}
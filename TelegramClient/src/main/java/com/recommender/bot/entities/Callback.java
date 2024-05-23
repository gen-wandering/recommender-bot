package com.recommender.bot.entities;

//   0   |  1  |    2    |      3
// ACTION|STATE|TURN_TYPE|CURRENT_PAGE

public class Callback {
    public static final Integer action = 0;
    public static final Integer state = 1;
    public static final Integer turn_type = 2;
    public static final Integer rating = 2;
    public static final Integer current_page = 3;

    public static class ACTION {
        public static final String IGNORE = "IGNORE";
        public static final String PAGE_TURN = "P_T";
        public static final String SAVE = "SAVE";
        public static final String GET_SCALE = "G_S";
        public static final String RATE = "RATE";
        public static final String DELETE = "DEL";
    }

    public static class STATE {
        public static final String VIEWS_TOP = "V_TOP";
        public static final String RATINGS_TOP = "R_TOP";
        public static final String RECOMMENDATION = "REC";
        public static final String MINE = "MINE";
    }
}
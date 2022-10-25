package com.jewel.entity.levelMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StarScore {
//    @JsonProperty("LevelType")
    int LevelType;
//    @JsonProperty("Star1Score")
    int Star1Score;
//    @JsonProperty("Star2Score")
    int Star2Score;
//    @JsonProperty("Star3Score")
    int Star3Score;

    public int getLevelType() {
        return LevelType;
    }

    public void setLevelType(int levelType) {
        this.LevelType = levelType;
    }

    public int getStar1Score() {
        return Star1Score;
    }

    public void setStar1Score(int star1Score) {
        this.Star1Score = star1Score;
    }

    public int getStar2Score() {
        return Star2Score;
    }

    public void setStar2Score(int star2Score) {
        this.Star2Score = star2Score;
    }

    public int getStar3Score() {
        return Star3Score;
    }

    public void setStar3Score(int star3Score) {
        this.Star3Score = star3Score;
    }
}

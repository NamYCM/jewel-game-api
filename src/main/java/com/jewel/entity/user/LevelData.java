package com.jewel.entity.user;

public class LevelData {
    int levelNumber;
    int score;
    int state;

    public LevelData() {
    }

    public LevelData(int levelNumber, int score, int state) {
        this.levelNumber = levelNumber;
        this.score = score;
        this.state = state;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

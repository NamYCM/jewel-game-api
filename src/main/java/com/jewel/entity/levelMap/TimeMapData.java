package com.jewel.entity.levelMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeMapData {
//    @JsonProperty("TargetScore")
    int TargetScore;
//    @JsonProperty("TimeInSecond")
    int TimeInSecond;

    public int getTargetScore() {
        return TargetScore;
    }

    public void setTargetScore(int targetScore) {
        this.TargetScore = targetScore;
    }

    public int getTimeInSecond() {
        return TimeInSecond;
    }

    public void setTimeInSecond(int timeInSecond) {
        this.TimeInSecond = timeInSecond;
    }
}

package com.jewel.entity.levelMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveMapData {
    @JsonProperty("MoveAmount")
    int MoveAmount;
    @JsonProperty("TargetScore")
    int TargetScore;

    public int getMoveAmount() {
        return MoveAmount;
    }

    public void setMoveAmount(int moveAmount) {
        this.MoveAmount = moveAmount;
    }

    public int getTargetScore() {
        return TargetScore;
    }

    public void setTargetScore(int targetScore) {
        this.TargetScore = targetScore;
    }
}

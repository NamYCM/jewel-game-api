package com.jewel.entity.levelMap;

import com.jewel.entity.levelMap.grid.Grid;

import java.util.HashMap;
import java.util.List;

public class LevelMap {
    List<Grid> grids;
    MoveMapData moveMapData;
    StarScore starScore;
    TimeMapData timeMapData;

    public List<Grid> getGrids() {
        return grids;
    }

    public void setGrids(List<Grid> grids) {
        this.grids = grids;
    }

    public MoveMapData getMoveMapData() {
        return moveMapData;
    }

    public void setMoveMapData(MoveMapData moveMapData) {
        this.moveMapData = moveMapData;
    }

    public StarScore getStarScore() {
        return starScore;
    }

    public void setStarScore(StarScore starScore) {
        this.starScore = starScore;
    }

    public TimeMapData getTimeMapData() {
        return timeMapData;
    }

    public void setTimeMapData(TimeMapData timeMapData) {
        this.timeMapData = timeMapData;
    }
}

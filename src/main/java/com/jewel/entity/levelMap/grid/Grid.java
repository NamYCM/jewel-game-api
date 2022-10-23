package com.jewel.entity.levelMap.grid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jewel.entity.levelMap.grid.position.Position;
import com.jewel.entity.levelMap.grid.rotation.Rotation;
import com.jewel.entity.levelMap.grid.targetGrid.TargetGrid;

import java.util.List;

public class Grid {
    @JsonProperty("InitialPieces")
    List<InitialPiece> InitialPieces;
    @JsonProperty("LossFertilityColumns")
    List<Integer> LossFertilityColumns;
    @JsonProperty("Name")
    String Name;
    @JsonProperty("Position")
    Position Position;
    @JsonProperty("Rotation")
    Rotation Rotation;
    @JsonProperty("TargetGrids")
    List<TargetGrid> TargetGrids;
    @JsonProperty("X")
    int X;
    @JsonProperty("Y")
    int Y;

    public List<InitialPiece> getInitialPieces() {
        return InitialPieces;
    }

    public void setInitialPieces(List<InitialPiece> initialPieces) {
        this.InitialPieces = initialPieces;
    }

    public List<Integer> getLossFertilityColumns() {
        return LossFertilityColumns;
    }

    public void setLossFertilityColumns(List<Integer> lossFertilityColumns) {
        this.LossFertilityColumns = lossFertilityColumns;
    }

    public List<TargetGrid> getTargetGrids() {
        return TargetGrids;
    }

    public void setTargetGrids(List<TargetGrid> targetGrids) {
        this.TargetGrids = targetGrids;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Position getPosition() {
        return Position;
    }

    public void setPosition(Position position) {
        this.Position = position;
    }

    public Rotation getRotation() {
        return Rotation;
    }

    public void setRotation(Rotation rotation) {
        this.Rotation = rotation;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        this.X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        this.Y = y;
    }
}

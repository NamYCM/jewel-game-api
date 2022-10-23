package com.jewel.entity.levelMap.grid.targetGrid;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TargetGrid {
    @JsonProperty("TargetGridName")
    String TargetGridName;
    @JsonProperty("ConnectColumns")
    List<ConnectColumn> ConnectColumns;

    public String getTargetGridName() {
        return TargetGridName;
    }

    public void setTargetGridName(String targetGridName) {
        this.TargetGridName = targetGridName;
    }

    public List<ConnectColumn> getConnectColumns() {
        return ConnectColumns;
    }

    public void setConnectColumns(List<ConnectColumn> connectColumns) {
        this.ConnectColumns = connectColumns;
    }
}

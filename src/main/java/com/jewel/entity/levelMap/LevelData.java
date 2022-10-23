package com.jewel.entity.levelMap;

import java.util.HashMap;

public class LevelData {
    int version;
    HashMap<Integer, LevelMap> maps;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public HashMap<Integer, LevelMap> getMaps() {
        return maps;
    }

    public void setMaps(HashMap<Integer, LevelMap> maps) {
        this.maps = maps;
    }
}

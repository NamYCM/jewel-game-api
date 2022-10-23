package com.jewel.entity.user;

import com.jewel.entity.PieceType;

import java.util.HashMap;
import java.util.Map;

public class User {
    String token;
    String username;
    String password;
    int currentLevel = 1;
    int money = 0;
    Map<String, Integer> specialPiecesAmount;
    Map<String, LevelData> levelDatas;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        levelDatas = new HashMap<>();

        specialPiecesAmount = new HashMap<>();
        specialPiecesAmount.put(PieceType.BOMB.toString(), 0);
        specialPiecesAmount.put(PieceType.COLUMN_CLEAR.toString(), 0);
        specialPiecesAmount.put(PieceType.RAINBOW.toString(), 0);
        specialPiecesAmount.put(PieceType.ROW_CLEAR.toString(), 0);
    }

    public Map<String, LevelData> getLevelDatas() {
        return levelDatas;
    }

    public void setLevelDatas(Map<String, LevelData> levelDatas) {
        this.levelDatas = levelDatas;
    }

    public Map<String, Integer> getSpecialPiecesAmount() {
        return specialPiecesAmount;
    }

    public void setSpecialPiecesAmount(Map<String, Integer> specialPiecesAmount) {
        this.specialPiecesAmount = specialPiecesAmount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}

package com.jewel.entity.levelMap.grid.position;

public class Normalized {
    float magnitude;
    float sqrMagnitude;
    float x;
    float y;
    float z;
    Normalized normalized;

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public float getSqrMagnitude() {
        return sqrMagnitude;
    }

    public void setSqrMagnitude(float sqrMagnitude) {
        this.sqrMagnitude = sqrMagnitude;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Normalized getNormalized() {
        return normalized;
    }

    public void setNormalized(Normalized normalized) {
        this.normalized = normalized;
    }
}

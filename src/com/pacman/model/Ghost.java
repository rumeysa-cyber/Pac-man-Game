package com.pacman.model;

public class Ghost {

    private double x;
    private double y;
    private double speed;
    private double size;

    private boolean movingRight;

    public Ghost(double x, double y, double speed, double size) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.size = size;
        this.movingRight = true;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
    public double getSize() {
        return size;
    }
    public void setSize(double size) {
        this.size = size;
    }
}
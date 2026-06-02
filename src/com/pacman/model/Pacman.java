package com.pacman.model;

public class Pacman {

    private double x;
    private double y;
    private double size;
    private double speed;

    public double getSpeed() {
        return speed;
    }

    public Pacman(double x, double y, double size, double speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    public void moveUp() {
        y -= speed;
    }

    public void moveDown() {
        y += speed;
    }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
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

    public double getSize() {
        return size;
    }
}
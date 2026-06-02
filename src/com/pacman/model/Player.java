package com.pacman.model;

public class Player {

    private String username;
    private int score;
    private int lives;

    public Player(String username) {
        this.username = username;
        this.score = 0;
        this.lives = 3;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        lives--;
    }
}
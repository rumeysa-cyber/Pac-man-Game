package com.pacman.model;

public class GameMap {

    private final int tileSize = 70;

    public int getTileSize() {
        return tileSize;
    }
    public boolean isWall(double x, double y) {
        int col = (int) (x / tileSize);
        int row = (int) (y / tileSize);

        if (row < 0 || row >= map.length || col < 0 || col >= map[0].length) {
            return true;
        }

        return map[row][col] == 1;
    }
    public int getRowCount() {
        return map.length;
    }

    public int getColumnCount() {
        return map[0].length;
    }

    public int getMapWidth() {
        return getColumnCount() * tileSize;
    }

    public int getMapHeight() {
        return getRowCount() * tileSize;
    }

    private final int[][] map = {
            {1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,0,1,1,1,0,1},
            {1,0,0,0,0,0,0,1,0,1},
            {1,1,1,1,1,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1}
    };

    public int[][] getMap() {
        return map;
    }
    public boolean hasPellet(double x, double y) {
        int col = (int) (x / tileSize);
        int row = (int) (y / tileSize);

        if (row < 0 || row >= map.length || col < 0 || col >= map[0].length) {
            return false;
        }

        return map[row][col] == 0;
    }



    public void eatPellet(double x, double y) {
        int col = (int) (x / tileSize);
        int row = (int) (y / tileSize);

        if (row >= 0 && row < map.length && col >= 0 && col < map[0].length) {
            if (map[row][col] == 0) {
                map[row][col] = 2;
            }
        }
    }

    public boolean hasRemainingPellets() {

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {

                if (map[row][col] == 0) {
                    return true;
                }
            }
        }

        return false;
    }
}
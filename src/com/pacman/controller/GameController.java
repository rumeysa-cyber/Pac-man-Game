package com.pacman.controller;
import com.pacman.Main;
import com.pacman.model.Player;
import com.pacman.model.Pacman;
import com.pacman.model.GameMap;
import com.pacman.model.Ghost;
import com.pacman.utils.FileManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;


public class GameController {

    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label livesLabel;

    @FXML
    private Label levelLabel;

    private String direction = "RIGHT";
    private String username;
    private int score = 0;
    private int lives = 3;
    private int level = 1;
    private boolean gameOverShown = false;
    private AnimationTimer timer;
    private Player player;
    private Pacman pacman;
    private GameMap gameMap;
    private boolean up, down, left, right;
    private Ghost ghost;
    private long lastHitTime = 0;
    private final long hitCooldown = 2_000_000_000L; // 2 seconds
    private boolean gameRunning = true;
    public void setUsername(String username) {

        this.username = username;

        player = new Player(username);
        pacman = new Pacman(90, 90, 40, 3);
        ghost = new Ghost(420, 90, 2, 40);
        gameMap = new GameMap();

        usernameLabel.setText("Player: " + player.getUsername());
        gameCanvas.widthProperty().bind(gameCanvas.getScene().widthProperty());
        gameCanvas.heightProperty().bind(gameCanvas.getScene().heightProperty().subtract(50));
        gameCanvas.getScene().setOnKeyPressed(event -> {

            switch (event.getCode()) {

                case UP:
                    up = true;
                    direction = "UP";
                    break;

                case DOWN:
                    down = true;
                    direction = "DOWN";
                    break;

                case LEFT:
                    left = true;
                    direction = "LEFT";
                    break;

                case RIGHT:
                    right = true;
                    direction = "RIGHT";
                    break;
            }
        });


        gameCanvas.getScene().setOnKeyReleased(event -> {

            switch (event.getCode()) {

                case UP:
                    up = false;
                    break;

                case DOWN:
                    down = false;
                    break;

                case LEFT:
                    left = false;
                    break;

                case RIGHT:
                    right = false;
                    break;
            }
        });

        startGameLoop();
    }


    @FXML
    private void initialize() {
        // Game objects are created after login in setUsername()
    }


    private void drawGameScreen() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gc.setFill(Color.RED);

        gc.setFill(Color.RED);

        gc.fillRoundRect(
                ghost.getX(),
                ghost.getY(),
                ghost.getSize(),
                ghost.getSize(),
                10,
                10
        );

        gc.setFill(Color.WHITE);

        gc.fillOval(
                ghost.getX() + 8,
                ghost.getY() + 8,
                8,
                8
        );

        gc.fillOval(
                ghost.getX() + 20,
                ghost.getY() + 8,
                8,
                8
        );

        gc.setFill(Color.BLACK);

        gc.fillOval(
                ghost.getX() + 10,
                ghost.getY() + 10,
                4,
                4
        );

        gc.fillOval(
                ghost.getX() + 22,
                ghost.getY() + 10,
                4,
                4
        );
        gc.setFill(Color.YELLOW);

        double startAngle = 30;

        if (direction.equals("RIGHT")) {
            startAngle = 30;
        } else if (direction.equals("LEFT")) {
            startAngle = 210;
        } else if (direction.equals("UP")) {
            startAngle = 120;
        } else if (direction.equals("DOWN")) {
            startAngle = 300;
        }

        gc.fillArc(
                pacman.getX(),
                pacman.getY(),
                pacman.getSize(),
                pacman.getSize(),
                startAngle,
                300,
                ArcType.ROUND
        );


        gc.setFill(Color.WHITE);
        gc.setFill(Color.BLUE);

        int[][] map = gameMap.getMap();

        for (int row = 0; row < map.length; row++) {

            for (int col = 0; col < map[row].length; col++) {

                if (map[row][col] == 1) {

                    gc.setFill(Color.BLUE);

                    gc.fillRect(
                            col * gameMap.getTileSize(),
                            row * gameMap.getTileSize(),
                            gameMap.getTileSize(),
                            gameMap.getTileSize()
                    );
                }

                else if (map[row][col] == 0) {

                    gc.setFill(Color.WHITE);

                    gc.fillOval(
                            col * gameMap.getTileSize()
                                    + gameMap.getTileSize() / 2.0 - 5,

                            row * gameMap.getTileSize()
                                    + gameMap.getTileSize() / 2.0 - 5,

                            10,
                            10
                    );
                }
            }
        }
    }


    private void startGameLoop() {
        if (!gameRunning) {
            return;
        }
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (!gameRunning) {
                    return;
                }

                movePacman();
                moveGhost();
                checkGhostCollision();
                drawGameScreen();
            }
        };

        timer.start();
    }

    private void movePacman() {

        double newX = pacman.getX();
        double newY = pacman.getY();

        if (left) {
            newX -= pacman.getSpeed();
        }
        if (right) {
            newX += pacman.getSpeed();
        }

        if (!isCollidingWithWall(newX, pacman.getY())) {
            pacman.setX(newX);
        }

        if (up) {
            newY -= pacman.getSpeed();
        }
        if (down) {
            newY += pacman.getSpeed();
        }

        if (!isCollidingWithWall(pacman.getX(), newY)) {
            pacman.setY(newY);
            checkPelletCollision();
        }

    }

    private void moveGhost() {

        double newX = ghost.getX();

        if (ghost.isMovingRight()) {
            newX += ghost.getSpeed();

            if (newX > 600 || isGhostCollidingWithWall(newX, ghost.getY())) {
                ghost.setMovingRight(false);
            } else {
                ghost.setX(newX);
            }

        } else {
            newX -= ghost.getSpeed();

            if (newX < 90 || isGhostCollidingWithWall(newX, ghost.getY())) {
                ghost.setMovingRight(true);
            } else {
                ghost.setX(newX);
            }
        }
    }
    private boolean isGhostCollidingWithWall(double x, double y) {

        double size = ghost.getSize();

        boolean topLeft = gameMap.isWall(x, y);
        boolean topRight = gameMap.isWall(x + size - 1, y);
        boolean bottomLeft = gameMap.isWall(x, y + size - 1);
        boolean bottomRight = gameMap.isWall(x + size - 1, y + size - 1);

        return topLeft || topRight || bottomLeft || bottomRight;
    }
    private void checkGhostCollision() {
        long now = System.nanoTime();

        if (now - lastHitTime < hitCooldown) {
            return;
        }
        if (!gameRunning || gameOverShown) {
            return;
        }
        double dx = pacman.getX() - ghost.getX();
        double dy = pacman.getY() - ghost.getY();

        double distance =
                Math.sqrt(dx * dx + dy * dy);

        if (distance < 35) {
            lastHitTime = now;
            lives--;
            if (lives <= 0) {

                lives = 0;

                livesLabel.setText("Lives: 0");

                gameOver();

                return;
            }
            livesLabel.setText(
                    "Lives: " + lives
            );

            pacman.setX(90);
            pacman.setY(90);

            if (lives <= 0) {
                gameOver();
            }
        }
    }
    private boolean isCollidingWithWall(double x, double y) {

        double size = pacman.getSize();

        boolean topLeft = gameMap.isWall(x, y);
        boolean topRight = gameMap.isWall(x + size - 1, y);
        boolean bottomLeft = gameMap.isWall(x, y + size - 1);
        boolean bottomRight = gameMap.isWall(x + size - 1, y + size - 1);

        return topLeft || topRight || bottomLeft || bottomRight;
    }

    private void checkPelletCollision() {
        double centerX = pacman.getX() + pacman.getSize() / 2;
        double centerY = pacman.getY() + pacman.getSize() / 2;

        if (gameMap.hasPellet(centerX, centerY)) {

            gameMap.eatPellet(centerX, centerY);
            player.addScore(10);
            scoreLabel.setText("Score: " + player.getScore());

            if (!gameMap.hasRemainingPellets()) {
                nextLevel();
            }
        }
    }
    private void nextLevel() {

        gameRunning = false;

        level++;
        levelLabel.setText("Level: " + level);

        if (timer != null) {
            timer.stop();
        }

        Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Level Up");
            alert.setHeaderText("🎉 LEVEL " + level + " 🎉");

            alert.setContentText(
                    "Great job!\n\n" +
                            "You cleared all pellets.\n" +
                            "Ghost speed increased!\n\n" +
                            "Current Score: " + player.getScore()
            );

            alert.getDialogPane().setStyle(
                    "-fx-background-color: #111111;"
            );

            alert.getDialogPane().lookup(".content.label").setStyle(
                    "-fx-text-fill: white; -fx-font-size: 15px;"
            );

            alert.getDialogPane().lookup(".header-panel").setStyle(
                    "-fx-background-color: #FFD700;"
            );

            alert.showAndWait();

            pacman.setX(90);
            pacman.setY(90);

            ghost.setX(420);
            ghost.setY(90);
            ghost.setMovingRight(true);
            ghost.setSpeed(ghost.getSpeed() + 1);

            gameMap = new GameMap();

            gameRunning = true;
            gameOverShown = false;

            startGameLoop();
        });
    }

    private void gameOver() {

        if (gameOverShown) {
            return;
        }

        gameOverShown = true;
        gameRunning = false;

        if (timer != null) {
            timer.stop();
        }

        FileManager.saveScore(
                player.getUsername(),
                player.getScore(),
                level
        );

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Game Over");
            alert.setHeaderText("💀 GAME OVER 💀");

            alert.setContentText(
                    "Player: " + player.getUsername() + "\n" +
                            "Final Score: " + player.getScore() + "\n" +
                            "Level Reached: " + level + "\n\n" +
                            "Your score has been saved."
            );

            alert.getDialogPane().setStyle(
                    "-fx-background-color: #111111;"
            );

            alert.getDialogPane().lookup(".content.label").setStyle(
                    "-fx-text-fill: white; -fx-font-size: 15px;"
            );

            alert.getDialogPane().lookup(".header-panel").setStyle(
                    "-fx-background-color: #B22222;"
            );

            alert.showAndWait();

            try {
                FXMLLoader loader =
                        new FXMLLoader(Main.class.getResource("menu.fxml"));

                Scene scene =
                        new Scene(loader.load(), 700, 540);

                Stage stage =
                        (Stage) gameCanvas.getScene().getWindow();

                stage.setScene(scene);
                stage.setTitle("Pac-Man Game");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
package com.pacman.controller;
import com.pacman.utils.FileManager;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void register() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter username and password.");
            return;
        }

        boolean success = FileManager.registerUser(username, password);

        if (success) {
            messageLabel.setStyle("-fx-text-fill: lightgreen;");
            messageLabel.setText("Registration successful. Please login.");
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Username already exists.");
        }
    }

    @FXML
    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter username and password.");
            return;
        }

        boolean success = FileManager.loginUser(username, password);

        if (success) {
            openGameScreen(username);
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Invalid username or password.");
        }
    }

    private void openGameScreen(String username) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(com.pacman.Main.class.getResource("game.fxml"));
            Scene scene = new Scene(loader.load(), 700, 540);

            GameController gameController = loader.getController();
            gameController.setUsername(username);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pac-Man Game - " + username);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showHighScores() {

        String scores = FileManager.readScores();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText("High Scores");
        alert.setContentText(scores);

        alert.showAndWait();
    }

    @FXML
    private void exitGame() {
        System.exit(0);
    }
}
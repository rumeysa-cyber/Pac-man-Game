package com.pacman.utils;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.*;

public class FileManager {

    private static final String USERS_FILE = "users.txt";
    private static final String SCORES_FILE = "scores.txt";

    public static boolean registerUser(String username, String password) {
        if (userExists(username)) {
            return false;
        }

        try (FileWriter writer = new FileWriter(USERS_FILE, true)) {
            writer.write(username + "," + password + "\n");
            return true;
        } catch (IOException e) {
            System.out.println("User could not be registered.");
            return false;
        }
    }

    public static boolean loginUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 2) {
                    String savedUsername = parts[0];
                    String savedPassword = parts[1];

                    if (savedUsername.equals(username) && savedPassword.equals(password)) {
                        return true;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Users file could not be read.");
        }

        return false;
    }

    public static boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true;
                }
            }

        } catch (IOException e) {
            return false;
        }

        return false;
    }

    public static void saveScore(String username, int score, int level) {

        try (FileWriter writer = new FileWriter("scores.txt", true)) {

            writer.write(username + "," + score + "," + level + "\n");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public static String readScores() {

        StringBuilder result = new StringBuilder();

        try {

            List<String> lines = Files.readAllLines(Paths.get("scores.txt"));

            lines.removeIf(line -> line.trim().isEmpty() || line.split(",").length < 3);

            lines.sort((a, b) -> {
                try {
                    int s1 = Integer.parseInt(a.split(",")[1].trim());
                    int s2 = Integer.parseInt(b.split(",")[1].trim());

                    return Integer.compare(s2, s1);
                } catch (Exception e) {
                    return 0;
                }
            });

            int count = 0;

            for (String line : lines) {

                if (count >= 10) {
                    break;
                }

                String[] parts = line.split(",");

                result.append("#")
                        .append(count + 1)
                        .append("  ")
                        .append(parts[0])
                        .append(" | Score: ")
                        .append(parts[1])
                        .append(" | Level: ")
                        .append(parts[2])
                        .append("\n");

                count++;
            }

        } catch (IOException e) {

            result.append("No score records found.");
        }

        if (result.length() == 0) {
            result.append("No scores yet.");
        }

        return result.toString();
    }
}
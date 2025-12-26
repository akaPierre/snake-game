package com.snakegame;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HighScoreManager {

    private static final String FILE_NAME = "highscore.txt";

    public int loadHighScore() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return 0;
        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            // Ignore and return 0
        }
        return 0;
    }

    public void saveHighScore(int highScore) {
        File file = new File(FILE_NAME);
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(Integer.toString(highScore));
            writer.newLine();
        } catch (IOException e) {
            // Ignore write errors for this simple game
        }
    }
}
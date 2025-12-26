package com.snakegame;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {

    private Clip eatClip;
    private Clip gameOverClip;
    private Clip highScoreClip;

    public SoundManager() {
        eatClip = loadClip("/sounds/eat.wav");
        gameOverClip = loadClip("/sounds/gameover.wav");
        highScoreClip = loadClip("/sounds/highscore.wav");
    }

    private Clip loadClip(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Sound not found: " + path);
                return null;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Failed to load sound " + path + ": " + e.getMessage());
            return null;
        }
    }

    private void playClip(Clip clip) {
        if (clip == null) return;
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void playEat() {
        playClip(eatClip);
    }

    public void playGameOver() {
        playClip(gameOverClip);
    }

    public void playHighScore() {
        playClip(highScoreClip);
    }
}
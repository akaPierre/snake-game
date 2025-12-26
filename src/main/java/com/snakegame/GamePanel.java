package com.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    // Sound Manager
    private final SoundManager soundManager = new SoundManager();

    // Game State Enum and Fields
    private enum GameState {
        MENU,
        RUNNING,
        PAUSED,
        GAME_OVER
    }

    private GameState gameState = GameState.MENU;
    // High score
    private final HighScoreManager highScoreManager = new HighScoreManager();
    private int highScore = highScoreManager.loadHighScore();

    // Speed / difficulty
    private int baseDelay = 140;      // initial speed in ms
    private int currentDelay = baseDelay;
    private int level = 1;
    private final int SCORE_PER_LEVEL = 50;   // every 50 points -> faster
    private final int MIN_DELAY = 60;         // cap maximum speed

    // Pause state
    private boolean paused = false;

    // Board settings
    private static final int TILE_SIZE = 25;
    private static final int GRID_WIDTH = 24;   // 24 * 25 = 600
    private static final int GRID_HEIGHT = 24;  // 24 * 25 = 600
    private static final int PANEL_WIDTH = GRID_WIDTH * TILE_SIZE;
    private static final int PANEL_HEIGHT = GRID_HEIGHT * TILE_SIZE;

    // Game state
    private final List<Point> snake = new ArrayList<>();
    private Point food;
    private char direction = 'R'; // U, D, L, R
    private boolean running = false;
    private boolean gameOver = false;
    private int score = 0;

    private Timer timer;
    private final Random random = new Random();

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
    }

    public void startGame() {
        snake.clear();
        int startX = GRID_WIDTH / 2;
        int startY = GRID_HEIGHT / 2;
        snake.add(new Point(startX, startY));
        snake.add(new Point(startX - 1, startY));
        snake.add(new Point(startX - 2, startY));

        direction = 'R';
        score = 0;
        level = 1;
        paused = false;
        gameOver = false;
        gameState = GameState.RUNNING;

        currentDelay = baseDelay;

        spawnFood();

        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(currentDelay, this);
        running = true;
        timer.start();
    }

    private void spawnFood() {
        while (true) {
            int x = random.nextInt(GRID_WIDTH);
            int y = random.nextInt(GRID_HEIGHT);
            Point p = new Point(x, y);
            if (!snake.contains(p)) {
                food = p;
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw grid (optional)
        g.setColor(Color.DARK_GRAY);
        for (int x = 0; x < PANEL_WIDTH; x += TILE_SIZE) {
            g.drawLine(x, 0, x, PANEL_HEIGHT);
        }
        for (int y = 0; y < PANEL_HEIGHT; y += TILE_SIZE) {
            g.drawLine(0, y, PANEL_WIDTH, y);
        }

        // Draw snake
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            if (i == 0) {
                g.setColor(Color.GREEN.brighter());
            } else {
                g.setColor(Color.GREEN.darker());
            }
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw food
        if (food != null) {
            g.setColor(Color.RED);
            g.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw HUD: score + level + pause hint
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 18));
        String hud = "Score: " + score +
                "   High: " + highScore +
                "   Level: " + level +
                "   [P]ause";
        g.drawString(hud, 10, 20);

        // MENU screen
        if (gameState == GameState.MENU) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            String title = "SNAKE GAME";
            int tw = g.getFontMetrics().stringWidth(title);
            g.drawString(title, (PANEL_WIDTH - tw) / 2, PANEL_HEIGHT / 2 - 80);

            g.setFont(new Font("Consolas", Font.PLAIN, 24));
            String line1 = "1 - Easy";
            String line2 = "2 - Normal";
            String line3 = "3 - Hard";
            String line4 = "Use Arrow Keys or WASD";
            int y = PANEL_HEIGHT / 2 - 20;
            int x1 = (PANEL_WIDTH - g.getFontMetrics().stringWidth(line1)) / 2;
            int x2 = (PANEL_WIDTH - g.getFontMetrics().stringWidth(line2)) / 2;
            int x3 = (PANEL_WIDTH - g.getFontMetrics().stringWidth(line3)) / 2;
            int x4 = (PANEL_WIDTH - g.getFontMetrics().stringWidth(line4)) / 2;
            g.drawString(line1, x1, y);
            g.drawString(line2, x2, y + 30);
            g.drawString(line3, x3, y + 60);
            g.drawString(line4, x4, y + 110);
            return; // Don’t draw PAUSE / GAME OVER overlays on menu
        }

        // Paused overlay
        if (gameState == GameState.RUNNING && paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Consolas", Font.BOLD, 32));
            String text = "PAUSED";
            int w = g.getFontMetrics().stringWidth(text);
            g.drawString(text, (PANEL_WIDTH - w) / 2, PANEL_HEIGHT / 2);
        }

        // Game over overlay
        if (gameState == GameState.GAME_OVER) {
            g.setColor(new Color(0, 0, 0, 170));
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 36));
            String go = "GAME OVER";
            int goWidth = g.getFontMetrics().stringWidth(go);
            g.drawString(go, (PANEL_WIDTH - goWidth) / 2, PANEL_HEIGHT / 2 - 20);

            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            String info = "Press R to return to Menu";
            int infoWidth = g.getFontMetrics().stringWidth(info);
            g.drawString(info, (PANEL_WIDTH - infoWidth) / 2, PANEL_HEIGHT / 2 + 20);

            if (score == highScore && score > 0) {
                g.setColor(Color.YELLOW);
                String hs = "NEW HIGH SCORE!";
                int hsWidth = g.getFontMetrics().stringWidth(hs);
                g.drawString(hs, (PANEL_WIDTH - hsWidth) / 2, PANEL_HEIGHT / 2 + 50);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == GameState.RUNNING && !paused) {
            move();
            checkCollisions();
            repaint();
        }
    }

    private void move() {
        // current head
        Point head = snake.get(0);
        int headX = head.x;
        int headY = head.y;

        switch (direction) {
            case 'U' -> headY--;
            case 'D' -> headY++;
            case 'L' -> headX--;
            case 'R' -> headX++;
        }

        Point newHead = new Point(headX, headY);

        // Check food
        if (newHead.equals(food)) {
            snake.add(0, newHead);
            score += 10;
            soundManager.playEat();
            updateSpeedIfNeeded();
            spawnFood();
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }
    }

    private void updateSpeedIfNeeded() {
        int newLevel = (score / SCORE_PER_LEVEL) + 1;
        if (newLevel > level) {
            level = newLevel;
            // Decrease delay to speed up, but don't go below MIN_DELAY
            currentDelay = Math.max(MIN_DELAY, baseDelay - (level - 1) * 10);
            if (timer != null) {
                timer.setDelay(currentDelay);
                timer.setInitialDelay(currentDelay);
                timer.restart();
            }
        }
    }

    private void togglePause() {
        if (gameOver) return;
        paused = !paused;
        if (paused) {
            if (timer != null) timer.stop();
        } else {
            if (timer != null) timer.start();
        }
        repaint();
    }

    private void checkCollisions() {
        Point head = snake.get(0);

        // Wall collision
        if (head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT) {
            gameOver();
            return;
        }

        // Self collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                return;
            }
        }
    }

    private void gameOver() {
        running = false;
        gameOver = true;
        gameState = GameState.GAME_OVER;
        if (timer != null) {
            timer.stop();
        }

        boolean newHigh = false;
        if (score > highScore) {
            highScore = score;
            highScoreManager.saveHighScore(highScore);
            newHigh = true;
        }

        soundManager.playGameOver();
        if (newHigh) {
            soundManager.playHighScore();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // MENU: difficulty selection
        if (gameState == GameState.MENU) {
            if (key == KeyEvent.VK_1) {        // Easy
                baseDelay = 180;
                currentDelay = baseDelay;
                startGame();
            } else if (key == KeyEvent.VK_2) { // Normal
                baseDelay = 140;
                currentDelay = baseDelay;
                startGame();
            } else if (key == KeyEvent.VK_3) { // Hard
                baseDelay = 100;
                currentDelay = baseDelay;
                startGame();
            }
            return;
        }

        // Restart from GAME_OVER
        if (gameState == GameState.GAME_OVER && key == KeyEvent.VK_R) {
            gameState = GameState.MENU;
            running = false;
            gameOver = false;
            repaint();
            return;
        }

        // Pause / resume with P (only when running)
        if (gameState == GameState.RUNNING && key == KeyEvent.VK_P) {
            togglePause();
            return;
        }

        // Ignore movement keys when not running or while paused
        if (gameState != GameState.RUNNING || paused) {
            return;
        }

        // Movement (no 180-degree turns)
        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && direction != 'R') {
            direction = 'L';
        } else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && direction != 'L') {
            direction = 'R';
        } else if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && direction != 'D') {
            direction = 'U';
        } else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && direction != 'U') {
            direction = 'D';
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
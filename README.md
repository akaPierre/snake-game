# Snake Game 🎮

A classic **Snake Game** implemented in **Java 25** with **Swing** and built using **Maven**. Guide the snake around a grid, eat food to grow, avoid walls and your own tail, and chase new high scores with multiple difficulty levels, pause support, and sound effects.

This project is ideal for practicing:

- Java 2D rendering with `JPanel` and `Graphics`
- Game loops using `javax.swing.Timer`
- Keyboard input with `KeyListener`
- Basic game state machines (menu / running / paused / game over)
- File I/O for persistent high scores
- Simple audio with `javax.sound.sampled.Clip`

---

## ✨ Features

- **Classic snake gameplay**
    - Grid-based movement
    - Growth when eating food
    - Self-collision and wall collision detection
- **Difficulty levels**
    - Main menu with **Easy**, **Normal**, **Hard**
    - Higher difficulties start faster
    - Speed scales with score (level system)
- **Controls**
    - Movement: Arrow keys or WASD
    - Pause/Resume: `P`
    - Restart from Game Over: `R` (returns to main menu)
- **Game states**
    - **Menu**: title screen + difficulty selection
    - **Running**: active gameplay
    - **Paused**: overlay with “PAUSED”
    - **Game Over**: overlay with restart prompt and high-score info
- **High score**
    - Stored in a local `highscore.txt` file
    - Loaded at startup and updated when a new record is set
- **Sound effects**
    - Eat food
    - Game over
    - New high score

---

## 🛠 Tech Stack

- **Language:** Java 25 (`source`/`target` 25, compiled with `release` 21 in Maven)
- **GUI:** Swing (`JFrame`, `JPanel`, `Timer`, `KeyListener`)
- **Build Tool:** Maven
- **Audio:** `javax.sound.sampled.Clip`
- **Persistence:** Plain text file for high score

---

## 📋 Requirements

#### Make sure you have:

- **Java JDK 25** installed  
```java -version```

- **Maven 3.8+** installed  
```mvn -v```

#### No external libraries are required beyond the JDK; Swing and audio APIs are part of standard Java.

---

## 📁 Project Structure
```
snake-game/
├── pom.xml
├── README.md
└── src/
└── main/
├── java/
│ └── com/
│ └── snakegame/
│ ├── SnakeGame.java # Main entry point (JFrame bootstrap)
│ ├── GamePanel.java # Core game logic, rendering, input
│ ├── HighScoreManager.java # Load/save high score to file
│ └── SoundManager.java # Load/play WAV sound effects
└── resources/
└── sounds/
├── eat.wav
├── gameover.wav
└── highscore.wav
```

---

## ⚙️ Maven Configuration

#### The project uses a simple Maven setup:

```
<groupId>com.snakegame</groupId>
<artifactId>snake-game</artifactId>
<version>1.0-SNAPSHOT</version>

<properties>
    <maven.compiler.source>25</maven.compiler.source>
    <maven.compiler.target>25</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <release>21</release>
            </configuration>
        </plugin>
    </plugins>
</build>
```

#### JUnit is included for tests.

---

## 🚀 Building and Running

### From the project root:

#### 1. Build the JAR
```
mvn clean package
```

#### This creates:

```
target/snake-game-1.0-SNAPSHOT.jar
```

#### 2. Run the game
```
java -cp target/snake-game-1.0-SNAPSHOT.jar com.snakegame.SnakeGame
```

#### A window titled “Snake Game” should open with the menu visible.

---

## ## ✨ Features

- **Classic snake gameplay**
    - Grid-based movement
    - Growth when eating food
    - Self-collision and wall collision detection
- **Difficulty levels**
    - Main menu with **Easy**, **Normal**, **Hard**
    - Higher difficulties start faster
    - Speed scales with score (level system)
- **Controls**
    - Movement: Arrow keys or WASD
    - Pause/Resume: `P`
    - Restart from Game Over: `R` (returns to main menu)
- **Game states**
    - **Menu**: title screen + difficulty selection
    - **Running**: active gameplay
    - **Paused**: overlay with “PAUSED”
    - **Game Over**: overlay with restart prompt and high-score info
- **High score**
    - Stored in a local `highscore.txt` file
    - Loaded at startup and updated when a new record is set
- **Sound effects**
    - Eat food
    - Game over
    - New high score

---

## 🎮 Gameplay & Controls

### Menu
- Shown on startup and after pressing `R` on the game over screen.
- Options:

  - `1` – Easy (slower starting speed)

  - `2` – Normal

  - `3` – Hard (faster starting speed)

### During the game
- Move:

  - Arrow keys: `↑ ↓ ← →`

  - Or: `W A S D`

- Pause / Resume:

  - Press `P`

- Restart (from Game Over):

  - Press `R` to return to the menu

- Rules
  - Eating food:

    - Snake grows by one segment.

    - Score increases (e.g. +10 per food).

    - Game speed increases every N points (level up).

- Collisions:

  - Hitting a wall or your own body ends the game.

- High score:

  - If your score exceeds the saved high score, it is updated and stored in highscore.txt.

---

## 🔊 Sound Effects
Sound files are loaded from `src/main/resources/sounds`:

- `eat.wav` – played when food is eaten.

- `gameover.wav` – played on game over.

- `highscore.wav` – played when a new high score is achieved.

Ensure your WAV files are valid PCM WAVs; if loading fails, the game will still run but without sound.

---

## 🧱 Architecture Overview
- SnakeGame.java

  - Creates the main `JFrame`, attaches `GamePanel`, and starts the game loop.

- GamePanel.java

  - Holds all game state: snake segments, food, direction, score, level, game state enum.

  - Uses a `javax.swing.Timer` for the game loop (movement, collision checks, repaint).

  - Implements `KeyListener` for controls and state changes (menu, pause, restart).

  - Renders the grid, snake, food, HUD, menu, pause, and game over overlays.

- HighScoreManager.java

  - Loads an integer high score from `highscore.txt` if present.

  - Saves a new high score when the current score beats the record.

- SoundManager.java

  - Loads WAV clips once at startup.

  - Provides methods `playEat()`, `playGameOver()`, and `playHighScore()`.

---

## 🔮 Future Improvements
- Multiple food types (e.g. golden apples, poison items).

- Edge-wrapping mode (snake appears on the opposite side instead of dying).

- Leaderboard of top N scores.

- Settings menu (toggle sound, adjust grid size, custom key bindings).

- Export to a platform-specific launcher or installer.

---

## 📄 License

MIT License

Copyright (c) 2025 Daniel Pierre Fachini de Toledo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

---

## 👤 Author

**Daniel Pierre Fachini de Toledo**  
GitHub: https://github.com/akaPierre
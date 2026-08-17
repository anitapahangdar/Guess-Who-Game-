# Guess Who — Java Swing Game

A desktop guessing game built in Java with Swing. The player chooses a character, asks questions about the computer's hidden character, eliminates candidates, and makes a final guess before the five-minute timer expires.

This repository contains a collaborative school project by **Arshia, Anita, Oliver, and Daniel**. The repository is maintained by **Anita Pahangdar**.

## Highlights

- 24-character game board
- Seven character-attribute questions
- Random computer character selection
- Manual candidate elimination
- Five-minute countdown timer
- Optional character artwork and sound effects
- Text-based fallbacks when optional media assets are not installed
- Separation between the Swing interface and game logic

## Technology

- Java
- Java Swing and AWT
- Java Collections
- Java Sound API
- Object-oriented design

## Project structure

```text
.
├── gui/
│   └── GameBoardGUI.java   # Swing interface and player interactions
├── logic/
│   └── GameLogic.java      # Characters, attributes, questions, and game state
├── main/
│   └── GuessWho.java       # Application entry point
└── README.md
```

## Run locally

### Requirements

Install a Java Development Kit (JDK) 8 or newer and confirm that `java` and `javac` are available in your terminal.

### Command line

From the repository root:

```bash
javac -d out main/GuessWho.java gui/GameBoardGUI.java logic/GameLogic.java
java -cp out main.GuessWho
```

On Windows PowerShell, the same commands work as written.

You can also import the repository into IntelliJ IDEA, Eclipse, or VS Code and run `main.GuessWho`.

## How to play

1. Select your character from the board.
2. Enter a question number and choose **Ask** to learn about the computer's character.
3. Click character cards to mark unlikely candidates as eliminated.
4. Choose **Guess Character**, enter a name, and submit your final guess.
5. Make your guess before the timer reaches zero.

## Optional media assets

The source supports character images in an `images/` directory and WAV audio in `src/sounds/`. Those media files are not included in this repository. The application therefore displays character names as buttons and runs without audio by default.

If you add your own appropriately licensed assets, use the filenames referenced in `gui/GameBoardGUI.java`.

## Current scope

This version demonstrates a playable player-versus-computer guessing flow. The computer selects a hidden character and answers questions from the stored attribute data. Automated computer questioning and turn-taking exist in the logic layer but are not yet connected to the Swing interface.

## Possible next steps

- Connect the computer's questioning logic to the interface
- Add unit tests for character filtering and win conditions
- Package the application as an executable JAR
- Replace local file paths with classpath resources
- Add original, appropriately licensed character artwork

## Credits

Created as a collaborative school project by **Arshia, Anita, Oliver, and Daniel** on January 17, 2025.

Repository presentation and ongoing maintenance: **Anita Pahangdar**.

# Hangman-OS

_This project is developed by **Jake Mondragon**, **Benedict Pagba**, **Justin Magne Agaton**, and **Niel Frias**_

## Overview

This HackedMan is a classic word-guessing game with an OS-theme. The game was inspired by the operating system being infected by a virus, to save your OS you need to correctly guess the word else your operating system will be infected by a virus. You have six tries to guess the word. This game is implemented in Java.

## Features

- Random word selection from a Database.txt
- Display of correctly guessed letters and blanks
- Count of remaining attempts
- GUI display for the art

---

## Installation & Setup Guide

### 1. Clone the Repository

```bash
git clone https://github.com/jbmondragon/Hangman-OS.git
cd Mine-Sweeper
```

## 2. Open the Project in VS Code

- Make sure you have the **Java Extension Pack** installed and use java 21.
- Open the `Main.java` file.

To compile and run the project via terminal:

```bash
find . -name "*.class" -type f -delete && javac --release 21 -d out $(find . -name "*.java") && java -cp "out;." Main
```

### 3️. Run the Application

You can run the game by:

- Clicking the Run Java button in the top-right of VS Code

### 4. Create and Run a jar file

```bash
jar cfe HackedMan.jar Main -C out .
java -jar HackedMan.jar
```

### 5. Run the .exe by double clicking the file

package backend;

public class Hangman {

    private static final int MAX_ATTEMPTS = 6;

    private String wordToGuess;
    private char[] guessedWord;
    private int remainingAttempts;
    private boolean[] guessedLetters;

    public Hangman(String word) {
        this.wordToGuess = word.toLowerCase();
        this.guessedWord = new char[word.length()];
        this.remainingAttempts = MAX_ATTEMPTS;
        this.guessedLetters = new boolean[26];
        initializeGuessedWord();
    }

    private void initializeGuessedWord() {
        for (int i = 0; i < guessedWord.length; i++) {
            guessedWord[i] = '_';
        }
    }

    // button pressed logic
    public boolean guess(char letter) {
        int index = letter - 'a';
        if (guessedLetters[index])
            return false;

        guessedLetters[index] = true;
        boolean found = false;

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == letter) {
                guessedWord[i] = letter;
                found = true;
            }
        }

        if (!found)
            remainingAttempts--;
        return found;
    }

    // ---------- GETTERS ----------
    public String getGuessedWord() {
        return new String(guessedWord).replace("", " ").trim();
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public boolean isGameOver() {
        return remainingAttempts <= 0 || isWordGuessed();
    }

    public boolean isWordGuessed() {
        for (char c : guessedWord) {
            if (c == '_')
                return false;
        }
        return true;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }
}

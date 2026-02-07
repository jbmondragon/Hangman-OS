package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Database {

    private static final Random RANDOM = new Random();

    // Code for reading the word that needs to be guesses from Database.txt
    public String getRandomWord() {

        ArrayList<String> words = new ArrayList<>();

        try (Scanner scan = new Scanner(new File("Database.txt"))) {

            while (scan.hasNextLine()) {
                String word = scan.nextLine().trim();

                if (!word.isEmpty()) {
                    words.add(word.toLowerCase());
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return null;
        }

        if (words.isEmpty()) {
            System.out.println("File is empty.");
            return null;
        }

        return words.get(RANDOM.nextInt(words.size()));
    }
}

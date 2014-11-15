package Øvinger;

import java.util.Random;
import java.util.Scanner;

/*
 * En simpel Hangman implementasjon.
 * Jeg har dessverre en annen utgave av boka som dere følger når jeg skal lete opp øvingene, jeg fant 2 forskjellige hangman versjoner
 * i 2 forskjellege kapittel, men jeg antok det var den fra kapittel 7 i min utgave hvor det er uten bruk av file I/O.
 */

public class Hangman {
    
    public Random random = new Random();
    
    public static final int MAX_GUESSES = 16; //change to increase/decrease difficulty
    private int numCorrect, numGuesses;
    private String winWord, currWord, guessedChars;
    private String[] words = {
            "string", "palindrome", "racecar", "irony", "philea", 
            "riemann", "manifolds", "euler", "identity", "prime",
            "generation", "polynomial", "flat", "gouraud", "blinn-phong",
            "lambert", "sutherland-cohen", "frenet-serret", "hyperbolic", "interpolation",
            "deferred", "continuous", "discrete", "induction", "simple", "regular", "relativity",
            "zappa", "jamiroquai", "opeth", "zeppelin", "debussy", "yes", "genesis", "megadeth",
            "mutex", "aggregate", "concurrency", "generics", "dijkstras",
            "wolfenstein3D", "doom", "quake", "half-life",
    };
    
    public Hangman() {
        winWord = "";
        currWord = "";
        guessedChars = "";
        numCorrect = 0;
        numGuesses = 0;
    }
    
    //Sets a random word for the hangman game from the wordlist
    public void init() {
        winWord = "";
        currWord = "";
        guessedChars = "";
        numCorrect = 0;
        numGuesses = 0;
        
        int index = random.nextInt(words.length);
        winWord = words[index];
        
        for(int i = 0; i < winWord.length(); ++i)
            currWord += "*";
    }
    
    public String getCurrent() {
        return currWord;
    }
    
    public String getWord() {
        return winWord;
    }
    
    public String getGuessed() {
        return guessedChars;
    }
    
    //returns true if and only if current word contained the guess
    //returns false if user already guessed that char or guessed incorrectly
    public boolean handleGuess(String guess) {
        if(winWord.equalsIgnoreCase(guess)) {
            numCorrect = winWord.length();
            return true;
        }
        char c = guess.charAt(0);
        if(guessedChars.contains(Character.toString(c).toLowerCase())) {
            System.out.println("You already guessed that!");
            return false;
        }
        guessedChars += guess;
        numGuesses++;
        if(winWord.contains(Character.toString(c).toLowerCase())) {
            int index = winWord.indexOf(Character.toLowerCase(c));
            currWord = new StringBuilder(currWord).replace(index, index+1, Character.toString(c).toLowerCase()).toString();
            numCorrect++;
            return true;
        }
        return false;
    }
    
    public boolean won() {
        if(numCorrect == currWord.length()) return true;
        return false;
    }
    
    public int getGuessAmount() {
        return numGuesses;
    }
    
    public boolean failed() {
        if(numGuesses >= MAX_GUESSES) return true;
        return false;
    }
    
    public static void main(String[] args) {
        
        Hangman hangman = new Hangman();
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        String guess;
        
        do {
            hangman.init();
            System.out.println("Playing Hangman!\nThe word: " + hangman.getCurrent() + ", start guessing & good luck!");
            while(true) {
                guess = scanner.next();
                hangman.handleGuess(guess);
                if(hangman.failed()) {
                    System.out.println("You spent all your guesses before solving the word and lost :(!");
                    System.out.println("The word was: \"" + hangman.getWord() + "\"");
                    break;
                }
                else if(hangman.won()) {
                    System.out.println("You figured out the word in time, hooray :)!");
                    break;
                }
                System.out.println("The word so far: " + hangman.getCurrent() + ", guessed chars: " + hangman.getGuessed());
            }
            System.out.print("Play again? (y/n): ");
            char c = scanner.next().charAt(0);
            c = Character.toLowerCase(c);
            if(c == 'n') {
                quit = true;
            }
        } while(!quit);
    }
}

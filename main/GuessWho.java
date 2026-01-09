package main;
/*
 * Name: Arshia, Anita, Oliver, Daniel
 * Date: 2025-01-17
 * Project: Guess Who ISP
 * 
 */
import gui.GameBoardGUI;
import logic.GameLogic;

//main class
public class GuessWho {
    public static void main(String[] args) {
    	//creates a new game board where the game will play. calls the logic class
        GameLogic gameLogic = new GameLogic();
        new GameBoardGUI(gameLogic);
    }
}

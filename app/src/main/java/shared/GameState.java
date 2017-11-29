package shared;

import java.util.Dictionary;
import java.util.Hashtable;
import java.io.Serializable;

public class GameState implements Serializable {
    public class Player implements Serializable {
        public int points;
        public String login;
        public boolean isConnected;
        public boolean hasTurn;
    }

    public enum Phase {
        ChoosingWord,
        Guess,
        EndGame
    }

    private final byte MAX_CLIENTS = 4;
    public Player players[];
    public Dictionary<Character, Boolean> keyboard;
    public String word;
    public Phase phase;
    public int hangmanHealth;

    public GameState() {
        players = new Player[4];
        for (byte i = 0; i < MAX_CLIENTS; i++)
            players[i] = new Player();
        hangmanHealth=7;
        keyboard = new Hashtable<>(26);
        for(char c = 'A'; c <= 'Z'; c++) {
            keyboard.put(c, false);
        }
    }
}

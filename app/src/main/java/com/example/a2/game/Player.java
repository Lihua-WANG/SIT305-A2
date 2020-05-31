package com.example.a2.game;

/**
 * Players
 * Score recording
 */

public class Player {

    // Players name
    String mName;
    // white or black
    int type;
    // Victory numbers
    int mWin;
    // Defeat numbers
    int mLose;

    // Initial constructors
    public Player(String name, int type) {
        this.mName = name; // Player name
        this.type = type; // player's chess type
    }

    // Returns the type of the chess piece
    public Player(int type) {
        if (type == Game.WHITE) {
            this.mName = "White";
        } else if (type == Game.BLACK) {
            this.mName = "Black";
        }
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    /**
     * Win a game
     */

    // Add a victory
    public void win() {
        mWin += 1;
    }

    // Get the number of current victory
    public String getWin() {
        return String.valueOf(mWin);
    }

    /**
     * Lose a game
     */
    public void lose() {
        mLose += 1;
    }
}

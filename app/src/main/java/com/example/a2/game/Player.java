package com.example.a2.game;

public class Player {

    String mName;
    // white or black
    int type;
    // Victory
    int mWin;
    // Defeat
    int mLose;

    public Player(String name, int type) {
        this.mName = name;
        this.type = type;
    }

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
    public void win() {
        mWin += 1;
    }

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

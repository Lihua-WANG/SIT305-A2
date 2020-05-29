package com.example.a2.game;

import android.os.Handler;
import android.os.Message;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Handling game logic
 */

public class Game {

    //    public static final int SCALE_SMALL = 11;
    public static final int SCALE_MEDIUM = 15;
//    public static final int SCALE_LARGE = 19;

    // Self
    Player me;
    // Partner
    Player challenger;

    private int mMode = 0;

    // The default black chess first
    private int mActive = 1;
    int mGameWidth = 0;
    int mGameHeight = 0;
    int[][] mGameMap = null;
    Deque<Coordinate> mActions;

    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private Handler mNotify;

    public Game(Handler h, Player me, Player challenger) {
        this(h, me, challenger, SCALE_MEDIUM, SCALE_MEDIUM);
    }

    public Game(Handler h, Player me, Player challenger, int width, int height) {
        mNotify = h;
        this.me = me;
        this.challenger = challenger;
        mGameWidth = width;
        mGameHeight = height;
        mGameMap = new int[mGameWidth][mGameHeight];
        mActions = new LinkedList<Coordinate>();
    }

    public void setMode(int mode) {
        this.mMode = mode;
    }

//    public int getMode() {
//        return mMode;
//    }

    /**
     * Regret chess
     *
     * @return Whether can regret chess
     */
    public boolean rollback() {
        Coordinate c = mActions.pollLast();
        if (c != null) {
            mGameMap[c.x][c.y] = 0;
            changeActive();
            return true;
        }
        return false;
    }

    /**
     * Game width
     *
     * @return Chessboard columns number
     */
    public int getWidth() {
        return mGameWidth;
    }

    /**
     * Game height
     *
     * @return Chessboard Lateral number
     */
    public int getHeight() {
        return mGameHeight;
    }

    /**
     * Set a chess piece
     *
     * @param x Horizontal subscript
     * @param y Vertical subscript
     * @return Whether the current position can set chess pieces
     */
    public boolean addChess(int x, int y) {
        if (mMode == GameConstants.MODE_FIGHT) {
            if (mGameMap[x][y] == 0) {
                int type;
                if (mActive == BLACK) {
                    mGameMap[x][y] = BLACK;
                    type = Game.BLACK;
                } else {
                    mGameMap[x][y] = WHITE;
                    type = Game.WHITE;
                }
                if (!isGameEnd(x, y, type)) {
                    changeActive();
                    sendAddChess(x, y);
                    mActions.add(new Coordinate(x, y));
                }
                return true;
            }
        } else if (mMode == GameConstants.MODE_SINGLE) {
            if (mActive == me.type && mGameMap[x][y] == 0) {
                mGameMap[x][y] = me.type;
                mActive = challenger.type;
                if (!isGameEnd(x, y, me.type)) {
                    sendAddChess(x, y);
                    mActions.add(new Coordinate(x, y));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Set a chess piece
     *
     * @param x      Horizontal subscript
     * @param y      Vertical subscript
     * @param player Game player
     */
    public void addChess(int x, int y, Player player) {
        if (mGameMap[x][y] == 0) {
            mGameMap[x][y] = player.type;
            mActions.add(new Coordinate(x, y));
            boolean isEnd = isGameEnd(x, y, player.type);
            mActive = me.type;
            if (!isEnd) {
                mNotify.sendEmptyMessage(GameConstants.ACTIVE_CHANGE);
            }
        }
    }

    /**
     * Set a chess piece
     *
     * @param c      Chess piece position
     * @param player Game player
     */
    public void addChess(Coordinate c, Player player) {
        addChess(c.x, c.y, player);
    }

    public static int getFighter(int type) {
        if (type == BLACK) {
            return WHITE;
        } else {
            return BLACK;
        }
    }

    /**
     * Return the current player
     *
     * @return mActive
     */
    public int getActive() {
        return mActive;
    }

    /**
     * Get the chessboard
     *
     * @return Chessboard data
     */
    public int[][] getChessMap() {
        return mGameMap;
    }

    /**
     * Get chessboard history
     *
     * @return mActions
     */
    public Deque<Coordinate> getActions() {
        return mActions;
    }

    /**
     * Reset game
     */
    public void reset() {
        mGameMap = new int[mGameWidth][mGameHeight];
        mActive = BLACK;
        mActions.clear();
    }


    private void changeActive() {
        if (mActive == BLACK) {
            mActive = WHITE;
        } else {
            mActive = BLACK;
        }
    }

    private void sendAddChess(int x, int y) {
        Message msg = new Message();
        msg.what = GameConstants.ADD_CHESS;
        msg.arg1 = x;
        msg.arg2 = y;
        mNotify.sendMessage(msg);
    }

    // Determine if five pieces are on the same line
    private boolean isGameEnd(int x, int y, int type) {
        int leftX = Math.max(x - 4, 0);
        int rightX = Math.min(x + 4, mGameWidth - 1);
        int topY = Math.max(y - 4, 0);
        int bottomY = Math.min(y + 4, mGameHeight - 1);

        int horizontal = 1;
        // Laterally left
        for (int i = x - 1; i >= leftX; --i) {
            if (mGameMap[i][y] != type) {
                break;
            }
            ++horizontal;
        }
        // Laterally right
        for (int i = x + 1; i <= rightX; ++i) {
            if (mGameMap[i][y] != type) {
                break;
            }
            ++horizontal;
        }
        if (horizontal >= 5) {
            sendGameResult(type);
            return true;
        }

        int vertical = 1;
        // Vertically upward
        for (int j = y - 1; j >= topY; --j) {
            if (mGameMap[x][j] != type) {
                break;
            }
            ++vertical;
        }
        // Vertically downward
        for (int j = y + 1; j <= bottomY; ++j) {
            if (mGameMap[x][j] != type) {
                break;
            }
            ++vertical;
        }
        if (vertical >= 5) {
            sendGameResult(type);
            return true;
        }

        int leftOblique = 1;
        // Left diagonally upward
        for (int i = x + 1, j = y - 1; i <= rightX && j >= topY; ++i, --j) {
            if (mGameMap[i][j] != type) {
                break;
            }
            ++leftOblique;
        }
        // Left obliquely downward
        for (int i = x - 1, j = y + 1; i >= leftX && j <= bottomY; --i, ++j) {
            if (mGameMap[i][j] != type) {
                break;
            }
            ++leftOblique;
        }
        if (leftOblique >= 5) {
            sendGameResult(type);
            return true;
        }

        int rightOblique = 1;
        // Right oblique upward
        for (int i = x - 1, j = y - 1; i >= leftX && j >= topY; --i, --j) {
            if (mGameMap[i][j] != type) {
                break;
            }
            ++rightOblique;
        }
        // Right obliquely downward
        for (int i = x + 1, j = y + 1; i <= rightX && j <= bottomY; ++i, ++j) {
            if (mGameMap[i][j] != type) {
                break;
            }
            ++rightOblique;
        }
        if (rightOblique >= 5) {
            sendGameResult(type);
            return true;
        }

        return false;
    }

    private void sendGameResult(int player) {
        Message msg = Message.obtain();
        msg.what = GameConstants.GAME_OVER;
        msg.arg1 = player;
        mNotify.sendMessage(msg);
    }

}

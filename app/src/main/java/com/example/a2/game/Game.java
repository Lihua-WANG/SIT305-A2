package com.example.a2.game;

import android.os.Handler;
import android.os.Message;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Handling game logic
 * Set the board height, width, and number of horizontal and vertical grids
 */

public class Game {

    // Chessboard size
    // Small: 11 grids per line; Medium: 15; Large: 19
    public static final int SCALE_SMALL = 11;
    public static final int SCALE_MEDIUM = 15;
    public static final int SCALE_LARGE = 19;

    // Self
    Player me;
    // Partner
    Player challenger;

    // Set the game mode, Single and Fight Game mode,
    // here initialize to 0
    private int mMode = 0;

    // The default is black chess first putting
    private int mActive = 1;
    // Initialize the width and height of chessboard
    int mGameWidth = 0;
    int mGameHeight = 0;
    // Initialize the map used to store the current chess piece status
    int[][] mGameMap = null;
    // Use dynamic array to store each operation, to regret chess easily
    Deque<Coordinate> mActions;

    // Black chess type is 1
    // White chess type is 2
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private Handler mNotify;

    /**
     * Define the constructors of Game.class
     */
    // Specifies the size of the chessboard
    public Game(Handler h, Player me, Player challenger) {
        this(h, me, challenger, SCALE_MEDIUM, SCALE_MEDIUM);
    }

    // Initialize variables
    public Game(Handler h, Player me, Player challenger, int width, int height) {
        mNotify = h;
        this.me = me;
        this.challenger = challenger;
        mGameWidth = width;
        mGameHeight = height;
        mGameMap = new int[mGameWidth][mGameHeight];
        mActions = new LinkedList<Coordinate>();
    }

    // Set the game mode which have defined in GameView.class
    public void setMode(int mode) {
        this.mMode = mode;
    }

    public int getMode() {
        return mMode;
    }

    /**
     * Regret chess
     *
     * @return Whether can regret chess
     */
    public boolean rollback() {
        // Get the horizontal and vertical coordinates from the last operation
        Coordinate c = mActions.pollLast();
        // Only can regret chess when the coordinates are not empty
        if (c != null) {
            // Clear the corresponding coordinates
            mGameMap[c.x][c.y] = 0;
            // Change the next player
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
     * Judge whether can set a chess piece
     *
     * @param x Horizontal subscript
     * @param y Vertical subscript
     * @return Whether the current position can set chess pieces
     */
    public boolean addChess(int x, int y) {
        // Fight Mode
        if (mMode == GameConstants.MODE_FIGHT) {
            // Whether the position putting piece is empty
            if (mGameMap[x][y] == 0) {
                int type;
                // Get the current player is black or white, and make choice
                if (mActive == BLACK) {
                    mGameMap[x][y] = BLACK;
                    type = Game.BLACK;
                } else {
                    mGameMap[x][y] = WHITE;
                    type = Game.WHITE;
                }
                // If game is not over, save the operation just made
                if (!isGameEnd(x, y, type)) {
                    changeActive();
                    sendAddChess(x, y);
                    mActions.add(new Coordinate(x, y));
                }
                // Returns true, means the pieces have been successfully added
                return true;
            }
        }
        // Single game mode is the same as Fight mode
        else if (mMode == GameConstants.MODE_SINGLE) {
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
        // Returns false means none of these match and can’t add chess here
        return false;
    }

    /**
     * Put a chess piece
     *
     * @param x      Horizontal subscript
     * @param y      Vertical subscript
     * @param player Game player
     */
    public void addChess(int x, int y, Player player) {

        if (mGameMap[x][y] == 0) {
            // Judge the type of chess piece
            // Assign the color type of the chess at the corresponding position
            mGameMap[x][y] = player.type;
            mActions.add(new Coordinate(x, y));
            // Message operation is changed
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
     * @return mGameMap: Whole chessboard data array
     */
    public int[][] getChessMap() {
        return mGameMap;
    }

    /**
     * Get chessboard history
     *
     * @return mActions: Dynamic array to store chess actions
     */
    public Deque<Coordinate> getActions() {
        return mActions;
    }

    /**
     * Reset game
     */
    public void reset() {
        // Create a new array to store chessboard information
        mGameMap = new int[mGameWidth][mGameHeight];
        // Default black piece play first
        mActive = BLACK;
        // Clear all the former actions
        mActions.clear();
    }

    // Set the next player to put chess piece
    private void changeActive() {
        // Make player characters exchange
        if (mActive == BLACK) {
            mActive = WHITE;
        } else {
            mActive = BLACK;
        }
    }

    // Send successful chess putting message
    private void sendAddChess(int x, int y) {
        Message msg = new Message();
        msg.what = GameConstants.ADD_CHESS;
        msg.arg1 = x;
        msg.arg2 = y;
        mNotify.sendMessage(msg);
    }

    // Determine if five pieces are on the same line (five-in-row) in 4 directions
    //(int horizontal coordinate, int vertical coordinate, int chess type)
    private boolean isGameEnd(int x, int y, int type) {
        // When the variable over the range, assigned the edge value,
        // otherwise assigned the abscissa or ordinate + -4
        int leftX = Math.max(x - 4, 0);
        int rightX = Math.min(x + 4, mGameWidth - 1);
        int topY = Math.max(y - 4, 0);
        int bottomY = Math.min(y + 4, mGameHeight - 1);

        // Traversed whether it is the same type piece in all directions,
        // if all it is, the flag is incremented.

        // Judge in horizontal flag
        int horizontal = 1;

        // horizontal left
        for (int i = x - 1; i >= leftX; --i) {
            if (mGameMap[i][y] != type) {
                break;
            }
            ++horizontal;
        }
        // horizontal right
        for (int i = x + 1; i <= rightX; ++i) {
            if (mGameMap[i][y] != type) {
                break;
            }
            ++horizontal;
        }
        // If flags increased to 5 which means five-in-row,
        // message game information and specify which type
        if (horizontal >= 5) {
            sendGameResult(type);
            return true;
        }

        // The same as horizontal
        // vertical flag
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
        // Vertically flags judgment
        if (vertical >= 5) {
            sendGameResult(type);
            return true;
        }

        // left oblique flag
        int leftOblique = 1;
        // left oblique upward
        for (int i = x + 1, j = y - 1; i <= rightX && j >= topY; ++i, --j) {
            if (mGameMap[i][j] != type) {
                break;
            }
            ++leftOblique;
        }
        // left oblique downward
        for (int i = x - 1, j = y + 1; i >= leftX && j <= bottomY; --i, ++j) {
            if (mGameMap[i][j] != type) {
                break;
            }
            ++leftOblique;
        }
        // left oblique flags judgment
        if (leftOblique >= 5) {
            sendGameResult(type);
            return true;
        }

        // right oblique flag
        int rightOblique = 1;
        // Right oblique upward
        for (int i = x - 1, j = y - 1; i >= leftX && j >= topY; --i, --j) {
            if (mGameMap[i][j] != type) {
                break;
            }
            ++rightOblique;
        }
        // Right oblique downward
        for (int i = x + 1, j = y + 1; i <= rightX && j <= bottomY; ++i, ++j) {
            if (mGameMap[i][j] != type) {
                break;
            }
            ++rightOblique;
        }
        // Right oblique flags judgment
        if (rightOblique >= 5) {
            sendGameResult(type);
            return true;
        }

        return false;
    }

    // Game winning and losing information,
    // send the signal of winner and victory
    private void sendGameResult(int player) {
        Message msg = Message.obtain();
        msg.what = GameConstants.GAME_OVER;
        msg.arg1 = player;
        mNotify.sendMessage(msg);
    }

}

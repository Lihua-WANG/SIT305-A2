package com.example.a2.game;

/**
 * SingleGame algorithm, attributes and constructor initial
 * Judging the various situations of the chess pieces by human-player
 * and method to deal with
 */

public class ComputerAI {

    private static final int FIVE = 100; // Live five: five in a row and both ends are not blocked
    private static final int L_FOUR = 90; // Live four: four in a row and one end blocked, another not
    private static final int D_FOUR = 100; // Dead four: four in a row and both ends are blocked

    public static final int HOR = 1;
    public static final int VER = 2;
    public static final int HOR_VER = 3;
    public static final int VER_HOR = 4;

    private int mWidth = 0;
    private int mHeight = 0;

    // Black chess priority value array
    int[][][] black = null;
    // white chess priority value array
    int[][][] white = null;

    // the value of position which has different performance
    // The weight of each point in Gobang
    int[][] plaValue = {{2, 6, 173, 212, 250, 250, 250}, {0, 5, 7, 200, 230, 231, 231},
            {0, 0, 0, 0, 230, 230, 230, 0}};
    int[][] cpuValue = {{0, 3, 166, 186, 229, 229, 229}, {0, 0, 5, 167, 220, 220, 220},
            {0, 0, 0, 0, 220, 220, 220, 0}};

    public ComputerAI(int width, int height) {
        mWidth = width;
        mHeight = height;
        black = new int[width][height][5];
        white = new int[width][height][5];
    }

    /**
     * Update board weights
     *
     * @param game
     */
    public void updateValue(Game game) {
        int[][] map = game.getChessMap();
    }

    /**
     * Update board weights
     */
    public void updateValue(int[][] map) {

        int[] computerValue = {0, 0, 0, 0};
        int[] playerValue = {0, 0, 0, 0};
        for (int i = 0; i < mWidth; i++) {
            for (int j = 0; j < mHeight; j++) {
                if (map[i][j] == 0) {
                    int counter = 0;
                    // Give different weights to different situations
                    // Vertical
                    for (int k = j + 1; k < mHeight; k++) {

                        if (map[i][k] == Game.BLACK)
                            computerValue[0]++;
                        if (map[i][k] == 0)
                            break;
                        if (map[i][k] == Game.WHITE) {
                            counter++;
                            break;
                        }
                        if (k == mHeight - 1)
                            counter++;
                    }

                    for (int k = j - 1; k >= 0; k--) {

                        if (map[i][k] == Game.BLACK)
                            computerValue[0]++;
                        if (map[i][k] == 0)
                            break;
                        if (map[i][k] == Game.WHITE) {
                            counter++;
                            break;
                        }
                        if (k == 0)
                            counter++;
                    }
                    if (j == 0 || j == mHeight - 1)
                        counter++;
                    white[i][j][0] = cpuValue[counter][computerValue[0]];
                    computerValue[0] = 0;
                    counter = 0;

                    // Backslash
                    for (int k = i + 1, l = j + 1; l < mHeight; k++, l++) {
                        if (k >= mHeight) {
                            break;
                        }
                        if (map[k][l] == Game.BLACK)
                            computerValue[1]++;
                        if (map[k][l] == 0)
                            break;
                        if (map[k][l] == Game.WHITE) {
                            counter++;
                            break;
                        }
                        if (k == mWidth - 1 || l == mHeight - 1)
                            counter++;
                    }

                    for (int k = i - 1, l = j - 1; l >= 0; k--, l--) {

                        if (k < 0) {
                            break;
                        }
                        if (map[k][l] == Game.BLACK)
                            computerValue[1]++;
                        if (map[k][l] == 0)
                            break;
                        if (map[k][l] == Game.WHITE) {
                            counter++;
                            break;
                        }
                        if (k == 0 || l == 0)
                            counter++;
                    }
                    if (i == 0 || i == mWidth - 1 || j == 0 || j == mHeight - 1)
                        counter++;

                    white[i][j][1] = cpuValue[counter][computerValue[1]];
                    computerValue[1] = 0;
                    counter = 0;

                    // Horizontal
                    for (int k = i + 1; k < mWidth; k++) {

                        if (map[k][j] == Game.BLACK)
                            computerValue[2]++;
                        if (map[k][j] == 0)
                            break;
                        if (map[k][j] == Game.WHITE) {
                            counter++;
                            break;
                        }
                        if (k == mWidth - 1)
                            counter++;
                    }


                    for (int k = i - 1; k >= 0; k--) {

                        if (map[k][j] == Game.BLACK)
                            computerValue[2]++;
                        if (map[k][j] == 0)
                            break;
                        if (map[k][j] == Game.WHITE) {
                            counter++;
                            break;
                        }
                        if (k == 0)
                            counter++;
                    }

                    if (i == 0 || i == mWidth - 1)
                        counter++;
                    white[i][j][2] = cpuValue[counter][computerValue[2]];
                    computerValue[2] = 0;
                    counter = 0;

                    // Forward slash
                    for (int k = i - 1, l = j + 1; l < mWidth; k--, l++) {

                        if (k < 0) {
                            break;
                        }
                        if (map[k][l] == Game.BLACK)
                            computerValue[3]++;
                        if (map[k][l] == 0)
                            break;
                        if (map[k][l] == Game.WHITE) {
                            counter++;
                            break;
                        }
                        if (k == 0 || l == mHeight - 1)
                            counter++;
                    }


                    for (int k = i + 1, l = j - 1; l >= 0; k++, l--) {

                        if (k >= mWidth) {
                            break;
                        }
                        if (map[k][l] == Game.BLACK)
                            computerValue[3]++;
                        if (map[k][l] == 0)
                            break;
                        if (map[k][l] == Game.WHITE) {
                            counter++;
                            break;
                        }
                        if (k == mWidth - 1 || l == 0)
                            counter++;
                    }
                    if (i == 0 || i == mWidth - 1 || j == 0 || j == mHeight - 1)
                        counter++;
                    white[i][j][3] = cpuValue[counter][computerValue[3]];
                    computerValue[3] = 0;
                    counter = 0;

                    // Judge the weights in both directions at the same time and give it an appropriate weight
                    for (int k = 0; k < 4; k++) {
                        if (white[i][j][k] == 173)
                            counter++;
                    }
                    if (counter >= 2 && white[i][j][4] < 175)
                        white[i][j][4] = 175;
                    counter = 0;


                    for (int k = 0; k < 4; k++) {
                        for (int l = 0; l < 4; l++) {
                            if (white[i][j][k] == 173 && white[i][j][l] == 200
                                    && white[i][j][4] < 201)
                                white[i][j][4] = 201;
                        }
                    }

                    if (j >= 1) {
                        if (map[i][j - 1] == 0) {
                            if (white[i][j - 1][0] >= 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] >= 173) {
                                        if (white[i][j][4] < 201) {
                                            white[i][j][4] = 201;
                                        }
                                    }
                                }
                            }
                            // If the weights in both directions are live three, lower the weights
                            // Live three: simultaneously forms two open rows of three stones
                            // (rows not blocked by an opponent's stone at either end).
                            if (white[i][j - 1][0] == 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] == 173) {
                                        if (white[i][j][4] == 201) {
                                            white[i][j][4] = 175;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (j >= 1 && i >= 1) {
                        if (map[i - 1][j - 1] == 0) {
                            if (white[i - 1][j - 1][1] >= 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] >= 173) {
                                        if (white[i][j][4] < 201) {
                                            white[i][j][4] = 201;
                                        }
                                    }
                                }
                            }
                            if (white[i - 1][j - 1][1] == 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] == 173) {
                                        if (white[i][j][4] == 201) {
                                            white[i][j][4] = 175;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i >= 1) {
                        if (map[i - 1][j] == 0) {
                            if (white[i - 1][j][2] >= 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] >= 173) {
                                        if (white[i][j][4] < 201) {
                                            white[i][j][4] = 201;
                                        }
                                    }
                                }
                            }
                            if (white[i - 1][j][2] == 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] == 173) {
                                        if (white[i][j][4] == 201) {
                                            white[i][j][4] = 175;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i > 0 && j < mHeight - 1) {
                        if (map[i - 1][j + 1] == 0) {
                            if (white[i - 1][j + 1][3] >= 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] >= 173) {
                                        if (white[i][j][4] < 201) {
                                            white[i][j][4] = 201;
                                        }
                                    }
                                }
                            }
                            if (white[i - 1][j + 1][3] == 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] == 173) {
                                        if (white[i][j][4] == 201) {
                                            white[i][j][4] = 175;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (j < mHeight - 1) {
                        if (map[i][j + 1] == 0) {
                            if (white[i][j + 1][0] >= 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] >= 173) {
                                        if (white[i][j][4] < 201) {
                                            white[i][j][4] = 201;
                                        }
                                    }
                                }
                            }
                            if (white[i][j + 1][0] == 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] == 173) {
                                        if (white[i][j][4] == 201) {
                                            white[i][j][4] = 175;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i < mWidth - 1 && j < mHeight - 1) {
                        if (map[i + 1][j + 1] == 0) {
                            if (white[i + 1][j + 1][1] >= 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] >= 173) {
                                        if (white[i][j][4] < 201) {
                                            white[i][j][4] = 201;
                                        }
                                    }
                                }
                            }
                            if (white[i + 1][j + 1][1] == 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] == 173) {
                                        if (white[i][j][4] == 201) {
                                            white[i][j][4] = 175;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i < mWidth - 1) {
                        if (map[i + 1][j] == 0) {
                            if (white[i + 1][j][2] >= 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] >= 173) {
                                        if (white[i][j][4] < 201) {
                                            white[i][j][4] = 201;
                                        }
                                    }
                                }
                            }
                            if (white[i + 1][j][2] == 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] == 173) {
                                        if (white[i][j][4] == 201) {
                                            white[i][j][4] = 175;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i < mWidth - 1 && j > 0) {
                        if (map[i + 1][j - 1] == 0) {
                            if (white[i + 1][j - 1][3] >= 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] >= 173) {
                                        if (white[i][j][4] < 201) {
                                            white[i][j][4] = 201;
                                        }
                                    }
                                }
                            }
                            if (white[i + 1][j - 1][3] == 173) {
                                for (int k = 0; k < 4; k++) {
                                    if (white[i][j][k] == 173) {
                                        if (white[i][j][4] == 201) {
                                            white[i][j][4] = 175;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < mWidth; i++) {
            for (int j = 0; j < mHeight; j++) {
                if (map[i][j] == 0) {
                    int counter = 0;
                    for (int k = j + 1; k < mHeight; k++) {

                        if (map[i][k] == Game.WHITE)
                            playerValue[0]++;
                        if (map[i][k] == 0)
                            break;
                        if (map[i][k] == Game.BLACK) {
                            counter++;
                            break;
                        }
                        if (k == mHeight - 1)
                            counter++;
                    }

                    for (int k = j - 1; k >= 0; k--) {

                        if (map[i][k] == Game.WHITE)
                            playerValue[0]++;
                        if (map[i][k] == 0)
                            break;
                        if (map[i][k] == Game.BLACK) {
                            counter++;
                            break;
                        }
                        if (k == 0)
                            counter++;
                    }
                    if (j == 0 || j == mHeight - 1)
                        counter++;
                    black[i][j][0] = plaValue[counter][playerValue[0]];
                    playerValue[0] = 0;
                    counter = 0;

                    for (int k = i + 1, l = j + 1; l < mHeight; k++, l++) {

                        if (k >= mWidth) {
                            break;
                        }
                        if (map[k][l] == Game.WHITE)
                            playerValue[1]++;
                        if (map[k][l] == 0)
                            break;
                        if (map[k][l] == Game.BLACK) {
                            counter++;
                            break;
                        }
                        if (k == mWidth - 1 || l == mHeight - 1)
                            counter++;
                    }


                    for (int k = i - 1, l = j - 1; l >= 0; k--, l--) {
                        if (k < 0) {
                            break;
                        }
                        if (map[k][l] == Game.WHITE)
                            playerValue[1]++;
                        if (map[k][l] == 0)
                            break;
                        if (map[k][l] == Game.BLACK) {
                            counter++;
                            break;
                        }
                        if (k == 0 || l == 0)
                            counter++;
                    }
                    if (i == 0 || i == mWidth - 1 || j == 0 || j == mHeight - 1)
                        counter++;

                    black[i][j][1] = plaValue[counter][playerValue[1]];
                    playerValue[1] = 0;
                    counter = 0;

                    for (int k = i + 1; k < mWidth; k++) {

                        if (map[k][j] == Game.WHITE)
                            playerValue[2]++;
                        if (map[k][j] == 0)
                            break;
                        if (map[k][j] == Game.BLACK) {
                            counter++;
                            break;
                        }
                        if (k == mWidth - 1)
                            counter++;
                    }


                    for (int k = i - 1; k >= 0; k--) {
                        if (map[k][j] == Game.WHITE)
                            playerValue[2]++;
                        if (map[k][j] == 0)
                            break;
                        if (map[k][j] == Game.BLACK) {
                            counter++;
                            break;
                        }
                        if (k == 0)
                            counter++;
                    }
                    if (i == 0 || i == mWidth - 1)
                        counter++;
                    black[i][j][2] = plaValue[counter][playerValue[2]];
                    playerValue[2] = 0;
                    counter = 0;

                    for (int k = i - 1, l = j + 1; l < mHeight; k--, l++) {

                        if (k < 0) {
                            break;
                        }
                        if (map[k][l] == Game.WHITE)
                            playerValue[3]++;
                        if (map[k][l] == 0)
                            break;
                        if (map[k][l] == Game.BLACK) {
                            counter++;
                            break;
                        }
                        if (k == 0 || l == mHeight - 1)
                            counter++;

                    }


                    for (int k = i + 1, l = j - 1; l >= 0; k++, l--) {

                        if (k >= mWidth) {
                            break;
                        }
                        if (map[k][l] == Game.WHITE)
                            playerValue[3]++;
                        if (map[k][l] == 0)
                            break;
                        if (map[k][l] == Game.BLACK) {
                            counter++;
                            break;
                        }
                        if (k == mWidth - 1 || l == 0)
                            counter++;

                    }
                    if (i == 0 || i == mWidth - 1 || j == 0 || j == mHeight - 1)
                        counter++;
                    black[i][j][3] = plaValue[counter][playerValue[3]];
                    playerValue[3] = 0;
                    counter = 0;

                    for (int k = 0; k < 4; k++) {
                        if (black[i][j][k] == 166)
                            counter++;
                    }
                    if (counter >= 2 && black[i][j][0] < 174) {
                        black[i][j][0] = 174;

                    }
                    counter = 0;

                    for (int k = 0; k < 4; k++) {
                        for (int l = 0; l < 4; l++) {
                            if (black[i][j][k] == 166 && black[i][j][l] == 167
                                    && black[i][j][0] < 176)
                                black[i][j][0] = 176;
                        }
                    }

                    for (int k = 0; k < 4; k++) {
                        for (int l = 0; l < 4; l++) {
                            if (black[i][j][k] == 166 && black[i][j][l] == 186
                                    && black[i][j][0] < 177)
                                black[i][j][0] = 177;
                        }
                    }

                    for (int k = 0; k < 4; k++) {
                        if (black[i][j][k] == 167)
                            counter++;
                    }
                    if (counter >= 2 && black[i][j][0] < 178)
                        black[i][j][0] = 178;
                    counter = 0;

                    for (int k = 0; k < 4; k++) {
                        for (int l = 0; l < 4; l++) {
                            if (black[i][j][k] == 167 && black[i][j][l] == 186
                                    && black[i][j][0] < 179)
                                black[i][j][0] = 179;
                        }
                    }

                    for (int k = 0; k < 4; k++) {
                        if (black[i][j][k] == 186)
                            counter++;
                    }
                    if (counter >= 2 && black[i][j][0] < 180)
                        black[i][j][0] = 180;
                    counter = 0;

                    if (j >= 1) {
                        if (map[i][j - 1] == 0) {
                            if (black[i][j - 1][0] >= 166) {
                                for (int k = 0; k < 4; k++) {
                                    if (black[i][j][k] >= 166 && black[i][j][k] < 176) {
                                        if (black[i][j][0] < 176) {
                                            black[i][j][0] = 176;
                                        }
                                    }
                                }
                            }
                        }

                    }

                    if (j >= 1 && i >= 1) {
                        if (map[i - 1][j - 1] == 0) {
                            if (black[i - 1][j - 1][1] >= 166) {
                                for (int k = 0; k < 4; k++) {
                                    if (black[i][j][k] >= 166) {
                                        if (black[i][j][0] < 176) {
                                            black[i][j][0] = 176;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i >= 1) {
                        if (map[i - 1][j] == 0) {
                            if (black[i - 1][j][2] >= 166) {
                                for (int k = 0; k < 4; k++) {
                                    if (black[i][j][k] >= 166) {
                                        if (black[i][j][0] < 176) {
                                            black[i][j][0] = 176;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i > 0 && j < mHeight - 1) {
                        if (map[i - 1][j + 1] == 0) {
                            if (black[i - 1][j + 1][3] >= 166) {
                                for (int k = 0; k < 4; k++) {
                                    if (black[i][j][k] >= 166) {
                                        if (black[i][j][0] < 176) {
                                            black[i][j][0] = 176;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (j < mHeight - 1) {
                        if (map[i][j + 1] == 0) {
                            if (black[i][j + 1][0] >= 166) {
                                for (int k = 0; k < 4; k++) {
                                    if (black[i][j][k] >= 166) {
                                        if (black[i][j][0] < 176) {
                                            black[i][j][0] = 176;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i < mWidth - 1 && j < mHeight - 1) {
                        if (map[i + 1][j + 1] == 0) {
                            if (black[i + 1][j + 1][1] >= 166) {
                                for (int k = 0; k < 4; k++) {
                                    if (black[i][j][k] >= 166) {
                                        if (black[i][j][0] < 176) {
                                            black[i][j][0] = 176;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i < mWidth - 1) {
                        if (map[i + 1][j] == 0) {
                            if (black[i + 1][j][2] >= 166) {
                                for (int k = 0; k < 4; k++) {
                                    if (black[i][j][k] >= 166) {
                                        if (black[i][j][0] < 176) {
                                            black[i][j][0] = 176;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (i < mWidth - 1 && j > 0) {
                        if (map[i + 1][j - 1] == 0) {
                            if (black[i + 1][j - 1][3] >= 166) {
                                for (int k = 0; k < 4; k++) {
                                    if (black[i][j][k] >= 166) {
                                        if (black[i][j][0] < 176) {
                                            black[i][j][0] = 176;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Coordinate getPosition(int[][] map) {
        int maxpSum = 0;
        int maxcSum = 0;
        int maxpValue = -10;
        int maxcValue = -10;
        int blackRow = 0;
        int blackCollum = 0;
        int whiteRow = 0;
        int whiteCollum = 0;
        for (int i = 0; i < mWidth; i++) {
            for (int j = 0; j < mHeight; j++) {
                if (map[i][j] == 0) {
                    for (int k = 0; k < 4; k++) {
                        if (black[i][j][k] > maxpValue) {
                            blackRow = i;
                            blackCollum = j;
                            maxpValue = black[i][j][k];
                            maxpSum = black[i][j][0] + black[i][j][1]
                                    + black[i][j][2] + black[i][j][3];
                        }

                        // if the value if equal, check the sum of the value
                        if (black[i][j][k] == maxpValue) {
                            if (maxpSum < (black[i][j][0] + black[i][j][1]
                                    + black[i][j][2] + black[i][j][3])) {
                                blackRow = i;
                                blackCollum = j;
                                maxpSum = black[i][j][0] + black[i][j][1]
                                        + black[i][j][2] + black[i][j][3];
                            }
                        }

                        if (white[i][j][k] > maxcValue) {
                            whiteRow = i;
                            whiteCollum = j;
                            maxcValue = white[i][j][k];
                            maxcSum = black[i][j][0] + black[i][j][1]
                                    + black[i][j][2] + black[i][j][3];

                        }


                        if (white[i][j][k] == maxcValue) {
                            if (maxcSum < (black[i][j][0] + black[i][j][1]
                                    + black[i][j][2] + black[i][j][3])) {
                                whiteRow = i;
                                whiteCollum = j;
                                maxcSum = black[i][j][0] + black[i][j][1]
                                        + black[i][j][2] + black[i][j][3];
                            }
                        }
                    }
                }
            }
        }

        Coordinate c = new Coordinate();
        if (maxcValue > maxpValue) {
            c.x = whiteRow;
            c.y = whiteCollum;
        } else {
            c.x = blackRow;
            c.y = blackCollum;
        }
        return c;
    }
}

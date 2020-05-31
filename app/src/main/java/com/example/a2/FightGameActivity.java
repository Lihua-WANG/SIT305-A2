package com.example.a2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a2.game.Game;
import com.example.a2.game.GameConstants;
import com.example.a2.game.GameView;
import com.example.a2.game.Player;

import java.io.IOException;

/**
 * This is two-player battle mode,
 * which needs two users play Go Bang on the same chessboard(mobile).
 */

public class FightGameActivity extends Activity implements OnClickListener {

    // Assign a tag to Fight game mode
    private static final String TAG = "FightGameActivity";

    // Define the main reference object
    GameView mGameView = null;
    Game mGame;
    Player black;
    Player white;

    // Define controls
    // The text of black or whit win times
    private TextView mBlackWin;
    private TextView mWhiteWin;

    // image of black and white chess pieces
    private ImageView mBlackActive;
    private ImageView mWhiteActive;

    // Control Button
    private Button restart;
    private Button rollback;

    // Called after the application is refreshed
    @SuppressLint("HandlerLeak")
    private Handler mRefreshHandler = new Handler() {

        public void handleMessage(Message msg) {
            Log.d(TAG, "refresh action=" + msg.what);
            switch (msg.what) {
                // If message is game over,
                // judge which part win,
                // Popup Dialog of win part message, play game sound and record adding 1 to win part
                case GameConstants.GAME_OVER:
                    if (msg.arg1 == Game.BLACK) {
                        showWinDialog("Black Win!");
                        playRing();
                        black.win();
                    } else if (msg.arg1 == Game.WHITE) {
                        showWinDialog("White Win!");
                        playRing();
                        white.win();
                    }
                    // Update the score display box
                    updateScore(black, white);
                    break;
                // If message is adding chess piece,
                // Update the current chess putting players
                case GameConstants.ADD_CHESS:
                    updateActive(mGame);
                    playPutRing();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_fight);
        initViews();
        initGame();
    }

    // Register the components and the listeners
    private void initViews() {
        mGameView = findViewById(R.id.game_view);
        mBlackWin = findViewById(R.id.black_win);
        mBlackActive = findViewById(R.id.black_active);
        mWhiteWin = findViewById(R.id.white_win);
        mWhiteActive = findViewById(R.id.white_active);
        restart = findViewById(R.id.restart);
        rollback = findViewById(R.id.rollback);
        restart.setOnClickListener(this);
        rollback.setOnClickListener(this);
    }

    // Register the players and images,
    // set the game mode, update the game page the score information
    private void initGame() {
        black = new Player(Game.BLACK);
        white = new Player(Game.WHITE);
        mGame = new Game(mRefreshHandler, black, white);
        // Set game mode is fight game
        mGame.setMode(GameConstants.MODE_FIGHT);
        mGameView.setGame(mGame);
        updateActive(mGame);
        updateScore(black, white);
    }

    // Update the game and game player turns in current
    private void updateActive(Game game) {
        // If it is black turn, invisible white
        if (game.getActive() == Game.BLACK) {
            mBlackActive.setVisibility(View.VISIBLE);
            mWhiteActive.setVisibility(View.INVISIBLE);
        } else { // Otherwise, invisible black
            mBlackActive.setVisibility(View.INVISIBLE);
            mWhiteActive.setVisibility(View.VISIBLE);
        }
    }

    // Update score
    private void updateScore(Player black, Player white) {
        mBlackWin.setText(black.getWin());
        mWhiteWin.setText(white.getWin());
    }

    // Create a dialog to show who win, and make decisions of continue game or exit.
    private void showWinDialog(String message) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);
        b.setMessage(message);
        b.setPositiveButton(R.string.Continue, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGame.reset();
                mGameView.drawGame();
            }
        });
        b.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        b.show();
    }

    // Create listener of rollback and restart a new game button
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // If click new game button
            case R.id.restart:
                // reset game
                mGame.reset();
                // update the game page
                updateActive(mGame);
                // update the score
                updateScore(black, white);
                // redraw the game page
                mGameView.drawGame();
                break;
            // If click rollback button
            case R.id.rollback:
                // regret chess piece
                mGame.rollback();
                // update the game page
                updateActive(mGame);
                // redraw the game page
                mGameView.drawGame();
                break;
            default:
                break;
        }
    }

    // Audios are all stored in assets.
    public AssetManager assetManager;

    // Play a short audio when end a round
    public MediaPlayer playRing() {
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("congrats.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    // Play the sound of playing chess piece
    public MediaPlayer playPutRing() {
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("put.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

}

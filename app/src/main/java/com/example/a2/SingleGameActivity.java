package com.example.a2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a2.game.ComputerAI;
import com.example.a2.game.Coordinate;
import com.example.a2.game.Game;
import com.example.a2.game.GameConstants;
import com.example.a2.game.GameView;
import com.example.a2.game.Player;

import java.io.IOException;

public class SingleGameActivity extends Activity implements OnClickListener {
    private static final String TAG = "SingleGameActivity";
    public SingleGameActivity singleGameAty = this;
    GameView mGameView = null;

    Game mGame;
    Player me;
    Player computer;
    ComputerAI ai;

    // Victory
    private TextView mBlackWin;
    private TextView mWhiteWin;

    // Current chess player
    private ImageView mBlackActive;
    private ImageView mWhiteActive;

    // Name
    private TextView mBlackName;
    private TextView mWhiteName;
    private TextView showtime;
    private TextView textView;

    // Control Button
    private Button restart;
    private Button rollback;

    private boolean isRollback;

    /**
     * Handle game callback information and refresh the interface
     */
    private Handler mComputerHandler;

    /**
     * Handle game callback information and refresh the interface
     */
    private Handler mRefreshHandler = new Handler() {

        public void handleMessage(Message msg) {
            Log.d(TAG, "refresh action=" + msg.what);
            switch (msg.what) {
                case GameConstants.GAME_OVER:
                    if (msg.arg1 == Game.BLACK) {
                        showWinDialog("Black Win!");
                        playCongratsRing();
                        me.win();
                    } else if (msg.arg1 == Game.WHITE) {
                        showWinDialog("White Win!");
                        playFailRing();
                        computer.win();
                    }

                    updateScore(me, computer);
                    break;
                case GameConstants.ACTIVE_CHANGE:
                    updateActive(mGame);
                    break;
                case GameConstants.ADD_CHESS:
                    playPutRing();
                    updateActive(mGame);
                    if (mGame.getActive() == computer.getType()) {
                        mComputerHandler.sendEmptyMessage(0);
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_single);
        singleGameAty = this;
        initViews();
        initGame();
        initComputer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    private void initViews() {
        mGameView = findViewById(R.id.game_view);
        mBlackName = findViewById(R.id.black_name);
        mBlackWin = findViewById(R.id.black_win);
        mBlackActive = findViewById(R.id.black_active);
        mWhiteName = findViewById(R.id.white_name);
        mWhiteWin = findViewById(R.id.white_win);
        mWhiteActive = findViewById(R.id.white_active);
        restart = findViewById(R.id.restart);
        rollback = findViewById(R.id.rollback);
        restart.setOnClickListener(this);
        rollback.setOnClickListener(this);
    }

    private void initGame() {
        me = new Player(getString(R.string.myself), Game.BLACK);
        computer = new Player(getString(R.string.computer), Game.WHITE);
        mGame = new Game(mRefreshHandler, me, computer);
        mGame.setMode(GameConstants.MODE_SINGLE);
        mGameView.setGame(mGame);
        updateActive(mGame);
        updateScore(me, computer);
        ai = new ComputerAI(mGame.getWidth(), mGame.getHeight());
    }

    private void initComputer() {
        HandlerThread thread = new HandlerThread("computerAi");
        thread.start();
        mComputerHandler = new ComputerHandler(thread.getLooper());
    }

    private void updateActive(Game game) {
        if (game.getActive() == Game.BLACK) {
            mBlackActive.setVisibility(View.VISIBLE);
            mWhiteActive.setVisibility(View.INVISIBLE);
        } else {
            mBlackActive.setVisibility(View.INVISIBLE);
            mWhiteActive.setVisibility(View.VISIBLE);
        }
    }

    private void updateScore(Player black, Player white) {
        mBlackWin.setText(black.getWin());
        mWhiteWin.setText(white.getWin());
    }

    @Override
    protected void onDestroy() {
        mComputerHandler.getLooper().quit();
        super.onDestroy();
    }

    private void showWinDialog(String message) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);
        b.setMessage(message);
        b.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGame.reset();
                mGameView.drawGame();
            }
        });
        b.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        b.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restart:
                mGame.reset();
                updateActive(mGame);
                updateScore(me, computer);
                mGameView.drawGame();
                break;
            case R.id.rollback:
                if (mGame.getActive() != me.getType()) {
                    isRollback = true;
                } else {
                    rollback();
                }
                break;
            default:
                break;
        }
    }

    private void rollback() {
        mGame.rollback();
        mGame.rollback();
        updateActive(mGame);
        mGameView.drawGame();
    }

    class ComputerHandler extends Handler {

        public ComputerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            ai.updateValue(mGame.getChessMap());
            Coordinate c = ai.getPosition(mGame.getChessMap());
            mGame.addChess(c, computer);
            mGameView.drawGame();
            if (isRollback) {
                rollback();
                isRollback = false;
            }
        }
    }

    public AssetManager assetManager;

    public MediaPlayer playCongratsRing() {
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

    public MediaPlayer playFailRing() {
        MediaPlayer mediaPlayer = null;
        try {
            mediaPlayer = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("fail.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

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

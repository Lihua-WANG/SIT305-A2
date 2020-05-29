package com.example.a2;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

/**
 * Create a subclass(BackgroundService) of Service class and override necessary methods.
 */

public class BackMusicService extends Service {
    //creating a mediaplayer object
    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public AssetManager assetManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            player = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("backgroundMusic.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            player.prepare();
            //setting loop play to true
            //this will make the ringtone continuously playing
            player.setLooping(true);
            //staring the player
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return player;
        //we have some options for service
        //start sticky means service will be explicitly started and stopped
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
        player.stop();
    }
}
package com.example.sricharans.magic_lantern; /**
 * Created by Srikanth S on 6/16/2016.
 */
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class ServiceMusic extends Service implements MediaPlayer.OnPreparedListener{
    private static final String ACTION_PLAY = "com.sricharans.example.act.play";

    private MediaPlayer mediaPlayer = null;
    private static ServiceMusic serviceMusic;
    private int length = 0;

    public ServiceMusic() {
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public static ServiceMusic getInstance(){
        return serviceMusic;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceMusic = this;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("hi","initiated");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(intent.getStringExtra("musicPath"));
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }
    public void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            length = mediaPlayer.getCurrentPosition();

        }
    }

    public void resumeMusic() {
        if (mediaPlayer.isPlaying() == false) {
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
        }
    }
    public void StopMusic(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
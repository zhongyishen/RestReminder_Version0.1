package com.szy.restreminder;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

//just for test
public class SoundThread extends MainActivity implements Runnable {
    @Override
    public void run() {
        AssetManager assetManager;
        MediaPlayer player = new MediaPlayer();
        assetManager = getResources().getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd("minder.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

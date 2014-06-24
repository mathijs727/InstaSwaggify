package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    MainActivity.java
 * This file contains a thread to play sounds without blocking the app.
 */

public class SoundThread extends Thread {
    MediaPlayer audioFile;

    public SoundThread (Context context, int resourceID) {
        MediaPlayer mp = MediaPlayer.create(context, resourceID);
        mp.setLooping(false);
        audioFile = mp;
    }

    public void run() {
        audioFile.start();
        while(audioFile.isPlaying());
        audioFile.release();
    }
}

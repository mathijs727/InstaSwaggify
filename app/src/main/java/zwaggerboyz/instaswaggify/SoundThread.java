package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This class plays a sound on a separate thread
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

package hu.respawncontrol;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {
    private static MusicManager instance = null;

    private Context context;
    private MediaPlayer mediaPlayer;
    private int startCounter;

    private MusicManager(Context context) {
        this.context = context.getApplicationContext();
        mediaPlayer = MediaPlayer.create(context, R.raw.theme_song);
    }

    public void onStart() {
        startCounter++;
        if (startCounter == 1) {
            startMusic();
        }
    }

    public void onStop() {
        startCounter--;
        if (startCounter == 0)
        {
            stopMusic();
        }
    }

    public static MusicManager getInstance(Context context) {
        if(instance == null) {
            instance = new MusicManager(context);
        }
        return instance;
    }

    public void startMusic() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.theme_song);
        }
        mediaPlayer.start();
    }

    public void stopMusic() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

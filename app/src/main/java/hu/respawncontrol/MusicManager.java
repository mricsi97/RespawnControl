package hu.respawncontrol;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.preference.PreferenceManager;

public class MusicManager {
    private static MusicManager instance = null;

    private final static int MAX_VOLUME = 100;

    private Context context;
    private MediaPlayer mediaPlayer;
    private int startCounter;

    private MusicManager(Context context) {
        this.context = context.getApplicationContext();
        mediaPlayer = MediaPlayer.create(context, R.raw.theme_song);
        mediaPlayer.setLooping(true);
        changeVolume();
    }

    public static MusicManager getInstance(Context context) {
        if(instance == null) {
            instance = new MusicManager(context);
        }
        return instance;
    }

    public void changeVolume() {
        int desiredVolume = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getInt("music_volume", 66);
        final float volume = (float) (1 - (Math.log(MAX_VOLUME + 1 - desiredVolume) / Math.log(MAX_VOLUME)));
        mediaPlayer.setVolume(volume, volume);
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

package hu.respawncontrol.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import hu.respawncontrol.MusicManager;
import hu.respawncontrol.R;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("music")) {
            boolean musicEnabled = sharedPreferences.getBoolean(key, false);
            if(musicEnabled) {
                MusicManager.getInstance(SettingsActivity.this).startMusic();
            } else {
                MusicManager.getInstance(SettingsActivity.this).stopMusic();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music", false);
        if(musicEnabled) {
            MusicManager musicManager = MusicManager.getInstance(this);
            musicManager.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music", false);
        if(musicEnabled) {
            MusicManager musicManager = MusicManager.getInstance(this);
            musicManager.onStop();
        }
    }
}

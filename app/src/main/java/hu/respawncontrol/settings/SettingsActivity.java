package hu.respawncontrol.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hu.respawncontrol.MusicManager;
import hu.respawncontrol.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        MusicManager musicManager = MusicManager.getInstance(this);
        musicManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        MusicManager musicManager = MusicManager.getInstance(this);
        musicManager.onStop();
    }
}

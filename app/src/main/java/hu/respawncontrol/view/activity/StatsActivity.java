package hu.respawncontrol.view.activity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import hu.respawncontrol.R;
import hu.respawncontrol.helper.MusicManager;
import hu.respawncontrol.view.adapter.SectionsPagerAdapter;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music_toggle", false);
        if(musicEnabled) {
            MusicManager musicManager = MusicManager.getInstance(this);
            musicManager.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music_toggle", false);
        if(musicEnabled) {
            MusicManager musicManager = MusicManager.getInstance(this);
            musicManager.onStop();
        }
    }
}
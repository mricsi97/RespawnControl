package hu.respawncontrol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.respawncontrol.adapter.LeaderboardAdapter;
import hu.respawncontrol.data.LeaderboardViewModel;
import hu.respawncontrol.data.room.entity.Result;
import hu.respawncontrol.data.room.helper.PlayerAndBestResult;

public class LeaderboardActivity extends AppCompatActivity {
    private static final String TAG = "LeaderboardActivity";

    private LeaderboardViewModel viewModel;

//    private int playerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

//        Intent intent = getIntent();
//        playerId = intent.getIntExtra(MainActivity.PLAYER_ID, -1);
//        if(playerId == -1) {
//            Log.e(TAG, "Invalid player ID passed from MainActivity");
//            return;
//        }

        final int playerId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("player_id", -1);
        if(playerId == -1) {
            Log.e(TAG, "Invalid player ID");
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final LeaderboardAdapter adapter = new LeaderboardAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(LeaderboardViewModel.class);

        viewModel.getAllPlayersAndBestResultsSorted().observe(this, new Observer<List<PlayerAndBestResult>>() {
            @Override
            public void onChanged(List<PlayerAndBestResult> playersAndBestResultsSorted) {
                adapter.setPlayersAndBestResultsSorted(playersAndBestResultsSorted);
            }
        });

        viewModel.getResultsForPlayer(playerId).observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                // Change other tab
            }
        });
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

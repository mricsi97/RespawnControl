package hu.respawncontrol.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import hu.respawncontrol.R;
import hu.respawncontrol.view.activity.MainActivity;
import hu.respawncontrol.view.activity.TimeTrialActivity;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        final Button btnTimeTrial = (Button) view.findViewById(R.id.btnTimeTrial);
        btnTimeTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), TimeTrialActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton btnSettings = (ImageButton) view.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchToSettingsFragment();
            }
        });

//        final ImageButton btnLeaderboards = (ImageButton) view.findViewById(R.id.btnLeaderboard);
//        btnSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity)getActivity()).switchToLeaderboardsFragment();
//            }
//        });

        final ImageButton btnStats = (ImageButton) view.findViewById(R.id.btnStats);
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchToStatsFragment();
            }
        });

        return view;
    }
}

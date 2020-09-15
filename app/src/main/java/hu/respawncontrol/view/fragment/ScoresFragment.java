package hu.respawncontrol.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.entity.Difficulty;
import hu.respawncontrol.model.room.entity.GameMode;
import hu.respawncontrol.model.room.entity.ItemTypeGroup;
import hu.respawncontrol.model.room.entity.Leaderboard;
import hu.respawncontrol.model.room.entity.Score;
import hu.respawncontrol.view.adapter.ScoreAdapter;
import hu.respawncontrol.viewmodel.ScoresViewModel;

public class ScoresFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_scores, container, false);

        final ScoresViewModel viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(ScoresViewModel.class);

        final Spinner spinnerGameMode = (Spinner) view.findViewById(R.id.spinner_scores_gameMode);
        final Spinner spinnerItemTypeGroup = (Spinner) view.findViewById(R.id.spinner_scores_itemTypeGroup);
        final Spinner spinnerDifficulty = (Spinner) view.findViewById(R.id.spinner_scores_difficulty);
        final ImageButton btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);

        viewModel.getAllGameModes().observe(getViewLifecycleOwner(),
                new Observer<List<GameMode>>() {
                    @Override
                    public void onChanged(List<GameMode> gameModes) {
                        List<String> gameModeNames = new ArrayList<>();
                        for (GameMode gameMode : gameModes) {
                            gameModeNames.add(gameMode.getName());
                        }

                        // Set game mode spinner content
                        ArrayAdapter<String> gameModeAdapter = new ArrayAdapter<>(ScoresFragment.this.getActivity(),
                                R.layout.spinner_item_custom, gameModeNames);
                        gameModeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
                        spinnerGameMode.setAdapter(gameModeAdapter);
                    }
                });

        viewModel.getAllItemTypeGroups().observe(getViewLifecycleOwner(),
                new Observer<List<ItemTypeGroup>>() {
                    @Override
                    public void onChanged(List<ItemTypeGroup> itemTypeGroups) {
                        List<String> itemTypeGroupNames = new ArrayList<>();
                        for (ItemTypeGroup itemTypeGroup : itemTypeGroups) {
                            itemTypeGroupNames.add(itemTypeGroup.getName());
                        }

                        // Set item type group spinner content
                        ArrayAdapter<String> itemTypeGroupAdapter = new ArrayAdapter<>(ScoresFragment.this.getActivity(),
                                R.layout.spinner_item_custom, itemTypeGroupNames);
                        itemTypeGroupAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
                        spinnerItemTypeGroup.setAdapter(itemTypeGroupAdapter);
                    }
                });

        spinnerGameMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setSelectedGameMode(position);
                viewModel.getDifficulties().observe(getViewLifecycleOwner(),
                        new Observer<List<Difficulty>>() {
                            @Override
                            public void onChanged(List<Difficulty> difficulties) {
                                List<String> difficultyNames = new ArrayList<>();
                                for (Difficulty difficulty : difficulties) {
                                    difficultyNames.add(difficulty.getName());
                                }

                                // Set difficulty spinner content
                                ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(ScoresFragment.this.getActivity(),
                                        R.layout.spinner_item_custom, difficultyNames);
                                difficultyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
                                spinnerDifficulty.setAdapter(difficultyAdapter);
                            }
                        });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerItemTypeGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setSelectedItemTypeGroup(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setSelectedDifficulty(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerScores);
        final ScoreAdapter scoreAdapter = new ScoreAdapter();
        recyclerView.setAdapter(scoreAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isTimeTrial = viewModel.determineIfTimeTrial();

                final TextView tvDifficultyHeader = (TextView) view.findViewById(R.id.tvDifficultyHeader_scores);
                tvDifficultyHeader.setText(isTimeTrial ? "Test Amount" : "Difficulty");

                scoreAdapter.setGameMode(isTimeTrial);

                viewModel.getLeaderboard().observe(getViewLifecycleOwner(),
                        new Observer<Leaderboard>() {
                            @Override
                            public void onChanged(Leaderboard leaderboard) {
                                if(leaderboard == null) {
                                    return;
                                }
                                viewModel.getScores(leaderboard).observe(getViewLifecycleOwner(),
                                        new Observer<List<Score>>() {
                                            @Override
                                            public void onChanged(List<Score> scores) {
                                                scoreAdapter.setScores(scores);
                                            }
                                        });
                            }
                        });
            }
        });

        return view;
    }
}

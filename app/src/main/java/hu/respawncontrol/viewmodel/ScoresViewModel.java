package hu.respawncontrol.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hu.respawncontrol.model.Repository;
import hu.respawncontrol.model.room.entity.Difficulty;
import hu.respawncontrol.model.room.entity.GameMode;
import hu.respawncontrol.model.room.entity.ItemGroup;
import hu.respawncontrol.model.room.entity.Leaderboard;
import hu.respawncontrol.model.room.entity.Score;

public class ScoresViewModel extends AndroidViewModel {
    private Repository repository;

    private LiveData<List<GameMode>> gameModes;
    private LiveData<List<ItemGroup>> itemGroups;
    private LiveData<List<Difficulty>> difficulties;

    private GameMode selectedGameMode;
    private ItemGroup selectedItemGroup;
    private Difficulty selectedDifficulty;

    public ScoresViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }

    public void setSelectedGameMode(int position) {
        selectedGameMode = gameModes.getValue().get(position);
    }

    public void setSelectedItemGroup(int position) {
        selectedItemGroup = itemGroups.getValue().get(position);
    }

    public void setSelectedDifficulty(int position) {
        selectedDifficulty = difficulties.getValue().get(position);
    }

    public LiveData<List<GameMode>> getAllGameModes() {
        if(gameModes == null) {
            gameModes = repository.getAllGameModes();
        }
        return gameModes;
    }

    public LiveData<List<ItemGroup>> getAllItemGroups() {
        if(itemGroups == null) {
            itemGroups = repository.getAllItemGroups();
        }
        return itemGroups;
    }

    public LiveData<List<Difficulty>> getDifficulties() {
        difficulties = repository.getDifficultiesByGameModeId(selectedGameMode.getId());
        return difficulties;
    }

    public LiveData<List<Score>> getScores(Leaderboard leaderboard) {
        return repository.getScoresByLeaderboard(leaderboard.getId());
    }

    public boolean determineIfTimeTrial() {
        return selectedGameMode.getName().equals("Time Trial");
    }

    public LiveData<Leaderboard> getLeaderboard() {
        return repository.getLeaderboardLiveData(selectedGameMode.getId(), selectedItemGroup.getItemGroupId(), selectedDifficulty.getId());
    }
}

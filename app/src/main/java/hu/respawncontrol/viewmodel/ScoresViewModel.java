package hu.respawncontrol.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hu.respawncontrol.helper.HelperMethods;
import hu.respawncontrol.model.Repository;
import hu.respawncontrol.model.room.entity.Difficulty;
import hu.respawncontrol.model.room.entity.GameMode;
import hu.respawncontrol.model.room.entity.Item;
import hu.respawncontrol.model.room.entity.ItemTypeGroup;
import hu.respawncontrol.model.room.entity.Leaderboard;
import hu.respawncontrol.model.room.entity.Score;

public class ScoresViewModel extends AndroidViewModel {
    private Repository repository;

    private LiveData<List<GameMode>> gameModes;
    private LiveData<List<ItemTypeGroup>> itemTypeGroups;
    private LiveData<List<Difficulty>> difficulties;

    private GameMode selectedGameMode;
    private ItemTypeGroup selectedItemTypeGroup;
    private Difficulty selectedDifficulty;

    public ScoresViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }

    public void setSelectedGameMode(int position) {
        selectedGameMode = gameModes.getValue().get(position);
    }

    public void setSelectedItemTypeGroup(int position) {
        selectedItemTypeGroup = itemTypeGroups.getValue().get(position);
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

    public LiveData<List<ItemTypeGroup>> getAllItemTypeGroups() {
        if(itemTypeGroups == null) {
            itemTypeGroups = repository.getAllItemTypeGroups();
        }
        return itemTypeGroups;
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
        return repository.getLeaderboardLiveData(selectedGameMode.getId(), selectedItemTypeGroup.getItemTypeGroupId(), selectedDifficulty.getId());
    }
}

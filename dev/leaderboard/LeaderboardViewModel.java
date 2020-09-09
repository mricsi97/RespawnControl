package hu.respawncontrol.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hu.respawncontrol.data.room.entity.Result;
import hu.respawncontrol.data.room.helper.PlayerAndBestResult;

public class LeaderboardViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<PlayerAndBestResult>> playersAndBestResultsSorted;
    private LiveData<List<Result>> resultsByPlayerId;

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        playersAndBestResultsSorted = repository.getAllPlayersAndBestResultsSorted();
    }

//    public void insertResult(Result result) {
//        repository.insertResult(result);
//    }

    public LiveData<List<Result>> getResultsForPlayer(int id) {
        return repository.getResultsByPlayerId(id);
    }

//    public void insertPlayer(Player player) {
//        repository.insertPlayer(player);
//    }
//
//    public void deletePlayer(Player player) {
//        repository.deletePlayer(player);
//    }

//    public List<PlayerAndBestResult> getAllPlayersAndBestResults() {
//        return repository.getAllPlayersAndBestResults();
//    }

    public LiveData<List<PlayerAndBestResult>> getAllPlayersAndBestResultsSorted() {
        return playersAndBestResultsSorted;
    }

//    public void insertItem(Item item) {
//        repository.insertItem(item);
//    }
//
//    public void insertItemType(ItemType itemType) {
//        repository.insertItemType(itemType);
//    }
}

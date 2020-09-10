package hu.respawncontrol.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import hu.respawncontrol.model.Repository;
import hu.respawncontrol.model.room.entity.Difficulty;
import hu.respawncontrol.model.room.entity.GameMode;
import hu.respawncontrol.model.room.entity.Item;
import hu.respawncontrol.model.room.entity.ItemType;
import hu.respawncontrol.model.room.entity.Leaderboard;
import hu.respawncontrol.model.room.entity.Result;
import hu.respawncontrol.model.room.entity.Score;
import hu.respawncontrol.model.room.helper.ItemTypeGroupWithItemTypes;

public class TimeTrialViewModel extends AndroidViewModel {
    public static final String TAG = "TimeTrialViewModel";

    private Repository repository;

    private static Random random;
    private Long date;

    private Leaderboard leaderboard;
    private GameMode gameMode;

    private LiveData<List<ItemTypeGroupWithItemTypes>> allItemTypeGroupsWithItemTypes;
    private LiveData<List<Difficulty>> difficulties;

    private ItemTypeGroupWithItemTypes selectedItemTypeGroupWithItemTypes;
    private Difficulty selectedDifficulty;
    private Integer selectedTestAmount;

    private MutableLiveData<List<Item>> testItems;
    private MutableLiveData<List<Long>> pickupMoments;
    private MutableLiveData<List<Long>> respawnMoments;
    private ArrayList<Long> solveTimes;

    private MutableLiveData<Long> solveTimeSum;
    private MutableLiveData<Long> averageSolveTime;
    private MutableLiveData<List<Long>> solveTimeDifferences;
    private MutableLiveData<List<Result>> results;

    public TimeTrialViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);

        random = new Random();
        date = Calendar.getInstance().getTime().getTime();

        testItems = new MutableLiveData<>();
        pickupMoments = new MutableLiveData<>();
        respawnMoments = new MutableLiveData<>();
        solveTimeSum = new MutableLiveData<>();
        averageSolveTime = new MutableLiveData<>();
        solveTimeDifferences = new MutableLiveData<>();
        results = new MutableLiveData<>();

        gameMode = repository.getGameModeByName("Time Trial");
    }

    public void prepareTests() {
        List<ItemType> itemTypes = selectedItemTypeGroupWithItemTypes.itemTypes;

        ArrayList<Item> testItems = new ArrayList<>();
        ArrayList<Long> pickupMoments = new ArrayList<>();
        ArrayList<Long> respawnMoments = new ArrayList<>();
        for(int i = 0; i < selectedTestAmount; i++) {
            // Select random item type and item
            ItemType randomItemType = itemTypes.get(random.nextInt(itemTypes.size()));

            ArrayList<Item> items = (ArrayList<Item>) repository.getItemsByItemTypeId(randomItemType.getItemTypeId());
            Item randomItem = items.get(random.nextInt(items.size()));
            testItems.add(randomItem);

            // Create random time for pickup in milliseconds
            long respawnTime = randomItem.getRespawnTimeInSeconds() * 1000;
            long randomPickupMoment = random.nextInt(780) * 1000;
            pickupMoments.add(randomPickupMoment);

            // Calculate time for respawn (solution)
            long respawnMoment = randomPickupMoment + respawnTime;
            respawnMoments.add(respawnMoment);
        }

        this.testItems.setValue(testItems);
        this.pickupMoments.setValue(pickupMoments);
        this.respawnMoments.setValue(respawnMoments);

        if(!selectedDifficulty.getName().equals("Custom")) {
            setLeaderboard();
        }
    }

    private void setLeaderboard() {
        int itemTypeGroupId = selectedItemTypeGroupWithItemTypes.itemTypeGroup.getItemTypeGroupId();
        int difficultyId = selectedDifficulty.getId();
        this.leaderboard = repository.getLeaderboard(gameMode.getId(), itemTypeGroupId, difficultyId);
    }

    public void calculateSolveTimeDifferences() {
        ArrayList<Long> solveTimeDifferencesValue = new ArrayList<>();
        for (int i = 0; i < solveTimes.size(); i++) {
            Long solveTime = solveTimes.get(i);
            long difference;
            if (i == 0) {
                difference = solveTime;
            } else {
                difference = solveTime - solveTimes.get(i-1);
            }
            solveTimeDifferencesValue.add(difference);
        }

        Long solveTimeSumValue = solveTimes.get(solveTimes.size() - 1);

        solveTimeDifferences.setValue(solveTimeDifferencesValue);
        solveTimeSum.setValue(solveTimeSumValue);
        averageSolveTime.setValue(solveTimeSumValue / solveTimes.size());
    }

    public void saveScore() {
        if(!selectedDifficulty.getName().equals("Custom")) {
            repository.insertScore(new Score(solveTimeSum.getValue(), testItems.getValue().size(), leaderboard.getId(), date));
        }
    }

    public LiveData<List<ItemTypeGroupWithItemTypes>> getAllItemTypeGroupsWithItemTypes() {
        if(allItemTypeGroupsWithItemTypes == null) {
            allItemTypeGroupsWithItemTypes = repository.getAllItemTypeGroupsWithItemTypes();
        }
        return allItemTypeGroupsWithItemTypes;
    }

    public LiveData<List<Difficulty>> getDifficulties() {
        if(difficulties == null) {
            difficulties = repository.getDifficultiesByGameModeId(gameMode.getId());
        }
        return difficulties;
    }

    public void saveResults() {
        ArrayList<Long> solveTimeDifferenceValues = (ArrayList<Long>) solveTimeDifferences.getValue();

        ArrayList<Result> resultValues = new ArrayList<>();
        for (int i = 0; i < solveTimes.size(); i++) {
            Result result = new Result(solveTimeDifferenceValues.get(i), testItems.getValue().get(i).getItemId(), date);
            resultValues.add(result);
        }

        results.setValue(resultValues);
        repository.insertResults(resultValues);
    }

    public void setSelectedItemTypeGroupWithItemTypes(int position) {
        if(allItemTypeGroupsWithItemTypes != null) {
            selectedItemTypeGroupWithItemTypes = allItemTypeGroupsWithItemTypes.getValue().get(position);
        }
    }

    public void setSelectedDifficulty(Difficulty difficulty) {
        this.selectedDifficulty = difficulty;
    }

    public void setSelectedTestAmount(int selectedTestAmount) {
        this.selectedTestAmount = selectedTestAmount;
    }

    public MutableLiveData<Long> getSolveTimeSum() {
        return solveTimeSum;
    }

    public MutableLiveData<List<Item>> getTestItems() {
        return testItems;
    }

    public MutableLiveData<List<Long>> getRespawnMoments() {
        return respawnMoments;
    }

    public MutableLiveData<List<Long>> getPickupMoments() {
        return pickupMoments;
    }

    public void setSolveTimes(ArrayList<Long> solveTimes) {
        this.solveTimes = solveTimes;
    }

    public MutableLiveData<List<Result>> getResults() {
        return results;
    }

    public MutableLiveData<Long> getAverageSolveTime() {
        return averageSolveTime;
    }
}

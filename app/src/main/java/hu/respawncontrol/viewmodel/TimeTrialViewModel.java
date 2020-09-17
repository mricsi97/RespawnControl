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
import hu.respawncontrol.model.room.entity.ItemGroup;
import hu.respawncontrol.model.room.entity.Leaderboard;
import hu.respawncontrol.model.room.entity.Result;
import hu.respawncontrol.model.room.entity.Score;
import hu.respawncontrol.model.room.helper.ItemGroupWithItems;

public class TimeTrialViewModel extends AndroidViewModel {
    public static final String TAG = "TimeTrialViewModel";

    private Repository repository;

    private static Random random;
    private Long date;

    private Leaderboard leaderboard;
    private GameMode gameMode;

    private LiveData<List<ItemGroupWithItems>> allItemGroupsWithItems;
    private LiveData<List<Difficulty>> difficulties;

    private ItemGroupWithItems selectedItemGroupWithItems;
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
        ItemGroup itemGroup = selectedItemGroupWithItems.itemGroup;
        List<Item> itemsInItemGroup = selectedItemGroupWithItems.items;
        List<Integer> frequencies = repository.getFrequenciesByItemGroup(itemGroup.getItemGroupId());

        // Random item picking setup
        int frequencySum = 0; // 85
        List<Integer> ranges = new ArrayList<>();
        for(int i = 0; i < itemsInItemGroup.size(); i++) {
            frequencySum += frequencies.get(i);
            ranges.add(frequencySum);
        }

        List<Long> pickupMoments = new ArrayList<>();
        List<Long> respawnMoments = new ArrayList<>();
        List<Item> testItems = new ArrayList<>();
        for(int i = 0; i < selectedTestAmount; i++) {
            int rand = random.nextInt(frequencySum);

            Item testItem = null;
            for(int j = 0; j < ranges.size(); j++) {
                if(rand < ranges.get(j)) {
                    testItem = itemsInItemGroup.get(j);
                    break;
                }
            }

            testItems.add(testItem);

            // Create random time for pickup in milliseconds
            long respawnTime = testItem.getRespawnTimeInSeconds() * 1000;
            long randomPickupMoment = random.nextInt(780) * 1000;
            pickupMoments.add(randomPickupMoment);

            // Calculate time for respawn (solution)
            long respawnMoment = randomPickupMoment + respawnTime;
            respawnMoments.add(respawnMoment);
        }

        this.testItems.setValue(testItems);
        this.pickupMoments.setValue(pickupMoments);
        this.respawnMoments.setValue(respawnMoments);

        setLeaderboard();
    }

    private void setLeaderboard() {
        int itemGroupId = selectedItemGroupWithItems.itemGroup.getItemGroupId();
        int difficultyId = selectedDifficulty.getId();
        this.leaderboard = repository.getLeaderboard(gameMode.getId(), itemGroupId, difficultyId);
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
        repository.insertScore(new Score(solveTimeSum.getValue(), testItems.getValue().size(), leaderboard.getId(), date));
    }

    public LiveData<List<ItemGroupWithItems>> getAllItemGroupsWithItems() {
        if(allItemGroupsWithItems == null) {
            allItemGroupsWithItems = repository.getAllItemGroupsWithItems();
        }
        return allItemGroupsWithItems;
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

    public void setSelectedItemGroupWithItems(int position) {
        if(allItemGroupsWithItems != null) {
            selectedItemGroupWithItems = allItemGroupsWithItems.getValue().get(position);
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

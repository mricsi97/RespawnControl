package hu.respawncontrol.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.Calendar;
import java.util.List;

import hu.respawncontrol.helper.HelperMethods;
import hu.respawncontrol.model.Repository;
import hu.respawncontrol.model.room.entity.Item;

public class ItemStatsViewModel extends AndroidViewModel {
    private Repository repository;

    private Long currentTime;
    private Long weekAgoTime;
    private Long threeMonthsAgoTime;

    private LiveData<List<Item>> allItems;
    private LiveData<List<Item>> itemsPastWeek;
    private LiveData<List<Item>> itemsPastSeason;

    private LiveData<List<Long>> averageLiveDataTotal;
    private LiveData<List<Long>> bestLiveDataTotal;
    private LiveData<List<Long>> worstLiveDataTotal;

    private LiveData<List<Long>> averageLiveDataWeek;
    private LiveData<List<Long>> bestLiveDataWeek;
    private LiveData<List<Long>> worstLiveDataWeek;

    private LiveData<List<Long>> averageLiveDataSeason;
    private LiveData<List<Long>> bestLiveDataSeason;
    private LiveData<List<Long>> worstLiveDataSeason;

    MediatorLiveData<List<Long>[]> itemStats;

    public ItemStatsViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
        currentTime = Calendar.getInstance().getTime().getTime();
        weekAgoTime = currentTime - 604800000L;
        threeMonthsAgoTime = currentTime - 7257600000L;
    }

    public LiveData<List<Item>> getItemsHavingResults(int tabPosition) {
        switch (tabPosition) {
            case 0:
                return getAllItemsHavingResults();
            case 1:
                return getItemsHavingResultsPastWeek();
            default:
                return getItemsHavingResultsPastSeason();
        }
    }

    public LiveData<List<Long>[]> getItemStats(int tabPosition) {
        switch (tabPosition) {
            case 0:
                itemStats = HelperMethods.zipLiveData(getAllAverageTimes(), getAllBestTimes(), getAllWorstTimes());
                break;
            case 1:
                itemStats = HelperMethods.zipLiveData(getAverageTimesPastWeek(), getBestTimesPastWeek(), getWorstTimesPastWeek());
                break;
            default:
                itemStats = HelperMethods.zipLiveData(getAverageTimesPastSeason(), getBestTimesPastSeason(), getWorstTimesPastSeason());
                break;
        }

        return itemStats;
    }

    private LiveData<List<Item>> getAllItemsHavingResults() {
        if(allItems == null) {
            allItems = repository.getAllItemsHavingResults();
        }
        return allItems;
    }

    private LiveData<List<Item>> getItemsHavingResultsPastWeek() {
        if(itemsPastWeek == null) {
            itemsPastWeek = repository.getItemsHavingResultsInTimePeriod(weekAgoTime, currentTime);
        }
        return itemsPastWeek;
    }

    private LiveData<List<Item>> getItemsHavingResultsPastSeason() {
        if(itemsPastSeason == null) {
            itemsPastSeason = repository.getItemsHavingResultsInTimePeriod(threeMonthsAgoTime, currentTime);
        }
        return itemsPastSeason;
    }

    private LiveData<List<Long>> getAllAverageTimes() {
        if(averageLiveDataTotal == null) {
            averageLiveDataTotal = repository.getAllAverageTimes();
        }
        return averageLiveDataTotal;
    }

    private LiveData<List<Long>> getAllBestTimes() {
        if(bestLiveDataTotal == null) {
            bestLiveDataTotal = repository.getAllBestTimes();
        }
        return bestLiveDataTotal;
    }

    private LiveData<List<Long>> getAllWorstTimes() {
        if(worstLiveDataTotal == null) {
            worstLiveDataTotal = repository.getAllWorstTimes();
        }
        return worstLiveDataTotal;
    }

    private LiveData<List<Long>> getAverageTimesPastWeek() {
        if(averageLiveDataWeek == null) {
            averageLiveDataWeek = repository.getAverageTimesInTimePeriod(weekAgoTime, currentTime);
        }
        return averageLiveDataWeek;
    }

    private LiveData<List<Long>> getBestTimesPastWeek() {
        if(bestLiveDataWeek == null) {
            bestLiveDataWeek = repository.getBestTimesInTimePeriod(weekAgoTime, currentTime);
        }
        return bestLiveDataWeek;
    }

    private LiveData<List<Long>> getWorstTimesPastWeek() {
        if(worstLiveDataWeek == null) {
            worstLiveDataWeek = repository.getWorstTimesInTimePeriod(weekAgoTime, currentTime);
        }
        return worstLiveDataWeek;
    }

    private LiveData<List<Long>> getAverageTimesPastSeason() {
        if(averageLiveDataSeason == null) {
            averageLiveDataSeason = repository.getAverageTimesInTimePeriod(threeMonthsAgoTime, currentTime);
        }
        return averageLiveDataSeason;
    }

    private LiveData<List<Long>> getBestTimesPastSeason() {
        if(bestLiveDataSeason == null) {
            bestLiveDataSeason = repository.getBestTimesInTimePeriod(threeMonthsAgoTime, currentTime);
        }
        return bestLiveDataSeason;
    }

    private LiveData<List<Long>> getWorstTimesPastSeason() {
        if(worstLiveDataSeason == null) {
            worstLiveDataSeason = repository.getWorstTimesInTimePeriod(threeMonthsAgoTime, currentTime);
        }
        return worstLiveDataSeason;
    }
}

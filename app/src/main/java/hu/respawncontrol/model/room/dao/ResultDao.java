package hu.respawncontrol.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.respawncontrol.model.room.entity.Result;

@Dao
public interface ResultDao {

    @Insert
    void insert(List<Result> results);

    @Query("SELECT AVG(Result.solveTime) " +
            "FROM Result JOIN Item ON Result.itemId = Item.itemId " +
            "GROUP BY Item.itemId")
    LiveData<List<Long>> getAllAverageTimes();

    @Query("SELECT MIN(Result.solveTime) " +
            "FROM Result JOIN Item ON Result.itemId = Item.itemId " +
            "GROUP BY Item.itemId")
    LiveData<List<Long>> getAllBestTimes();

    @Query("SELECT MAX(Result.solveTime) " +
            "FROM Result JOIN Item ON Result.itemId = Item.itemId " +
            "GROUP BY Item.itemId")
    LiveData<List<Long>> getAllWorstTimes();

    @Query("SELECT AVG(Result.solveTime) " +
            "FROM Result JOIN Item ON Result.itemId = Item.itemId " +
            "WHERE Result.date BETWEEN :fromTime AND :toTime " +
            "GROUP BY Item.itemId")
    LiveData<List<Long>> getAverageTimesInTimePeriod(Long fromTime, Long toTime);

    @Query("SELECT MIN(Result.solveTime) " +
            "FROM Result JOIN Item ON Result.itemId = Item.itemId " +
            "WHERE Result.date BETWEEN :fromTime AND :toTime " +
            "GROUP BY Item.itemId")
    LiveData<List<Long>> getBestTimesInTimePeriod(Long fromTime, Long toTime);

    @Query("SELECT MAX(Result.solveTime) " +
            "FROM Result JOIN Item ON Result.itemId = Item.itemId " +
            "WHERE Result.date BETWEEN :fromTime AND :toTime " +
            "GROUP BY Item.itemId")
    LiveData<List<Long>> getWorstTimesInTimePeriod(Long fromTime, Long toTime);
}

package hu.respawncontrol.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.respawncontrol.model.room.entity.Score;

@Dao
public interface ScoreDao {

    @Insert
    void insert(Score score);

    @Query("SELECT * " +
            "FROM Score " +
            "WHERE leaderboardId = :leaderboardId " +
            "ORDER BY date DESC")
    LiveData<List<Score>> getScoresByLeaderboard(int leaderboardId);
}

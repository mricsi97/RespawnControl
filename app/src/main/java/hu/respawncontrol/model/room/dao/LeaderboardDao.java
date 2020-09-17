package hu.respawncontrol.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import hu.respawncontrol.model.room.entity.Leaderboard;

@Dao
public interface LeaderboardDao {

    @Insert
    void insert(Leaderboard leaderboard);

    @Query("SELECT * FROM Leaderboard WHERE gameModeId = :gameModeId AND itemGroupId = :itemGroupId AND difficultyId = :difficultyId")
    Leaderboard getLeaderboard(int gameModeId, int itemGroupId, int difficultyId);

    @Query("SELECT * FROM Leaderboard WHERE gameModeId = :gameModeId AND itemGroupId = :itemGroupId AND difficultyId = :difficultyId")
    LiveData<Leaderboard> getLeaderboardLiveData(int gameModeId, int itemGroupId, int difficultyId);
}
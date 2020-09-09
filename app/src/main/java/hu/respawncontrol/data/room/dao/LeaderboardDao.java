package hu.respawncontrol.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import hu.respawncontrol.data.room.entity.Leaderboard;

@Dao
public interface LeaderboardDao {

    @Insert
    void insert(Leaderboard leaderboard);

    @Query("SELECT * FROM Leaderboard WHERE gameModeId = :gameModeId AND itemTypeGroupId = :itemTypeGroupId AND difficultyId = :difficultyId")
    Leaderboard getLeaderboard(int gameModeId, int itemTypeGroupId, int difficultyId);
}
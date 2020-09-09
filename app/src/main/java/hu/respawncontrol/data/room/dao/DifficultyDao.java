package hu.respawncontrol.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.respawncontrol.data.room.entity.Difficulty;

@Dao
public interface DifficultyDao {

    @Insert
    long insert(Difficulty difficulty);

    @Query("SELECT * FROM Difficulty WHERE gameModeId = :gameModeId")
    LiveData<List<Difficulty>> getDifficultiesByGameModeId(int gameModeId);
}

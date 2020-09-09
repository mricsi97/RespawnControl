package hu.respawncontrol.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import hu.respawncontrol.data.room.entity.GameMode;

@Dao
public interface GameModeDao {

    @Insert
    long insert(GameMode gameMode);

    @Query("SELECT * FROM GameMode WHERE name = :name")
    GameMode getGameModeByName(String name);
}

package hu.respawncontrol.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import hu.respawncontrol.data.room.entity.Score;

@Dao
public interface ScoreDao {

    @Insert
    void insert(Score score);
}

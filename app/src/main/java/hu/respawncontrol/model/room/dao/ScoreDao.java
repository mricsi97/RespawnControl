package hu.respawncontrol.model.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import hu.respawncontrol.model.room.entity.Score;

@Dao
public interface ScoreDao {

    @Insert
    void insert(Score score);
}

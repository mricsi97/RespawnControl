package hu.respawncontrol.model.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import java.util.List;

import hu.respawncontrol.model.room.entity.Result;

@Dao
public interface ResultDao {

    @Insert
    void insert(List<Result> results);
}

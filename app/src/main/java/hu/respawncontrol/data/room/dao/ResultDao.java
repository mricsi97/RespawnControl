package hu.respawncontrol.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import java.util.List;

import hu.respawncontrol.data.room.entity.Result;

@Dao
public interface ResultDao {

    @Insert
    void insert(List<Result> results);
}

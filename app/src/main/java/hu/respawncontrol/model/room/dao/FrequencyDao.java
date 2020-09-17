package hu.respawncontrol.model.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.respawncontrol.model.room.entity.Frequency;

@Dao
public interface FrequencyDao {

    @Insert
    void insert(Frequency frequency);

    @Query("SELECT frequency " +
            "FROM Frequency " +
            "WHERE itemGroupId = :itemGroupId")
    List<Integer> getFrequenciesByItemGroupId(int itemGroupId);
}

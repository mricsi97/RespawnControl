package hu.respawncontrol.model.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.respawncontrol.model.room.entity.ItemCrossItemType;
import hu.respawncontrol.model.room.entity.ItemType;

@Dao
public interface ItemTypeDao {

    @Insert
    long insert(ItemType itemType);

    @Insert
    void insert(ItemCrossItemType itemCrossItemType);

    @Query("SELECT * FROM ItemType")
    List<ItemType> getAllItemTypes();
}

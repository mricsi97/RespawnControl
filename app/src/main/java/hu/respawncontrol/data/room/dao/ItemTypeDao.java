package hu.respawncontrol.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.respawncontrol.data.room.entity.ItemCrossItemType;
import hu.respawncontrol.data.room.entity.ItemType;

@Dao
public interface ItemTypeDao {

    @Insert
    long insert(ItemType itemType);

    @Insert
    void insert(ItemCrossItemType itemCrossItemType);

    @Query("SELECT * FROM ItemType")
    List<ItemType> getAllItemTypes();
}

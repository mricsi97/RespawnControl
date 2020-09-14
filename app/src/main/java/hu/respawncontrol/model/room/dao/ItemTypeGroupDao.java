package hu.respawncontrol.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import hu.respawncontrol.model.room.entity.ItemTypeCrossItemTypeGroup;
import hu.respawncontrol.model.room.entity.ItemTypeGroup;
import hu.respawncontrol.model.room.helper.ItemTypeGroupWithItemTypes;

@Dao
public interface ItemTypeGroupDao {

    @Insert
    long insert(ItemTypeGroup itemTypeGroup);

    @Insert
    void insert(ItemTypeCrossItemTypeGroup itemTypeCrossItemTypeGroup);

    @Transaction
    @Query("SELECT * FROM ItemTypeGroup")
    LiveData<List<ItemTypeGroupWithItemTypes>> getAllItemTypeGroupsWithItemTypes();

    @Query("SELECT * FROM ItemTypeGroup")
    LiveData<List<ItemTypeGroup>> getAllItemTypeGroups();
}

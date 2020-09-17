package hu.respawncontrol.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import hu.respawncontrol.model.room.entity.ItemGroup;
import hu.respawncontrol.model.room.helper.ItemGroupWithItems;

@Dao
public interface ItemGroupDao {

    @Insert
    long insert(ItemGroup itemGroup);

    @Query("SELECT * FROM ItemGroup")
    LiveData<List<ItemGroup>> getAllItemGroups();

    @Transaction
    @Query("SELECT * FROM ItemGroup")
    LiveData<List<ItemGroupWithItems>> getAllItemGroupsWithItems();
}

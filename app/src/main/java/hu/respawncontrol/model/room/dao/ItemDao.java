package hu.respawncontrol.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.respawncontrol.model.room.entity.Item;

@Dao
public interface ItemDao {

    @Insert
    long insert(Item item);

    @Query("SELECT * " +
            "FROM Item JOIN Result ON Item.itemId = Result.itemId " +
            "GROUP BY Item.itemId")
    LiveData<List<Item>> getAllItemsHavingResults();

    @Query("SELECT * " +
            "FROM Item JOIN Result ON Item.itemId = Result.itemId " +
            "WHERE Result.date BETWEEN :fromTime AND :toTime " +
            "GROUP BY Item.itemId")
    LiveData<List<Item>> getItemsHavingResultsInTimePeriod(Long fromTime, Long toTime);

    @Query("SELECT * FROM Item INNER JOIN ItemCrossItemType " +
            "ON Item.itemId = ItemCrossItemType.itemId " +
            "WHERE ItemCrossItemType.itemTypeId = :itemTypeId")
    List<Item> getItemsByItemTypeId(int itemTypeId);
}

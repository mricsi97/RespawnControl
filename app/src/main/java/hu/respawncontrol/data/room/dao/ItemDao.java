package hu.respawncontrol.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.respawncontrol.data.room.entity.Item;

@Dao
public interface ItemDao {

    @Insert
    long insert(Item item);

    @Query("SELECT * FROM Item INNER JOIN ItemCrossItemType " +
            "ON Item.itemId = ItemCrossItemType.itemId " +
            "WHERE ItemCrossItemType.itemTypeId = :itemTypeId")
    List<Item> getItemsByItemTypeId(int itemTypeId);
}

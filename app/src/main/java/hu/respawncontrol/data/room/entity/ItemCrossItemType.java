package hu.respawncontrol.data.room.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"itemId", "itemTypeId"})
public class ItemCrossItemType {
    public int itemId;
    public int itemTypeId;

    public ItemCrossItemType(int itemId, int itemTypeId) {
        this.itemId = itemId;
        this.itemTypeId = itemTypeId;
    }
}

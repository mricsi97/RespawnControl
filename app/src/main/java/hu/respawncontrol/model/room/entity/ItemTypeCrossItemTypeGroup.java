package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"itemTypeId", "itemTypeGroupId"})
public class ItemTypeCrossItemTypeGroup {
    public int itemTypeId;
    public int itemTypeGroupId;

    public ItemTypeCrossItemTypeGroup(int itemTypeId, int itemTypeGroupId) {
        this.itemTypeId = itemTypeId;
        this.itemTypeGroupId = itemTypeGroupId;
    }
}

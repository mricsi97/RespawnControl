package hu.respawncontrol.model.room.helper;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import hu.respawncontrol.model.room.entity.ItemType;
import hu.respawncontrol.model.room.entity.ItemTypeCrossItemTypeGroup;
import hu.respawncontrol.model.room.entity.ItemTypeGroup;

public class ItemTypeGroupWithItemTypes {
    @Embedded public ItemTypeGroup itemTypeGroup;
    @Relation(
            parentColumn = "itemTypeGroupId",
            entityColumn = "itemTypeId",
            associateBy = @Junction(ItemTypeCrossItemTypeGroup.class)
    )
    public List<ItemType> itemTypes;
}

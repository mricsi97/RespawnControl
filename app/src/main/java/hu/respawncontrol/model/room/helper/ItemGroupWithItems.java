package hu.respawncontrol.model.room.helper;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import hu.respawncontrol.model.room.entity.Frequency;
import hu.respawncontrol.model.room.entity.Item;
import hu.respawncontrol.model.room.entity.ItemGroup;

public class ItemGroupWithItems {
    @Embedded public ItemGroup itemGroup;
    @Relation(
            parentColumn = "itemGroupId",
            entityColumn = "itemId",
            associateBy = @Junction(Frequency.class)
    )
    public List<Item> items;
}

package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ItemTypeGroup {

    @PrimaryKey(autoGenerate = true)
    private int itemTypeGroupId;
    private String name;

    public ItemTypeGroup(String name) {
        this.name = name;
    }

    public void setItemTypeGroupId(int itemTypeGroupId) {
        this.itemTypeGroupId = itemTypeGroupId;
    }

    public int getItemTypeGroupId() {
        return itemTypeGroupId;
    }

    public String getName() {
        return name;
    }
}

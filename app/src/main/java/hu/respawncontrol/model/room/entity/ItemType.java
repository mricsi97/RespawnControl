package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ItemType {

    @PrimaryKey(autoGenerate = true)
    private int itemTypeId;
    private String name;
    private String shortName;

    public ItemType(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}
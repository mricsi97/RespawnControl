package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"itemId", "itemGroupId"})
public class Frequency {
    private int itemId;
    private int itemGroupId;
    private Integer frequency;

    public Frequency(int itemId, int itemGroupId, Integer frequency) {
        this.itemId = itemId;
        this.itemGroupId = itemGroupId;
        this.frequency = frequency;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getItemGroupId() {
        return itemGroupId;
    }

    public Integer getFrequency() {
        return frequency;
    }
}
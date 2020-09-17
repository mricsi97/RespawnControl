package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ItemGroup {

    @PrimaryKey(autoGenerate = true)
    private int itemGroupId;
    private String itemGroupName;
    private boolean editable;

    public ItemGroup(String itemGroupName, boolean editable) {
        this.itemGroupName = itemGroupName;
        this.editable = editable;
    }

    public void setItemGroupId(int itemGroupId) {
        this.itemGroupId = itemGroupId;
    }

    public int getItemGroupId() {
        return itemGroupId;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public boolean isEditable() {
        return editable;
    }
}

package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

import hu.respawncontrol.model.room.Converters;

@Entity
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int itemId;
    private String name;
    private String imageResourceName;
    @TypeConverters(Converters.class)
    private List<Integer> soundResourceIds;
    private Integer respawnTimeInSeconds;

    public Item(String name, String imageResourceName, List<Integer> soundResourceIds, Integer respawnTimeInSeconds) {
        this.name = name;
        this.imageResourceName = imageResourceName;
        this.soundResourceIds = soundResourceIds;
        this.respawnTimeInSeconds = respawnTimeInSeconds;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getImageResourceName() {
        return imageResourceName;
    }

    public List<Integer> getSoundResourceIds() {
        if(soundResourceIds == null) {
            soundResourceIds = new ArrayList<>();
        }
        return soundResourceIds;
    }

    public Integer getRespawnTimeInSeconds() {
        return respawnTimeInSeconds;
    }

}

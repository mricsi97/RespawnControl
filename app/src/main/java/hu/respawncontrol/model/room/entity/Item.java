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
    private String itemName;
    private String shortName;
    private String imageResourceName;
    @TypeConverters(Converters.class)
    private List<String> soundResourceEntryNames;
    private Integer respawnTimeInSeconds;

    public Item(String itemName, String shortName, String imageResourceName, List<String> soundResourceEntryNames, Integer respawnTimeInSeconds) {
        this.itemName = itemName;
        this.shortName = shortName;
        this.imageResourceName = imageResourceName;
        this.soundResourceEntryNames = soundResourceEntryNames;
        this.respawnTimeInSeconds = respawnTimeInSeconds;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getImageResourceName() {
        return imageResourceName;
    }

    public List<String> getSoundResourceEntryNames() {
        if(soundResourceEntryNames == null) {
            soundResourceEntryNames = new ArrayList<>();
        }
        return soundResourceEntryNames;
    }

    public Integer getRespawnTimeInSeconds() {
        return respawnTimeInSeconds;
    }

}

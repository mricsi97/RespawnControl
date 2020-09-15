package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = { @ForeignKey(entity = GameMode.class, parentColumns = "id", childColumns = "gameModeId"),
                        @ForeignKey(entity = ItemTypeGroup.class, parentColumns = "itemTypeGroupId", childColumns = "itemTypeGroupId"),
                        @ForeignKey(entity = Difficulty.class, parentColumns = "id", childColumns = "difficultyId") })
public class Leaderboard {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int gameModeId;
    private int itemTypeGroupId;
    private int difficultyId;
    private boolean isCustom;

    public Leaderboard(String name, int gameModeId, int itemTypeGroupId, int difficultyId, boolean isCustom) {
        this.name = name;
        this.gameModeId = gameModeId;
        this.itemTypeGroupId = itemTypeGroupId;
        this.difficultyId = difficultyId;
        this.isCustom = isCustom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGameModeId() {
        return gameModeId;
    }

    public int getItemTypeGroupId() {
        return itemTypeGroupId;
    }

    public int getDifficultyId() {
        return difficultyId;
    }

    public boolean isCustom() {
        return isCustom;
    }
}
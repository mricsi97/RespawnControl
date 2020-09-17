package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = { @ForeignKey(entity = GameMode.class, parentColumns = "id", childColumns = "gameModeId"),
                        @ForeignKey(entity = ItemGroup.class, parentColumns = "itemGroupId", childColumns = "itemGroupId"),
                        @ForeignKey(entity = Difficulty.class, parentColumns = "id", childColumns = "difficultyId") })
public class Leaderboard {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int gameModeId;
    private int itemGroupId;
    private int difficultyId;
    private boolean isCustom;

    public Leaderboard(String name, int gameModeId, int itemGroupId, int difficultyId, boolean isCustom) {
        this.name = name;
        this.gameModeId = gameModeId;
        this.itemGroupId = itemGroupId;
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

    public int getItemGroupId() {
        return itemGroupId;
    }

    public int getDifficultyId() {
        return difficultyId;
    }

    public boolean isCustom() {
        return isCustom;
    }
}
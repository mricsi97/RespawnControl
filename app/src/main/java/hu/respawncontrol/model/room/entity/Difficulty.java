package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = GameMode.class, parentColumns = "id", childColumns = "gameModeId"))
public class Difficulty {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int gameModeId;

    public Difficulty(String name, int gameModeId) {
        this.name = name;
        this.gameModeId = gameModeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getGameModeId() {
        return gameModeId;
    }
}
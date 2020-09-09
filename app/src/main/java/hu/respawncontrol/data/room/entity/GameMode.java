package hu.respawncontrol.data.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GameMode {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public GameMode(String name) {
        this.name = name;
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
}

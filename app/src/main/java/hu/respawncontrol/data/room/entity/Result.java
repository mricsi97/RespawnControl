package hu.respawncontrol.data.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = { @ForeignKey(entity = Item.class, parentColumns = "itemId", childColumns = "itemId") })
public class Result {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Long solveTime;
    private int itemId;

    public Result(Long solveTime, int itemId) {
        this.solveTime = solveTime;
        this.itemId = itemId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Long getSolveTime() {
        return solveTime;
    }

    public int getItemId() {
        return itemId;
    }
}

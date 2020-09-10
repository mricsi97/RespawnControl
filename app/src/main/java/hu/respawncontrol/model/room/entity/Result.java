package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = { @ForeignKey(entity = Item.class, parentColumns = "itemId", childColumns = "itemId") })
public class Result {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Long solveTime;
    private int itemId;
    private Long date;

    public Result(Long solveTime, int itemId, Long date) {
        this.solveTime = solveTime;
        this.itemId = itemId;
        this.date = date;
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

    public Long getDate() {
        return date;
    }
}

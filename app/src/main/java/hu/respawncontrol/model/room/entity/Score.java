package hu.respawncontrol.model.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Leaderboard.class, parentColumns = "id", childColumns = "leaderboardId"))
public class Score {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Long time; // actual score in Time Trial, difficulty in Endless
    private int testAmount; // actual score in Endless, difficulty in Time Trial
    private int leaderboardId;
    private Long date;

    public Score(Long time, int testAmount, int leaderboardId, Long date) {
        this.time = time;
        this.testAmount = testAmount;
        this.leaderboardId = leaderboardId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public int getTestAmount() {
        return testAmount;
    }

    public int getLeaderboardId() {
        return leaderboardId;
    }

    public Long getDate() {
        return date;
    }
}

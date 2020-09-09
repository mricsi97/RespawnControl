package hu.respawncontrol.data.room.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Leaderboard.class, parentColumns = "id", childColumns = "leaderboardId"))
public class Score {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Long solveTime;
    private int leaderboardId;

    public Score(Long solveTime, int leaderboardId) {
        this.solveTime = solveTime;
        this.leaderboardId = leaderboardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getSolveTime() {
        return solveTime;
    }

    public int getLeaderboardId() {
        return leaderboardId;
    }
}

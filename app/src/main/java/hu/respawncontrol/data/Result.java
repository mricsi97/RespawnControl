package hu.respawncontrol.data;

public class Result {
    private Long solveTime;
    private Item item;

    public Result(Long solveTime, Item item) {
        this.solveTime = solveTime;
        this.item = item;
    }

    public Long getSolveTime() {
        return solveTime;
    }

    public Item getItem() {
        return item;
    }
}

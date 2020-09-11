package hu.respawncontrol.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import hu.respawncontrol.model.room.LocalDatabase;
import hu.respawncontrol.model.room.dao.DifficultyDao;
import hu.respawncontrol.model.room.dao.GameModeDao;
import hu.respawncontrol.model.room.dao.ItemDao;
import hu.respawncontrol.model.room.dao.ItemTypeDao;
import hu.respawncontrol.model.room.dao.ItemTypeGroupDao;
import hu.respawncontrol.model.room.dao.LeaderboardDao;
import hu.respawncontrol.model.room.dao.ResultDao;
import hu.respawncontrol.model.room.dao.ScoreDao;
import hu.respawncontrol.model.room.entity.Difficulty;
import hu.respawncontrol.model.room.entity.GameMode;
import hu.respawncontrol.model.room.entity.Item;
import hu.respawncontrol.model.room.entity.ItemType;
import hu.respawncontrol.model.room.entity.Leaderboard;
import hu.respawncontrol.model.room.entity.Result;
import hu.respawncontrol.model.room.entity.Score;
import hu.respawncontrol.model.room.helper.ItemTypeGroupWithItemTypes;

public class Repository {
    private ItemDao itemDao;
    private ItemTypeDao itemTypeDao;
    private ItemTypeGroupDao itemTypeGroupDao;
    private GameModeDao gameModeDao;
    private DifficultyDao difficultyDao;
    private LeaderboardDao leaderboardDao;
    private ResultDao resultDao;
    private ScoreDao scoreDao;

    public Repository(Application application) {
        LocalDatabase database = LocalDatabase.getInstance(application);
        itemDao = database.itemDao();
        itemTypeDao = database.itemTypeDao();
        itemTypeGroupDao = database.itemTypeGroupDao();
        gameModeDao = database.gameModeDao();
        difficultyDao = database.difficultyDao();
        leaderboardDao = database.leaderboardDao();
        resultDao = database.resultDao();
        scoreDao = database.scoreDao();
    }

    public LiveData<List<Item>> getAllItemsHavingResults() {
        AsyncTask<Void, Void, LiveData<List<Item>>> task = new GetAllItemsHavingResultsAsyncTask(itemDao).execute();
        LiveData<List<Item>> items = null;
        try {
            items = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return items;
    }

    public LiveData<List<Item>> getItemsHavingResultsPastWeek(Long currentDate) {
        AsyncTask<Long, Void, LiveData<List<Item>>> task = new GetItemsHavingResultsPastWeekAsyncTask(itemDao).execute(currentDate);
        LiveData<List<Item>> items = null;
        try {
            items = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return items;
    }

    public LiveData<List<Item>> getItemsHavingResultsPastSeason(Long currentDate) {
        AsyncTask<Long, Void, LiveData<List<Item>>> task = new GetItemsHavingResultsPastSeasonAsyncTask(itemDao).execute(currentDate);
        LiveData<List<Item>> items = null;
        try {
            items = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> getItemsByItemTypeId(int id) {
        AsyncTask<Integer, Void, List<Item>> task = new GetItemsByItemTypeIdAsyncTask(itemDao).execute(id);
        List<Item> items = null;
        try {
            items = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void insertItemType(ItemType itemType) {
        new InsertItemTypeAsyncTask(itemTypeDao).execute(itemType);
    }

    public List<ItemType> getAllItemTypes() {
        AsyncTask<Void, Void, List<ItemType>> task = new GetAllItemTypesAsyncTask(itemTypeDao).execute();
        List<ItemType> itemTypes = null;
        try {
            itemTypes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return itemTypes;
    }

    public LiveData<List<ItemTypeGroupWithItemTypes>> getAllItemTypeGroupsWithItemTypes() {
        AsyncTask<Void, Void, LiveData<List<ItemTypeGroupWithItemTypes>>> task = new GetAllItemTypeGroupsWithItemTypesAsyncTask(itemTypeGroupDao).execute();
        LiveData<List<ItemTypeGroupWithItemTypes>> itemTypeGroupsWithItemTypes = null;
        try {
            itemTypeGroupsWithItemTypes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return itemTypeGroupsWithItemTypes;
    }

    public GameMode getGameModeByName(String name) {
        AsyncTask<String, Void, GameMode> task = new GetGameModeByNameAsyncTask(gameModeDao).execute(name);
        GameMode gameMode = null;
        try {
            gameMode = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return gameMode;
    }

    public LiveData<List<Difficulty>> getDifficultiesByGameModeId(int id) {
        AsyncTask<Integer, Void, LiveData<List<Difficulty>>> task = new GetDifficultiesByGameModeIdAsyncTask(difficultyDao).execute(id);
        LiveData<List<Difficulty>> difficulties = null;
        try {
            difficulties = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return difficulties;
    }

    public Leaderboard getLeaderboard(int gameModeId, int itemTypeGroupId, int difficultyId) {
        AsyncTask<Integer, Void, Leaderboard> task = new GetLeaderboardAsyncTask(leaderboardDao).execute(gameModeId, itemTypeGroupId, difficultyId);
        Leaderboard leaderboard = null;
        try {
            leaderboard = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }

    public void insertResults(List<Result> results) {
        new InsertResultsAsyncTask(resultDao).execute(results);
    }

    public LiveData<List<Long>> getAllAverageTimes() {
        AsyncTask<Void, Void, LiveData<List<Long>>> task = new GetAllAverageTimesAsyncTask(resultDao).execute();
        LiveData<List<Long>> averageTimes = null;
        try {
            averageTimes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return averageTimes;
    }

    public LiveData<List<Long>> getAllBestTimes() {
        AsyncTask<Void, Void, LiveData<List<Long>>> task = new GetAllBestTimesAsyncTask(resultDao).execute();
        LiveData<List<Long>> bestTimes = null;
        try {
            bestTimes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return bestTimes;
    }

    public LiveData<List<Long>> getAllWorstTimes() {
        AsyncTask<Void, Void, LiveData<List<Long>>> task = new GetAllWorstTimesAsyncTask(resultDao).execute();
        LiveData<List<Long>> worstTimes = null;
        try {
            worstTimes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return worstTimes;
    }

    public LiveData<List<Long>> getAverageTimesPastWeek(Long currentDate) {
        AsyncTask<Long, Void, LiveData<List<Long>>> task = new GetAverageTimesPastWeekAsyncTask(resultDao).execute(currentDate);
        LiveData<List<Long>> averageTimes = null;
        try {
            averageTimes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return averageTimes;
    }

    public LiveData<List<Long>> getBestTimesPastWeek(Long currentDate) {
        AsyncTask<Long, Void, LiveData<List<Long>>> task = new GetBestTimesPastWeekAsyncTask(resultDao).execute(currentDate);
        LiveData<List<Long>> bestTimes = null;
        try {
            bestTimes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return bestTimes;
    }

    public LiveData<List<Long>> getWorstTimesPastWeek(Long currentDate) {
        AsyncTask<Long, Void, LiveData<List<Long>>> task = new GetWorstTimesPastWeekAsyncTask(resultDao).execute(currentDate);
        LiveData<List<Long>> worstTimes = null;
        try {
            worstTimes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return worstTimes;
    }

    public LiveData<List<Long>> getAverageTimesPastSeason(Long currentDate) {
        AsyncTask<Long, Void, LiveData<List<Long>>> task = new GetAverageTimesPastSeasonAsyncTask(resultDao).execute(currentDate);
        LiveData<List<Long>> averageTimes = null;
        try {
            averageTimes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return averageTimes;
    }

    public LiveData<List<Long>> getBestTimesPastSeason(Long currentDate) {
        AsyncTask<Long, Void, LiveData<List<Long>>> task = new GetBestTimesPastSeasonAsyncTask(resultDao).execute(currentDate);
        LiveData<List<Long>> bestTimes = null;
        try {
            bestTimes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return bestTimes;
    }

    public LiveData<List<Long>> getWorstTimesPastSeason(Long currentDate) {
        AsyncTask<Long, Void, LiveData<List<Long>>> task = new GetWorstTimesPastSeasonAsyncTask(resultDao).execute(currentDate);
        LiveData<List<Long>> worstTimes = null;
        try {
            worstTimes = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return worstTimes;
    }

    public void insertScore(Score score) {
        new InsertScoreAsyncTask(scoreDao).execute(score);
    }

    //////////////////////
    // Background tasks //
    //////////////////////

    protected static class GetAllItemsHavingResultsAsyncTask extends AsyncTask<Void, Void, LiveData<List<Item>>> {
        private ItemDao itemDao;

        public GetAllItemsHavingResultsAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected LiveData<List<Item>> doInBackground(Void... voids) {
            return itemDao.getAllItemsHavingResults();
        }
    }

    protected static class GetItemsHavingResultsPastWeekAsyncTask extends AsyncTask<Long, Void, LiveData<List<Item>>> {
        private ItemDao itemDao;

        public GetItemsHavingResultsPastWeekAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected LiveData<List<Item>> doInBackground(Long... longs) {
            return itemDao.getItemsHavingResultsPastWeek(longs[0]);
        }
    }

    protected static class GetItemsHavingResultsPastSeasonAsyncTask extends AsyncTask<Long, Void, LiveData<List<Item>>> {
        private ItemDao itemDao;

        public GetItemsHavingResultsPastSeasonAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected LiveData<List<Item>> doInBackground(Long... longs) {
            return itemDao.getItemsHavingResultsPastSeason(longs[0]);
        }
    }

    private static class GetItemsByItemTypeIdAsyncTask extends AsyncTask<Integer, Void, List<Item>> {
        private ItemDao itemDao;

        private GetItemsByItemTypeIdAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected List<Item> doInBackground(Integer... ids) {
            return itemDao.getItemsByItemTypeId(ids[0]);
        }
    }

    private static class InsertItemTypeAsyncTask extends AsyncTask<ItemType, Void, Void> {
        private ItemTypeDao itemTypeDao;

        private InsertItemTypeAsyncTask(ItemTypeDao itemTypeDao) {
            this.itemTypeDao = itemTypeDao;
        }

        @Override
        protected Void doInBackground(ItemType... itemTypes) {
            itemTypeDao.insert(itemTypes[0]);
            return null;
        }
    }

    private static class GetAllItemTypesAsyncTask extends AsyncTask<Void, Void, List<ItemType>> {
        private ItemTypeDao itemTypeDao;

        private GetAllItemTypesAsyncTask(ItemTypeDao itemTypeDao) {
            this.itemTypeDao = itemTypeDao;
        }

        @Override
        protected List<ItemType> doInBackground(Void... voids) {
            return itemTypeDao.getAllItemTypes();
        }
    }

    private static class GetAllItemTypeGroupsWithItemTypesAsyncTask extends AsyncTask<Void, Void, LiveData<List<ItemTypeGroupWithItemTypes>>> {
        private ItemTypeGroupDao itemTypeGroupDao;

        private GetAllItemTypeGroupsWithItemTypesAsyncTask(ItemTypeGroupDao itemTypeGroupDao) {
            this.itemTypeGroupDao = itemTypeGroupDao;
        }

        @Override
        protected LiveData<List<ItemTypeGroupWithItemTypes>> doInBackground(Void... voids) {
            return itemTypeGroupDao.getAllItemTypeGroupsWithItemTypes();
        }
    }

    protected static class GetGameModeByNameAsyncTask extends AsyncTask<String, Void, GameMode> {
        private GameModeDao gameModeDao;

        public GetGameModeByNameAsyncTask(GameModeDao gameModeDao) {
            this.gameModeDao = gameModeDao;
        }

        @Override
        protected GameMode doInBackground(String... strings) {
            return gameModeDao.getGameModeByName(strings[0]);
        }
    }

    protected static class GetDifficultiesByGameModeIdAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Difficulty>>> {
        private DifficultyDao difficultyDao;

        public GetDifficultiesByGameModeIdAsyncTask(DifficultyDao difficultyDao) {
            this.difficultyDao = difficultyDao;
        }

        @Override
        protected LiveData<List<Difficulty>> doInBackground(Integer... integers) {
            return difficultyDao.getDifficultiesByGameModeId(integers[0]);
        }
    }

    protected static class GetLeaderboardAsyncTask extends AsyncTask<Integer, Void, Leaderboard> {
        private LeaderboardDao leaderboardDao;

        public GetLeaderboardAsyncTask(LeaderboardDao leaderboardDao) {
            this.leaderboardDao = leaderboardDao;
        }

        @Override
        protected Leaderboard doInBackground(Integer... integers) {
            return leaderboardDao.getLeaderboard(integers[0], integers[1], integers[2]);
        }
    }

    protected static class InsertResultsAsyncTask extends AsyncTask<List<Result>, Void, Void> {
        private ResultDao resultDao;

        private InsertResultsAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected Void doInBackground(List<Result>... lists) {
            resultDao.insert(lists[0]);
            return null;
        }
    }

    protected static class GetAllAverageTimesAsyncTask extends AsyncTask<Void, Void, LiveData<List<Long>>> {
        private ResultDao resultDao;

        public GetAllAverageTimesAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Void... voids) {
            return resultDao.getAllAverageTimes();
        }
    }

    protected static class GetAllBestTimesAsyncTask extends AsyncTask<Void, Void, LiveData<List<Long>>> {
        private ResultDao resultDao;

        public GetAllBestTimesAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Void... voids) {
            return resultDao.getAllBestTimes();
        }
    }

    protected static class GetAllWorstTimesAsyncTask extends AsyncTask<Void, Void, LiveData<List<Long>>> {
        private ResultDao resultDao;

        public GetAllWorstTimesAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Void... voids) {
            return resultDao.getAllWorstTimes();
        }
    }

    protected static class GetAverageTimesPastWeekAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private ResultDao resultDao;

        public GetAverageTimesPastWeekAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Long... longs) {
            return resultDao.getAverageTimesPastWeek(longs[0]);
        }
    }

    protected static class GetBestTimesPastWeekAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private ResultDao resultDao;

        public GetBestTimesPastWeekAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Long... longs) {
            return resultDao.getBestTimesPastWeek(longs[0]);
        }
    }

    protected static class GetWorstTimesPastWeekAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private ResultDao resultDao;

        public GetWorstTimesPastWeekAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Long... longs) {
            return resultDao.getWorstTimesPastWeek(longs[0]);
        }
    }

    protected static class GetAverageTimesPastSeasonAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private ResultDao resultDao;

        public GetAverageTimesPastSeasonAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Long... longs) {
            return resultDao.getAverageTimesPastSeason(longs[0]);
        }
    }

    protected static class GetBestTimesPastSeasonAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private ResultDao resultDao;

        public GetBestTimesPastSeasonAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Long... longs) {
            return resultDao.getBestTimesPastSeason(longs[0]);
        }
    }

    protected static class GetWorstTimesPastSeasonAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private ResultDao resultDao;

        public GetWorstTimesPastSeasonAsyncTask(ResultDao resultDao) {
            this.resultDao = resultDao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Long... longs) {
            return resultDao.getWorstTimesPastSeason(longs[0]);
        }
    }

    protected static class InsertScoreAsyncTask extends AsyncTask<Score, Void, Void> {
        private ScoreDao scoreDao;

        private InsertScoreAsyncTask(ScoreDao scoreDao) {
            this.scoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Score... scores) {
            scoreDao.insert(scores[0]);
            return null;
        }
    }
}

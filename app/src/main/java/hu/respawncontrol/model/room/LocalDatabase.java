package hu.respawncontrol.model.room;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Arrays;

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.dao.DifficultyDao;
import hu.respawncontrol.model.room.dao.FrequencyDao;
import hu.respawncontrol.model.room.dao.GameModeDao;
import hu.respawncontrol.model.room.dao.ItemDao;
import hu.respawncontrol.model.room.dao.ItemGroupDao;
import hu.respawncontrol.model.room.dao.LeaderboardDao;
import hu.respawncontrol.model.room.dao.ResultDao;
import hu.respawncontrol.model.room.dao.ScoreDao;
import hu.respawncontrol.model.room.entity.Difficulty;
import hu.respawncontrol.model.room.entity.Frequency;
import hu.respawncontrol.model.room.entity.GameMode;
import hu.respawncontrol.model.room.entity.Item;
import hu.respawncontrol.model.room.entity.ItemGroup;
import hu.respawncontrol.model.room.entity.Leaderboard;
import hu.respawncontrol.model.room.entity.Result;
import hu.respawncontrol.model.room.entity.Score;

@androidx.room.Database(entities = {GameMode.class, Difficulty.class, Item.class, ItemGroup.class, Frequency.class, Leaderboard.class, Result.class, Score.class}, version = 6)
@TypeConverters({Converters.class})
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase instance;
    private static Context context;

    public abstract GameModeDao gameModeDao();
    public abstract DifficultyDao difficultyDao();
    public abstract ItemDao itemDao();
    public abstract ItemGroupDao itemGroupDao();
    public abstract FrequencyDao frequencyDao();
    public abstract LeaderboardDao leaderboardDao();
    public abstract ResultDao resultDao();
    public abstract ScoreDao scoreDao();

    public static synchronized LocalDatabase getInstance(Context context) {
        LocalDatabase.context = context;
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    LocalDatabase.class, "local_database")
                    .fallbackToDestructiveMigration()
                    .createFromAsset("database/local_database")
                    .build();
//            instance.populateInitialData();
        }
        return instance;
    }

    private void populateInitialData() {
        new PopulateDbAsyncTask(instance, context).execute();
    }

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private GameModeDao gameModeDao;
        private DifficultyDao difficultyDao;
        private ItemDao itemDao;
        private ItemGroupDao itemGroupDao;
        private FrequencyDao frequencyDao;
        private LeaderboardDao leaderboardDao;
        private Context context;

        private PopulateDbAsyncTask(LocalDatabase db, Context context) {
            gameModeDao = db.gameModeDao();
            difficultyDao = db.difficultyDao();
            itemDao = db.itemDao();
            itemGroupDao = db.itemGroupDao();
            frequencyDao = db.frequencyDao();
            leaderboardDao = db.leaderboardDao();
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Game modes
            int timeTrialId = (int) gameModeDao.insert(new GameMode("Time Trial"));
            int endlessId = (int) gameModeDao.insert(new GameMode("Endless"));

            // Difficulties
            int timeTrial50Id = (int) difficultyDao.insert(new Difficulty("50", timeTrialId));
            int timeTrial100Id = (int) difficultyDao.insert(new Difficulty("100", timeTrialId));
            int timeTrialCustomId = (int) difficultyDao.insert(new Difficulty("Custom", timeTrialId));
            int endless3_5Id = (int) difficultyDao.insert(new Difficulty("3.5s (Easy)", endlessId));
            int endless2_5Id = (int) difficultyDao.insert(new Difficulty("2.5s (Normal)", endlessId));
            int endless1_5Id = (int) difficultyDao.insert(new Difficulty("1.5s (Pro)", endlessId));
            int endlessCustom = (int) difficultyDao.insert(new Difficulty("Custom", endlessId));

            Resources res = context.getResources();

            // Items
            int redItemId = (int) itemDao.insert(new Item("Red Armor", "A100", res.getResourceEntryName(R.drawable.armor_100), Arrays.asList(res.getResourceEntryName(R.raw.armort4)), 25));
            int yellowItemId = (int) itemDao.insert(new Item("Yellow Armor", "A75", res.getResourceEntryName(R.drawable.armor_75), Arrays.asList(res.getResourceEntryName(R.raw.armort3)), 25));
            int blueItemId = (int) itemDao.insert(new Item("Blue Armor", "A50", res.getResourceEntryName(R.drawable.armor_50), Arrays.asList(res.getResourceEntryName(R.raw.armort2)), 25));
            int shardItemId = (int) itemDao.insert(new Item("Armor Shard", "A5", res.getResourceEntryName(R.drawable.armor_5), Arrays.asList(res.getResourceEntryName(R.raw.armort1)), 25));

            int megaItemId = (int) itemDao.insert(new Item("Mega Health", "H100", res.getResourceEntryName(R.drawable.health_100), Arrays.asList(res.getResourceEntryName(R.raw.hpt3)), 35));
            int health50ItemId = (int) itemDao.insert(new Item("50 Health", "H50", res.getResourceEntryName(R.drawable.health_50), Arrays.asList(res.getResourceEntryName(R.raw.hpt2)), 20));
            int health25ItemId = (int) itemDao.insert(new Item("25 Health", "H25", res.getResourceEntryName(R.drawable.health_25), Arrays.asList(res.getResourceEntryName(R.raw.hpt1)), 20));
            int health5ItemId = (int) itemDao.insert(new Item("Bubble Health", "H5", res.getResourceEntryName(R.drawable.health_5), Arrays.asList(res.getResourceEntryName(R.raw.hpt0)), 20));

            int machineGunItemId = (int) itemDao.insert(new Item("Machine Gun", "MG", res.getResourceEntryName(R.drawable.machine_gun), Arrays.asList(res.getResourceEntryName(R.raw.weaponmac)), 15));
            int blasterItemId = (int) itemDao.insert(new Item("Blaster", "BLA", res.getResourceEntryName(R.drawable.blaster), Arrays.asList(res.getResourceEntryName(R.raw.weaponbl)), 15));
            int shotgunItemId = (int) itemDao.insert(new Item("Super Shotgun", "SS", res.getResourceEntryName(R.drawable.shotgun), Arrays.asList(res.getResourceEntryName(R.raw.weaponss)), 15));
            int rocketLauncherItemId = (int) itemDao.insert(new Item("Rocket Launcher", "RL", res.getResourceEntryName(R.drawable.rocket_launcher), Arrays.asList(res.getResourceEntryName(R.raw.weaponrl)), 15));
            int shaftItemId = (int) itemDao.insert(new Item("Shaft", "SHA", res.getResourceEntryName(R.drawable.shaft), Arrays.asList(res.getResourceEntryName(R.raw.weaponshaft)), 15));
            int crossbowItemId = (int) itemDao.insert(new Item("Crossbow", "XB", res.getResourceEntryName(R.drawable.crossbow), Arrays.asList(res.getResourceEntryName(R.raw.weaponcb)), 15));
            int pincerItemId = (int) itemDao.insert(new Item("Pincer", "PNCR", res.getResourceEntryName(R.drawable.pincer), Arrays.asList(res.getResourceEntryName(R.raw.weaponpncr)), 15));
            int grenadeLauncherItemId = (int) itemDao.insert(new Item("Grenade Launcher", "GL", res.getResourceEntryName(R.drawable.grenade_launcher), Arrays.asList(res.getResourceEntryName(R.raw.weapongl)), 15));

            int siphonatorItemId = (int) itemDao.insert(new Item("Siphonator", "SIP", res.getResourceEntryName(R.drawable.siphonator), Arrays.asList(res.getResourceEntryName(R.raw.powerup_siphonator), res.getResourceEntryName(R.raw.announcer_common_powerup_siphonator_pickup)), 120));
            int vanguardItemId = (int) itemDao.insert(new Item("Vanguard", "VAN", res.getResourceEntryName(R.drawable.vanguard), Arrays.asList(res.getResourceEntryName(R.raw.powerup_vanguard), res.getResourceEntryName(R.raw.announcer_common_powerup_vg_pickup)), 120));
            int vindicatorItemId = (int) itemDao.insert(new Item("Vindicator", "VIN", res.getResourceEntryName(R.drawable.vindicator), Arrays.asList(res.getResourceEntryName(R.raw.powerup), res.getResourceEntryName(R.raw.announcer_common_powerup_td_pickup)), 120));
            int diaboticalItemId = (int) itemDao.insert(new Item("Diabotical", "DIA", res.getResourceEntryName(R.drawable.diabotical), Arrays.asList(res.getResourceEntryName(R.raw.powerup), res.getResourceEntryName(R.raw.announcer_common_powerup_db_pickup)), 240));

            // Item groups
            int duelId = (int) itemGroupDao.insert(new ItemGroup("Duel", false));
            int extinctionId = (int) itemGroupDao.insert(new ItemGroup("Extinction", false));
            int macGuffinId = (int) itemGroupDao.insert(new ItemGroup("MacGuffin", false));
            int varietyId = (int) itemGroupDao.insert(new ItemGroup("Variety", false));

            // Frequencies (item <-> item group)
            frequencyDao.insert(new Frequency(redItemId, duelId, 29)); // 26.85%
            frequencyDao.insert(new Frequency(yellowItemId, duelId, 29)); // 26.85%
            frequencyDao.insert(new Frequency(blueItemId, duelId, 29)); // 26.85%
            frequencyDao.insert(new Frequency(megaItemId, duelId, 21)); // 19.5%

            frequencyDao.insert(new Frequency(redItemId, extinctionId, 29)); // 34.1%
            frequencyDao.insert(new Frequency(blueItemId, extinctionId, 29)); // 34.1%
            frequencyDao.insert(new Frequency(megaItemId, extinctionId, 21)); // 24.7%
            frequencyDao.insert(new Frequency(siphonatorItemId, extinctionId, 2)); // 2.35%
            frequencyDao.insert(new Frequency(vanguardItemId, extinctionId, 2)); // 2.35%
            frequencyDao.insert(new Frequency(vindicatorItemId, extinctionId, 2)); // 2.35%

            frequencyDao.insert(new Frequency(redItemId, macGuffinId, 29)); // 34.1%
            frequencyDao.insert(new Frequency(blueItemId, macGuffinId, 29)); // 34.1%
            frequencyDao.insert(new Frequency(megaItemId, macGuffinId, 21)); // 24.7%
            frequencyDao.insert(new Frequency(vindicatorItemId, macGuffinId, 6)); // 7%

            frequencyDao.insert(new Frequency(redItemId, varietyId, 10)); // 11.9%
            frequencyDao.insert(new Frequency(yellowItemId, varietyId, 10)); // 11.9%
            frequencyDao.insert(new Frequency(blueItemId, varietyId, 10)); // 11.9%
            frequencyDao.insert(new Frequency(shardItemId, varietyId, 10)); // 11.9%
            frequencyDao.insert(new Frequency(megaItemId, varietyId, 10)); // 11.9%
            frequencyDao.insert(new Frequency(health50ItemId, varietyId, 10)); // 11.9%
            frequencyDao.insert(new Frequency(health25ItemId, varietyId, 10)); // 11.9%
            frequencyDao.insert(new Frequency(health5ItemId, varietyId, 10)); // 11.9%
            frequencyDao.insert(new Frequency(siphonatorItemId, varietyId, 1)); // 1.2%
            frequencyDao.insert(new Frequency(vanguardItemId, varietyId, 1)); // 1.2%
            frequencyDao.insert(new Frequency(vindicatorItemId, varietyId, 1)); // 1.2%
            frequencyDao.insert(new Frequency(diaboticalItemId, varietyId, 1)); // 1.2%

            // Google Play leaderboards
            leaderboardDao.insert(new Leaderboard("Time Trial | Duel | 50", timeTrialId, duelId, timeTrial50Id, false));
            leaderboardDao.insert(new Leaderboard("Time Trial | Duel | 100", timeTrialId, duelId, timeTrial100Id, false));
            leaderboardDao.insert(new Leaderboard("Time Trial | Extinction | 50", timeTrialId, extinctionId, timeTrial50Id, false));
            leaderboardDao.insert(new Leaderboard("Time Trial | Extinction | 100", timeTrialId, extinctionId, timeTrial100Id, false));
            leaderboardDao.insert(new Leaderboard("Time Trial | MacGuffin | 50", timeTrialId, macGuffinId, timeTrial50Id, false));
            leaderboardDao.insert(new Leaderboard("Time Trial | MacGuffin | 100", timeTrialId, macGuffinId, timeTrial100Id, false));
            leaderboardDao.insert(new Leaderboard("Time Trial | Variety | 50", timeTrialId, varietyId, timeTrial50Id, false));
            leaderboardDao.insert(new Leaderboard("Time Trial | Variety | 100", timeTrialId, varietyId, timeTrial100Id, false));

            leaderboardDao.insert(new Leaderboard("Endless | Duel | 3.5", endlessId, duelId, endless3_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | Duel | 2.5", endlessId, duelId, endless2_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | Duel | 1.5", endlessId, duelId, endless1_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | Extinction | 3.5", endlessId, extinctionId, endless3_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | Extinction | 2.5", endlessId, extinctionId, endless2_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | Extinction | 1.5", endlessId, extinctionId, endless1_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | MacGuffin | 3.5", endlessId, macGuffinId, endless3_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | MacGuffin | 2.5", endlessId, macGuffinId, endless2_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | MacGuffin | 1.5", endlessId, macGuffinId, endless1_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | Variety | 3.5", endlessId, varietyId, endless3_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | Variety | 2.5", endlessId, varietyId, endless2_5Id, false));
            leaderboardDao.insert(new Leaderboard("Endless | Variety | 1.5", endlessId, varietyId, endless1_5Id, false));

            // Custom leaderboards
            leaderboardDao.insert(new Leaderboard("Time Trial | Duel | Custom", timeTrialId, duelId, timeTrialCustomId, true));
            leaderboardDao.insert(new Leaderboard("Time Trial | Extinction | Custom", timeTrialId, extinctionId, timeTrialCustomId, true));
            leaderboardDao.insert(new Leaderboard("Time Trial | MacGuffin | Custom", timeTrialId, macGuffinId, timeTrialCustomId, true));
            leaderboardDao.insert(new Leaderboard("Time Trial | Variety | Custom", timeTrialId, varietyId, timeTrialCustomId, true));

            leaderboardDao.insert(new Leaderboard("Endless | Duel | Custom", endlessId, duelId,  endlessCustom, true));
            leaderboardDao.insert(new Leaderboard("Endless | Extinction | Custom", endlessId, extinctionId, endlessCustom, true));
            leaderboardDao.insert(new Leaderboard("Endless | MacGuffin | Custom", endlessId, macGuffinId, endlessCustom, true));
            leaderboardDao.insert(new Leaderboard("Endless | Variety | Custom", endlessId, varietyId, endlessCustom, true));

            return null;
        }
    }
}

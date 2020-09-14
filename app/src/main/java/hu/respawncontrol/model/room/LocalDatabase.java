package hu.respawncontrol.model.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.Arrays;

import hu.respawncontrol.R;
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
import hu.respawncontrol.model.room.entity.ItemCrossItemType;
import hu.respawncontrol.model.room.entity.ItemType;
import hu.respawncontrol.model.room.entity.ItemTypeCrossItemTypeGroup;
import hu.respawncontrol.model.room.entity.ItemTypeGroup;
import hu.respawncontrol.model.room.entity.Leaderboard;
import hu.respawncontrol.model.room.entity.Result;
import hu.respawncontrol.model.room.entity.Score;

@androidx.room.Database(entities = {GameMode.class, Difficulty.class, Item.class, ItemType.class, ItemCrossItemType.class,
        ItemTypeGroup.class, ItemTypeCrossItemTypeGroup.class, Leaderboard.class, Result.class, Score.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase instance;
    private static Context context;

    public abstract GameModeDao gameModeDao();
    public abstract DifficultyDao difficultyDao();
    public abstract ItemDao itemDao();
    public abstract ItemTypeDao itemTypeDao();
    public abstract ItemTypeGroupDao itemTypeGroupDao();
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
        private ItemTypeDao itemTypeDao;
        private ItemTypeGroupDao itemTypeGroupDao;
        private LeaderboardDao leaderboardDao;
        private Context context;

        private PopulateDbAsyncTask(LocalDatabase db, Context context) {
            gameModeDao = db.gameModeDao();
            difficultyDao = db.difficultyDao();
            itemDao = db.itemDao();
            itemTypeDao = db.itemTypeDao();
            itemTypeGroupDao = db.itemTypeGroupDao();
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

            // Items
            int redItemId = (int) itemDao.insert(new Item("Red Armor", context.getResources().getResourceEntryName(R.drawable.armor_100), Arrays.asList(R.raw.armort4), 25));
            int yellowItemId = (int) itemDao.insert(new Item("Yellow Armor", context.getResources().getResourceEntryName(R.drawable.armor_75), Arrays.asList(R.raw.armort3), 25));
            int blueItemId = (int) itemDao.insert(new Item("Blue Armor", context.getResources().getResourceEntryName(R.drawable.armor_50), Arrays.asList(R.raw.armort2), 25));
            int shardItemId = (int) itemDao.insert(new Item("Armor Shard", context.getResources().getResourceEntryName(R.drawable.armor_5), Arrays.asList(R.raw.armort1), 25));

            int megaItemId = (int) itemDao.insert(new Item("Mega Health", context.getResources().getResourceEntryName(R.drawable.health_100), Arrays.asList(R.raw.hpt3), 35));
            int health5ItemId = (int) itemDao.insert(new Item("Bubble Health", context.getResources().getResourceEntryName(R.drawable.health_5), Arrays.asList(R.raw.hpt0), 20));
            int health25ItemId = (int) itemDao.insert(new Item("25 Health", context.getResources().getResourceEntryName(R.drawable.health_25), Arrays.asList(R.raw.hpt1), 20));
            int health50ItemId = (int) itemDao.insert(new Item("50 Health", context.getResources().getResourceEntryName(R.drawable.health_50), Arrays.asList(R.raw.hpt2), 20));

            int machineGunItemId = (int) itemDao.insert(new Item("Machine Gun", context.getResources().getResourceEntryName(R.drawable.machine_gun), Arrays.asList(R.raw.weaponmac), 15));
            int blasterItemId = (int) itemDao.insert(new Item("Blaster", context.getResources().getResourceEntryName(R.drawable.blaster), Arrays.asList(R.raw.weaponbl), 15));
            int shotgunItemId = (int) itemDao.insert(new Item("Super Shotgun", context.getResources().getResourceEntryName(R.drawable.shotgun), Arrays.asList(R.raw.weaponss), 15));
            int rocketLauncherItemId = (int) itemDao.insert(new Item("Rocket Launcher", context.getResources().getResourceEntryName(R.drawable.rocket_launcher), Arrays.asList(R.raw.weaponrl), 15));
            int shaftItemId = (int) itemDao.insert(new Item("Shaft", context.getResources().getResourceEntryName(R.drawable.shaft), Arrays.asList(R.raw.weaponshaft), 15));
            int crossbowItemId = (int) itemDao.insert(new Item("Crossbow", context.getResources().getResourceEntryName(R.drawable.crossbow), Arrays.asList(R.raw.weaponcb), 15));
            int pincerItemId = (int) itemDao.insert(new Item("Pincer", context.getResources().getResourceEntryName(R.drawable.pincer), Arrays.asList(R.raw.weaponpncr), 15));
            int grenadeLauncherItemId = (int) itemDao.insert(new Item("Grenade Launcher", context.getResources().getResourceEntryName(R.drawable.grenade_launcher), Arrays.asList(R.raw.weapongl), 15));

            int siphonatorItemId = (int) itemDao.insert(new Item("Siphonator", context.getResources().getResourceEntryName(R.drawable.siphonator), Arrays.asList(R.raw.powerup_siphonator, R.raw.announcer_common_powerup_siphonator_pickup), 120));
            int vanguardItemId = (int) itemDao.insert(new Item("Vanguard", context.getResources().getResourceEntryName(R.drawable.vanguard), Arrays.asList(R.raw.powerup_vanguard, R.raw.announcer_common_powerup_vg_pickup), 120));
            int vindicatorItemId = (int) itemDao.insert(new Item("Vindicator", context.getResources().getResourceEntryName(R.drawable.vindicator), Arrays.asList(R.raw.powerup, R.raw.announcer_common_powerup_td_pickup), 120));
            int diaboticalItemId = (int) itemDao.insert(new Item("Diabotical", context.getResources().getResourceEntryName(R.drawable.diabotical), Arrays.asList(R.raw.powerup, R.raw.announcer_common_powerup_db_pickup), 240));

            // Item types
            int redArmorTypeId = (int) itemTypeDao.insert(new ItemType("Red Armor", "A100"));
            int yellowArmorTypeId = (int) itemTypeDao.insert(new ItemType("Yellow Armor", "A75"));
            int blueArmorTypeId = (int) itemTypeDao.insert(new ItemType("Blue Armor", "A50"));
            int shardArmorTypeId = (int) itemTypeDao.insert(new ItemType("Shard Armor", "A5"));
            int megaHealthTypeId = (int) itemTypeDao.insert(new ItemType("Mega Health", "H100"));
            int health50TypeId = (int) itemTypeDao.insert(new ItemType("50 Health", "H50"));
            int health25TypeId = (int) itemTypeDao.insert(new ItemType("25 Health", "H25"));
            int health5TypeId = (int) itemTypeDao.insert(new ItemType("5 Health", "H5"));
            int otherHealthTypeId = (int) itemTypeDao.insert(new ItemType("Other Health", "H5/25/50"));
            int weaponTypeId = (int) itemTypeDao.insert(new ItemType("Weapon", "Weapons"));
            int powerupTypeId = (int) itemTypeDao.insert(new ItemType("Powerup", "Powerups"));
            int siphonatorTypeId = (int) itemTypeDao.insert(new ItemType("Siphonator", "Sip"));
            int vanguardTypeId = (int) itemTypeDao.insert(new ItemType("Vanguard", "Van"));
            int vindicatorTypeId = (int) itemTypeDao.insert(new ItemType("Vindicator", "Vin"));
            int diaboticalTypeId = (int) itemTypeDao.insert(new ItemType("Diabotical", "Dia"));

            itemTypeDao.insert(new ItemCrossItemType(redItemId, redArmorTypeId));
            itemTypeDao.insert(new ItemCrossItemType(yellowItemId, yellowArmorTypeId));
            itemTypeDao.insert(new ItemCrossItemType(blueItemId, blueArmorTypeId));
            itemTypeDao.insert(new ItemCrossItemType(shardItemId, shardArmorTypeId));
            itemTypeDao.insert(new ItemCrossItemType(megaItemId, megaHealthTypeId));
            itemTypeDao.insert(new ItemCrossItemType(health5ItemId, health5TypeId));
            itemTypeDao.insert(new ItemCrossItemType(health5ItemId, otherHealthTypeId));
            itemTypeDao.insert(new ItemCrossItemType(health25ItemId, health25TypeId));
            itemTypeDao.insert(new ItemCrossItemType(health25ItemId, otherHealthTypeId));
            itemTypeDao.insert(new ItemCrossItemType(health50ItemId, health50TypeId));
            itemTypeDao.insert(new ItemCrossItemType(health50ItemId, otherHealthTypeId));
            itemTypeDao.insert(new ItemCrossItemType(machineGunItemId, weaponTypeId));
            itemTypeDao.insert(new ItemCrossItemType(blasterItemId, weaponTypeId));
            itemTypeDao.insert(new ItemCrossItemType(shotgunItemId, weaponTypeId));
            itemTypeDao.insert(new ItemCrossItemType(rocketLauncherItemId, weaponTypeId));
            itemTypeDao.insert(new ItemCrossItemType(shaftItemId, weaponTypeId));
            itemTypeDao.insert(new ItemCrossItemType(crossbowItemId, weaponTypeId));
            itemTypeDao.insert(new ItemCrossItemType(pincerItemId, weaponTypeId));
            itemTypeDao.insert(new ItemCrossItemType(grenadeLauncherItemId, weaponTypeId));
            itemTypeDao.insert(new ItemCrossItemType(siphonatorItemId, siphonatorTypeId));
            itemTypeDao.insert(new ItemCrossItemType(siphonatorItemId, powerupTypeId));
            itemTypeDao.insert(new ItemCrossItemType(vanguardItemId, vanguardTypeId));
            itemTypeDao.insert(new ItemCrossItemType(vanguardItemId, powerupTypeId));
            itemTypeDao.insert(new ItemCrossItemType(vindicatorItemId, vindicatorTypeId));
            itemTypeDao.insert(new ItemCrossItemType(vindicatorItemId, powerupTypeId));
            itemTypeDao.insert(new ItemCrossItemType(diaboticalItemId, diaboticalTypeId));
            itemTypeDao.insert(new ItemCrossItemType(diaboticalItemId, powerupTypeId));

            // Item type groups
            int duelItemTypeGroupId = (int) itemTypeGroupDao.insert(new ItemTypeGroup("Duel"));
            int extinctionItemTypeGroupId = (int) itemTypeGroupDao.insert(new ItemTypeGroup("Extinction"));
            int macGuffinItemTypeGroupId = (int) itemTypeGroupDao.insert(new ItemTypeGroup("MacGuffin"));

            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(redArmorTypeId, duelItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(yellowArmorTypeId, duelItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(blueArmorTypeId, duelItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(megaHealthTypeId, duelItemTypeGroupId));

            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(redArmorTypeId, extinctionItemTypeGroupId));
//            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(yellowArmorTypeId, extinctionItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(blueArmorTypeId, extinctionItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(megaHealthTypeId, extinctionItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(siphonatorTypeId, extinctionItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(vanguardTypeId, extinctionItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(vindicatorTypeId, extinctionItemTypeGroupId));

            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(redArmorTypeId, macGuffinItemTypeGroupId));
//            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(yellowArmorTypeId, macGuffinItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(blueArmorTypeId, macGuffinItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(megaHealthTypeId, macGuffinItemTypeGroupId));
            itemTypeGroupDao.insert(new ItemTypeCrossItemTypeGroup(vindicatorTypeId, macGuffinItemTypeGroupId));

            // Leaderboards
            leaderboardDao.insert(new Leaderboard("Time Trial | Duel | 50", timeTrialId, duelItemTypeGroupId, timeTrial50Id));
            leaderboardDao.insert(new Leaderboard("Time Trial | Duel | 100", timeTrialId, duelItemTypeGroupId, timeTrial100Id));
            leaderboardDao.insert(new Leaderboard("Time Trial | Duel | Custom", timeTrialId, duelItemTypeGroupId, timeTrialCustomId));
            leaderboardDao.insert(new Leaderboard("Time Trial | Extinction | 50", timeTrialId, extinctionItemTypeGroupId, timeTrial50Id));
            leaderboardDao.insert(new Leaderboard("Time Trial | Extinction | 100", timeTrialId, extinctionItemTypeGroupId, timeTrial100Id));
            leaderboardDao.insert(new Leaderboard("Time Trial | Extinction | Custom", timeTrialId, extinctionItemTypeGroupId, timeTrialCustomId));
            leaderboardDao.insert(new Leaderboard("Time Trial | MacGuffin | 50", timeTrialId, macGuffinItemTypeGroupId, timeTrial50Id));
            leaderboardDao.insert(new Leaderboard("Time Trial | MacGuffin | 100", timeTrialId, macGuffinItemTypeGroupId, timeTrial100Id));
            leaderboardDao.insert(new Leaderboard("Time Trial | MacGuffin | Custom", timeTrialId, macGuffinItemTypeGroupId, timeTrialCustomId));

            return null;
        }
    }
}

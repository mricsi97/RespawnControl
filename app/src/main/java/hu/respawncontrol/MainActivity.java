package hu.respawncontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.respawncontrol.data.Item;
import hu.respawncontrol.data.ItemType;
import hu.respawncontrol.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements TimeTrialOptionsDialog.DialogFinishedListener {

    private static final String TAG = "MainActivity";
    public static final String ITEM_TYPES = "ITEM_TYPES";
    public static final String SELECTED_ITEM_TYPES = "SELECTED_ITEM_TYPES";
    public static final String TEST_AMOUNT = "TEST_AMOUNT";

    private ArrayList<ItemType> itemTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createItems();

        final Button btnTimeTrial = (Button) findViewById(R.id.btnTimeTrial);
        final ImageButton btnSettings = (ImageButton) findViewById(R.id.btnSettings);

        btnTimeTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ITEM_TYPES, itemTypes);
                TimeTrialOptionsDialog dialog = new TimeTrialOptionsDialog();
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "TimeTrialOptionsDialog");
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music", false);
        if(musicEnabled) {
            MusicManager musicManager = MusicManager.getInstance(this);
            musicManager.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music", false);
        if(musicEnabled) {
            MusicManager musicManager = MusicManager.getInstance(this);
            musicManager.onStop();
        }
    }

    @Override
    public void sendResult(ArrayList<ItemType> itemTypes, Integer testAmount) {
        startTimeTrial(itemTypes, testAmount);
    }

    private void createItems() {
        List<Item> redArmor = Arrays.asList(
                new Item("Red Armor", R.drawable.armor_100, Arrays.asList(R.raw.armort4), 25));

        List<Item> otherArmors = Arrays.asList(
                new Item("Armor Shard", R.drawable.armor_5, Arrays.asList(R.raw.armort1), 25),
                new Item("Blue Armor", R.drawable.armor_50, Arrays.asList(R.raw.armort2), 25),
                new Item("Yellow Armor", R.drawable.armor_75, Arrays.asList(R.raw.armort3), 25));

        List<Item> megaHealth = Arrays.asList(
                new Item("Mega Health", R.drawable.health_100, Arrays.asList(R.raw.hpt3), 35));

        List<Item> otherHealths = Arrays.asList(
                new Item("Bubble Health", R.drawable.health_5, Arrays.asList(R.raw.hpt0), 20),
                new Item("25 Health", R.drawable.health_25, Arrays.asList(R.raw.hpt1), 20),
                new Item("50 Health", R.drawable.health_50, Arrays.asList(R.raw.hpt2), 20));

        List<Item> weapons = Arrays.asList(
                new Item("Machine Gun", R.drawable.machine_gun, Arrays.asList(R.raw.weaponmac), 5),
                new Item("Blaster", R.drawable.blaster, Arrays.asList(R.raw.weaponbl), 5),
                new Item("Super Shotgun", R.drawable.shotgun, Arrays.asList(R.raw.weaponss), 5),
                new Item("Rocket Launcher", R.drawable.rocket_launcher, Arrays.asList(R.raw.weaponrl), 5),
                new Item("Shaft", R.drawable.shaft, Arrays.asList(R.raw.weaponshaft), 5),
                new Item("Crossbow", R.drawable.crossbow, Arrays.asList(R.raw.weaponcb), 5),
                new Item("Pincer", R.drawable.pincer, Arrays.asList(R.raw.weaponpncr), 5),
                new Item("Grenade Launcher", R.drawable.grenade_launcher, Arrays.asList(R.raw.weapongl), 5));

        List<Item> powerups = Arrays.asList(
                new Item("Siphonator", R.drawable.siphonator, Arrays.asList(R.raw.powerup_siphonator, R.raw.announcer_common_powerup_siphonator_pickup), 120),
                new Item("Vanguard", R.drawable.vanguard, Arrays.asList(R.raw.powerup_vanguard, R.raw.announcer_common_powerup_vg_pickup), 120),
                new Item("Vindicator", R.drawable.vindicator, Arrays.asList(R.raw.powerup, R.raw.announcer_common_powerup_td_pickup), 120),
                new Item("Diabotical", R.drawable.diabotical, Arrays.asList(R.raw.powerup, R.raw.announcer_common_powerup_db_pickup), 240));

        itemTypes = new ArrayList<>(Arrays.asList(
                new ItemType("Red Armor", redArmor),
                new ItemType("Other Armors", otherArmors),
                new ItemType("Mega Health", megaHealth),
                new ItemType("Other Health", otherHealths),
                new ItemType("Weapon", weapons),
                new ItemType("Powerup", powerups)
        ));
    }

    private void startTimeTrial(ArrayList<ItemType> itemTypes, Integer testAmount) {
        Intent intent = new Intent(MainActivity.this, TimeTrialActivity.class);
        intent.putParcelableArrayListExtra(SELECTED_ITEM_TYPES, itemTypes);
        intent.putExtra(TEST_AMOUNT, testAmount);
        startActivity(intent);
    }
}

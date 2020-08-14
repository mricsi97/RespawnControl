package hu.respawncontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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
    public static final String CALCULATION_NUMBER = "CALCULATION_NUMBER";

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
    public void sendResult(ArrayList<ItemType> itemTypes, String calcNum) {
        startTimeTrial(itemTypes, Integer.parseInt(calcNum));
    }

    private void createItems() {
        List<Item> redArmor = Arrays.asList(
                new Item("Red Armor", R.drawable.armor_100, 25));

        List<Item> otherArmors = Arrays.asList(
                new Item("Armor Shard", R.drawable.armor_5, 25),
                new Item("Blue Armor", R.drawable.armor_50, 25),
                new Item("Yellow Armor", R.drawable.armor_75, 25));

        List<Item> megaHealth = Arrays.asList(
                new Item("Mega Health", R.drawable.health_100, 35));

        List<Item> otherHealths = Arrays.asList(
                new Item("Bubble Health", R.drawable.health_5, 20),
                new Item("25 Health", R.drawable.health_25, 20),
                new Item("50 Health", R.drawable.health_50, 20));

        List<Item> weapons = Arrays.asList(
                new Item("Machine Gun", R.drawable.machine_gun, 5),
                new Item("Blaster", R.drawable.blaster, 5),
                new Item("Super Shotgun", R.drawable.shotgun, 5),
                new Item("Rocket Launcher", R.drawable.rocket_launcher, 5),
                new Item("Shaft", R.drawable.shaft, 5),
                new Item("Crossbow", R.drawable.crossbow, 5),
                new Item("Pincer", R.drawable.pincer,5),
                new Item("Grenade Launcher", R.drawable.grenade_launcher, 5));

        Log.i(TAG, String.valueOf(R.drawable.pincer));
        Log.i(TAG, String.valueOf(weapons.get(6).getImageResourceId()));

        List<Item> powerups = Arrays.asList(
                new Item("Siphonator", R.drawable.siphonator, 120),
                new Item("Vanguard", R.drawable.vanguard, 120),
                new Item("Vindicator", R.drawable.vindicator, 120),
                new Item("Diabotical", R.drawable.diabotical, 240));

        itemTypes = new ArrayList<>(Arrays.asList(
                new ItemType("Red Armor", redArmor),
                new ItemType("Other Armors", otherArmors),
                new ItemType("Mega Health", megaHealth),
                new ItemType("Other Health", otherHealths),
                new ItemType("Weapon", weapons),
                new ItemType("Powerup", powerups)
        ));
    }

    private void startTimeTrial(ArrayList<ItemType> itemTypes, Integer calcNum) {
        Intent intent = new Intent(MainActivity.this, TimeTrialActivity.class);
        intent.putParcelableArrayListExtra(SELECTED_ITEM_TYPES, itemTypes);
        intent.putExtra(CALCULATION_NUMBER, calcNum);
        startActivity(intent);
    }
}

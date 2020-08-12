package hu.respawncontrol;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class TimeTrialActivity extends AppCompatActivity {

    private static final String TAG = "TimeTrialActivity";
    private static Random random;

    private TextView tvTimer;
    private TextView tvPickupTime;
    private ImageView ivItem;
    private TextView tvRespawnTime;
    private EditText etRespawnTime;

    private ArrayList<ItemType> itemTypes;
    private Integer calculationNumber;

    private long startTime;
    private Boolean recheckInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_trial);

        random = new Random();

        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvPickupTime = (TextView) findViewById(R.id.tvPickupTime);
        ivItem = (ImageView) findViewById(R.id.ivItem);
        tvRespawnTime = (TextView) findViewById(R.id.tvRespawnTime);
        etRespawnTime = (EditText) findViewById(R.id.etRespawnTime);

        etRespawnTime.setPressed(true);
        etRespawnTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 2) {
                    recheckInput = false;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Intent intent = getIntent();
        itemTypes = intent.getParcelableArrayListExtra(MainActivity.SELECTED_ITEM_TYPES);
        calculationNumber = intent.getIntExtra(MainActivity.CALCULATION_NUMBER, -1);

        startTimeTrial();
    }

    private void startTimeTrial() {
        ArrayList<Item> itemsToDisplay = new ArrayList<>();
        ArrayList<Integer> pickupSeconds = new ArrayList<>();
        ArrayList<Integer> respawnSeconds = new ArrayList<>();
        for(int i = 0; i < calculationNumber; i++) {
            // Select random item type and item
            ItemType randomItemType = itemTypes.get(random.nextInt(itemTypes.size()));
            ArrayList<Item> items = (ArrayList<Item>) randomItemType.getItems();
            Item randomItem = items.get(random.nextInt(items.size()));
            itemsToDisplay.add(randomItem);

            // Create random time (0-59) for pickup
            Integer randomPickupSecond = random.nextInt(60);
            pickupSeconds.add(randomPickupSecond);

            // Calculate time for respawn (solution)
            respawnSeconds.add((randomPickupSecond + randomItem.getRespawnTimeInSeconds()) % 60);
        }

        // Run tests
        startTime = SystemClock.elapsedRealtime();
        TestRunner testRunner = new TestRunner(itemsToDisplay, pickupSeconds, respawnSeconds);
        Thread testThread = new Thread(testRunner);
        testThread.start();
    }

    private class TestRunner implements Runnable {
        private ArrayList<Item> itemsToDisplay;
        private ArrayList<Integer> pickupSeconds, respawnSeconds;

        private long lastTimerUpdate;

        TestRunner(ArrayList<Item> itemsToDisplay, ArrayList<Integer> pickupSeconds, ArrayList<Integer> respawnSeconds) {
            this.itemsToDisplay = itemsToDisplay;
            this.pickupSeconds = pickupSeconds;
            this.respawnSeconds = respawnSeconds;
        }

        @Override
        public void run() {
            for(int i = 0; i < itemsToDisplay.size(); i++) {
                recheckInput = true;
                Boolean correct = displayItem(itemsToDisplay.get(i), pickupSeconds.get(i), respawnSeconds.get(i));

                if(!correct) {
                    return;
                }
            }
        }

        private Boolean displayItem(Item item, Integer pickupSecond, Integer respawnSecond) {
            setPickupTime("X:" + (pickupSecond < 10 ? "0" + pickupSecond : pickupSecond));
            setRespawnTime("X:");
            setEditTextValue("");
            setItemImage(item.getImageResourceId());

            while(recheckInput) {
                final long currentTime = SystemClock.elapsedRealtime();
                if((currentTime - lastTimerUpdate) / 1000 >= 1) {
                    updateTimer(currentTime);
                    lastTimerUpdate = currentTime;
                }
            }
            final long currentTime = SystemClock.elapsedRealtime();
            if((currentTime - lastTimerUpdate) / 1000 >= 1) {
                updateTimer(currentTime);
                lastTimerUpdate = currentTime;
            }

            String input = etRespawnTime.getText().toString();

            boolean correct = Integer.parseInt(input) == respawnSecond;
            if(correct) {
                return true;
            } else {
                setTimerText("WRONG!");
                return false;
            }
        }
    }

    private void updateTimer(long currentTime) {
        long elapsedSeconds = (currentTime - startTime) / 1000;
        setTimerText(String.valueOf(elapsedSeconds));
    }

    private void setTimerText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTimer.setText(text);
            }
        });
    }

    private void setPickupTime(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvPickupTime.setText(text);
            }
        });
    }

    private void setRespawnTime(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvRespawnTime.setText(text);
            }
        });
    }

    private void setEditTextValue(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etRespawnTime.setText(text);
            }
        });
    }

    private void setItemImage(final Integer imageResourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivItem.setImageDrawable(getDrawable(imageResourceId));
            }
        });
    }
}

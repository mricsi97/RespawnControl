package hu.respawncontrol;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import hu.respawncontrol.data.Item;
import hu.respawncontrol.data.ItemType;

public class TimeTrialActivity extends AppCompatActivity {

    public static final String ITEMS_TESTED = "ITEMS_TESTED";
    public static final String SOLVE_TIMES = "SOLVE_TIMES";

    private static final String TAG = "TimeTrialActivity";
    private static Random random;

    private TextView tvTimer;
    private TextView tvPickupTime;
    private ImageView ivItem;
    private TextView tvRespawnMinute;
    private EditText etRespawnMinute;
    private EditText etRespawnSecond;

    private ArrayList<ItemType> itemTypes;
    private Integer calculationNumber;

    private long startTime;
    private Boolean isSecondInputLongerThanTwo;
    private SimpleDateFormat timerFormatter = new SimpleDateFormat("m:ss.SS", Locale.ROOT);
    private SimpleDateFormat pickupFormatter = new SimpleDateFormat("m:ss", Locale.ROOT);
    private SimpleDateFormat respawnMinuteFormatter = new SimpleDateFormat("m", Locale.ROOT);

    boolean minuteHintEnabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_trial);

        random = new Random();
        minuteHintEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("time_trial_minute_hint", false);

        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvPickupTime = (TextView) findViewById(R.id.tvPickupTime);
        ivItem = (ImageView) findViewById(R.id.ivItem_timeTrial);
        tvRespawnMinute = (TextView) findViewById(R.id.tvRespawnMinute);
        etRespawnMinute = (EditText) findViewById(R.id.etRespawnMinute);
        etRespawnSecond = (EditText) findViewById(R.id.etRespawnSecond);

        etRespawnSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 2) {
                    isSecondInputLongerThanTwo = true;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        etRespawnMinute.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= 2) {
                    etRespawnSecond.requestFocus();
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
        ArrayList<Long> pickupMoments = new ArrayList<>();
        ArrayList<Long> respawnMoments = new ArrayList<>();
        for(int i = 0; i < calculationNumber; i++) {
            // Select random item type and item
            ItemType randomItemType = itemTypes.get(random.nextInt(itemTypes.size()));
            ArrayList<Item> items = (ArrayList<Item>) randomItemType.getItems();
            Item randomItem = items.get(random.nextInt(items.size()));
            itemsToDisplay.add(randomItem);

            // Create random time for pickup in milliseconds
            long respawnTime = randomItem.getRespawnTimeInSeconds() * 1000;
            long randomPickupMoment = random.nextInt(780) * 1000;
            pickupMoments.add(randomPickupMoment);

            // Calculate time for respawn (solution)
            long respawnMoment = randomPickupMoment + respawnTime;
            respawnMoments.add(respawnMoment);
        }

        // Run tests
        TestRunner testRunner = new TestRunner(itemsToDisplay, pickupMoments, respawnMoments);
        Thread testThread = new Thread(testRunner);
        testThread.start();
    }

    private class TestRunner implements Runnable {
        private ArrayList<Item> itemsToDisplay;
        private ArrayList<Long> pickupMoments, respawnMoments;

        private long lastTimerUpdate;
        private long[] solveTimes;

        TestRunner(ArrayList<Item> itemsToDisplay, ArrayList<Long> pickupMoments, ArrayList<Long> respawnMoments) {
            this.itemsToDisplay = itemsToDisplay;
            this.pickupMoments = pickupMoments;
            this.respawnMoments = respawnMoments;
        }

        @Override
        public void run() {
//            solveTimes = new ArrayList<>();
            solveTimes = new long[itemsToDisplay.size()];
            startTime = SystemClock.elapsedRealtime();
            for(int i = 0; i < itemsToDisplay.size(); i++) {
                isSecondInputLongerThanTwo = false;
                Long solveTime = doTest(itemsToDisplay.get(i), pickupMoments.get(i), respawnMoments.get(i));

                if(solveTime == -1L) {
                    // TODO: lose
                    return;
                } else {
//                    solveTimes.add(solveTime);
                    solveTimes[i] = solveTime;
                }
            }

            // TODO: saveResults();
            displayResults();
        }

        private Long doTest(Item item, Long pickupMoment, Long respawnMoment) {
            boolean minuteInputRequired = item.getRespawnTimeInSeconds() > 60;

            // Display times, input fields and icon
            setEtRespawnSecond("");
            setPickupTime(pickupMoment);
            if(minuteInputRequired) {
                setMinuteInput(true);
                setEtRespawnMinute("");
                setTvRespawnMinute("00");   // Needed for spacing
            } else {
                setMinuteInput(false);

                if(minuteHintEnabled) {
                    setTvRespawnMinute(respawnMoment);
                } else {
                    setTvRespawnMinute("X");
                }
            }
            setItemImage(item.getImageResourceId());

            long elapsedTime = 0;
            do {
                final long currentTime = SystemClock.elapsedRealtime();
                // Update only every 0.01 seconds (so the UI thread has time)
                if((currentTime - lastTimerUpdate) / 10 >= 1) {
                    elapsedTime = updateTimer(currentTime);
                    lastTimerUpdate = currentTime;
                }
            } while(!isSecondInputLongerThanTwo);

            boolean correct;
            String secondInput = etRespawnSecond.getText().toString();
            if(minuteInputRequired) {
                String minuteInput = etRespawnMinute.getText().toString();
                correct = (Long.parseLong(minuteInput) * 60000 + Long.parseLong(secondInput) * 1000) == respawnMoment;
            } else {
                correct = Long.parseLong(secondInput) * 1000 == respawnMoment % 60000;
            }

            if(correct) {
                return elapsedTime;
            } else {
                setTimerText("WRONG!");
                return -1L;
            }
        }

        private void displayResults() {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ITEMS_TESTED, itemsToDisplay);
            bundle.putLongArray(SOLVE_TIMES, solveTimes);
            TimeTrialResultDialog resultDialog = new TimeTrialResultDialog();
            resultDialog.setArguments(bundle);
            resultDialog.show(getSupportFragmentManager(), "TimeTrialResultDialog");
        }
    }

    private long updateTimer(long currentTime) {
        long elapsedTime = (currentTime - startTime);
        setTimerText(timerFormatter.format(elapsedTime));

        return elapsedTime;
    }

    private void setTimerText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTimer.setText(text);
            }
        });
    }

    private void setPickupTime(final Long time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvPickupTime.setText(pickupFormatter.format(time));
            }
        });
    }

    private void setTvRespawnMinute(final Long time) {
        setTvRespawnMinute(respawnMinuteFormatter.format(time));
    }

    private void setTvRespawnMinute(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvRespawnMinute.setText(text);
            }
        });
    }

    private void setEtRespawnMinute(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etRespawnMinute.setText(text);
            }
        });
    }

    private void setEtRespawnSecond(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etRespawnSecond.setText(text);
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

    private void setMinuteInput(final Boolean minuteInputRequired) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etRespawnMinute.setEnabled(minuteInputRequired);
                etRespawnMinute.setVisibility(minuteInputRequired ? View.VISIBLE : View.INVISIBLE);

                tvRespawnMinute.setEnabled(!minuteInputRequired);
                tvRespawnMinute.setVisibility(!minuteInputRequired ? View.VISIBLE : View.INVISIBLE);

                if(minuteInputRequired) {
                    etRespawnMinute.requestFocus();
                } else {
                    etRespawnSecond.requestFocus();
                }
            }
        });
    }

}

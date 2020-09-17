package hu.respawncontrol.view.activity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import hu.respawncontrol.helper.MusicManager;
import hu.respawncontrol.R;
import hu.respawncontrol.view.fragment.TimeTrialOptionsDialog;
import hu.respawncontrol.view.fragment.TimeTrialResultDialog;
import hu.respawncontrol.viewmodel.TimeTrialViewModel;
import hu.respawncontrol.model.room.entity.Item;

public class TimeTrialActivity extends AppCompatActivity implements TimeTrialOptionsDialog.DialogFinishedListener {

    private static final String TAG = "TimeTrialActivity";

    private TimeTrialViewModel viewModel;
    
    private TextView tvCountdown;
    private TextView tvTestProgress;
    private ImageButton btnReplayTopRight;
    private TextView tvTimer;
    private TextView tvPickup;
    private TextView tvPickupTime;
    private ImageView ivItem;
    private TextView tvRespawn;
    private TextView tvRespawnMinute;
    private EditText etRespawnMinute;
    private TextView tvSeparator;
    private EditText etRespawnSecond;
    private ImageButton btnReplayHidden;

    private long startTime;
    private Boolean isSecondInputLongerThanTwo;
    private SimpleDateFormat timerFormatter = new SimpleDateFormat("m:ss.SS", Locale.ROOT);
    private SimpleDateFormat pickupFormatter = new SimpleDateFormat("m:ss", Locale.ROOT);
    private SimpleDateFormat respawnMinuteFormatter = new SimpleDateFormat("m", Locale.ROOT);

    private boolean minuteHintEnabled;
    private int countdownSeconds;
    private boolean countdownSoundEnabled;
    private boolean itemSoundsEnabled;
    private boolean voiceLinesEnabled;

    private int minuteEtMaxLength = 2;

    private SoundPool soundPool;
    HashMap<String, ArrayList<Integer>> soundIdLists = new HashMap<>();
    private final static int MAX_VOLUME = 100;
    private float countdownVolume;
    private float itemSoundVolume;

    private int countdownSoundId;

    private ArrayList<Item> testItems;
    private ArrayList<Long> pickupMoments;
    private ArrayList<Long> respawnMoments;

    private static Thread testThread;
    private static boolean interruptThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_trial);

        // Start setup dialog
        TimeTrialOptionsDialog dialog = new TimeTrialOptionsDialog();
        dialog.show(getSupportFragmentManager(), "TimeTrialOptionsDialog");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        minuteHintEnabled = preferences.getBoolean("time_trial_minute_hint", false);
        countdownSeconds = Integer.parseInt(preferences.getString("time_trial_countdown_seconds", "3"));
        countdownSoundEnabled = preferences.getBoolean("countdown_sound", false);
        countdownVolume = (float) (1 - (Math.log(MAX_VOLUME + 1 - preferences.getInt("countdown_volume", 100)) / Math.log(MAX_VOLUME)));
        itemSoundsEnabled = preferences.getBoolean("item_sounds", false);
        itemSoundVolume = (float) (1 - (Math.log(MAX_VOLUME + 1 - preferences.getInt("pickup_volume", 95)) / Math.log(MAX_VOLUME)));
        voiceLinesEnabled = preferences.getBoolean("voice_lines", false);

        if(itemSoundsEnabled || countdownSoundEnabled) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                        .build();

                soundPool = new SoundPool.Builder()
                        .setMaxStreams(3)
                        .setAudioAttributes(audioAttributes)
                        .build();
            } else {
                soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
            }

            soundPool.play(countdownSoundId, 0, 0, 1, -1, 1f);
        }

        if(countdownSoundEnabled) {
            countdownSoundId = soundPool.load(this, R.raw.ui_countdown, 1);
        }

        tvCountdown = (TextView) findViewById(R.id.tvCountdown);
        tvTestProgress = (TextView) findViewById(R.id.tvTestProgress);
        btnReplayTopRight = (ImageButton) findViewById(R.id.btnReplay_topRight);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvPickup = (TextView) findViewById(R.id.tvPickup);
        tvPickupTime = (TextView) findViewById(R.id.tvPickupTime);
        ivItem = (ImageView) findViewById(R.id.ivItem_timeTrial);
        tvRespawn = (TextView) findViewById(R.id.tvRespawn);
        tvRespawnMinute = (TextView) findViewById(R.id.tvRespawnMinute);
        etRespawnMinute = (EditText) findViewById(R.id.etRespawnMinute);
        tvSeparator = (TextView) findViewById(R.id.tvSeparator);
        etRespawnSecond = (EditText) findViewById(R.id.etRespawnSecond);
        btnReplayHidden = (ImageButton) findViewById(R.id.btnReplay_hidden);

        btnReplayTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartTimeTrial(true);
            }
        });

        etRespawnMinute.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() >= minuteEtMaxLength) {
                    etRespawnSecond.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

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

        btnReplayHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartTimeTrial(true);
            }
        });

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TimeTrialViewModel.class);

        viewModel.getTestItems().observe(this,
                new Observer<List<Item>>() {
                    @Override
                    public void onChanged(List<Item> items) {
                        testItems = (ArrayList<Item>) items;
                        loadSoundsForItems(items);
                    }
                });
        viewModel.getPickupMoments().observe(this,
                new Observer<List<Long>>() {
                    @Override
                    public void onChanged(List<Long> longs) {
                        pickupMoments = (ArrayList<Long>) longs;
                    }
                });
        viewModel.getRespawnMoments().observe(this,
                new Observer<List<Long>>() {
                    @Override
                    public void onChanged(List<Long> longs) {
                        respawnMoments = (ArrayList<Long>) longs;
                    }
                });
    }

    @Override
    public void dialogFinished() {
        startTimeTrial();
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music_toggle", false);
        if(musicEnabled) {
            MusicManager musicManager = MusicManager.getInstance(this);
            musicManager.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        
        hideKeyboard();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music_toggle", false);
        if(musicEnabled) {
            MusicManager musicManager = MusicManager.getInstance(this);
            musicManager.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        interruptThread = true;

        if(soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    private void startTimeTrial() {
        showBigReplayButton(false);

        viewModel.prepareTests();

        // Run tests
        TestRunner testRunner = new TestRunner(testItems, soundIdLists, pickupMoments, respawnMoments);
        interruptThread = false;
        testThread = new Thread(testRunner);
        testThread.start();
    }

    private void loadSoundsForItems(List<Item> items) {
        if(itemSoundsEnabled) {
            for(Item item : items) {
                String itemName = item.getItemName();

                if(soundIdLists.containsKey(itemName)){
                    continue;
                }

                Resources res = getResources();
                ArrayList<String> soundResourceEntryNames = (ArrayList<String>) item.getSoundResourceEntryNames();
                ArrayList<Integer> soundIdList = new ArrayList<>();
                for(String resourceEntryName : soundResourceEntryNames) {
                    Integer soundId = soundPool.load(this, res.getIdentifier(resourceEntryName, "raw", getPackageName()), 1);
                    soundIdList.add(soundId);
                }

                soundIdLists.put(itemName, soundIdList);
            }
        }
    }

    private class TestRunner implements Runnable {
        private ArrayList<Item> itemsToTest;
        private HashMap<String, ArrayList<Integer>> soundIdLists;
        private ArrayList<Long> pickupMoments, respawnMoments;

        private long lastTimerUpdate;
        private ArrayList<Long> solveTimes;

        TestRunner(ArrayList<Item> itemsToTest, HashMap<String, ArrayList<Integer>> soundIdLists, ArrayList<Long> pickupMoments, ArrayList<Long> respawnMoments) {
            this.itemsToTest = itemsToTest;
            this.soundIdLists = soundIdLists;
            this.pickupMoments = pickupMoments;
            this.respawnMoments = respawnMoments;
            solveTimes = new ArrayList<>();
        }

        @Override
        public void run() {
            doCountdown(countdownSeconds);

            if(interruptThread) {
                return;
            }

            showKeyboard();

            startTime = SystemClock.elapsedRealtime();
            for(int i = 0; i < itemsToTest.size(); i++) {
                isSecondInputLongerThanTwo = false;
                Long solveTime = doTest(i, itemsToTest.get(i), pickupMoments.get(i), respawnMoments.get(i));

                if(solveTime == -1L) { // Wrong solution
                    displayGameOver();
                    return;
                } else if(solveTime == -2L) { // Thread interrupted (~ game restarted)
                    return;
                } else {
                    solveTimes.add(solveTime);
                }
            }

            setTvTestProgress(itemsToTest.size(), itemsToTest.size());

            displayAndSaveResults();
        }

        private void doCountdown(int countDownSeconds) {
            if(countDownSeconds <= 0) {
                return;
            }

            showUI(false);

            showTvCountdown(true);
            while(countDownSeconds > 0) {
                if(interruptThread) {
                    return;
                }
                if(countdownSoundEnabled) {
                    soundPool.play(countdownSoundId, countdownVolume, countdownVolume, 0, 0, 1);
                }
                setCountdownText(String.valueOf(countDownSeconds));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownSeconds--;
            }

            showTvCountdown(false);
            showUI(true);
        }

        private Long doTest(int progress, Item item, Long pickupMoment, Long respawnMoment) {
            boolean minuteInputRequired = item.getRespawnTimeInSeconds() > 60;

            // Display times, input fields and icon
            setTvTestProgress(progress, itemsToTest.size());
            setEtRespawnSecond("");
            setPickupTime(pickupMoment);
            if(minuteInputRequired) {
                setMinuteInput(true);
                setEtRespawnMinute("");
                // Needed for spacing
                setTvRespawnMinute("00");
                // Require 2 decimals if respawn time is 10 mins or higher, 1 digits otherwise
                setEtRespawnMinuteMaxLength((respawnMoment / 1000 > 599 ? 2 : 1));
            } else {
                setMinuteInput(false);

                if(minuteHintEnabled) {
                    setTvRespawnMinute(respawnMoment);
                } else {
                    setTvRespawnMinute("X");
                }
            }
            setItemImage(getResources().getIdentifier(item.getImageResourceName(), "drawable", getPackageName()));

            // Play sounds for the item
            if(itemSoundsEnabled) {
                ArrayList<Integer> soundIds = soundIdLists.get(item.getItemName());
                if(soundIds != null) {
                    int numberOfSoundsToPlay = voiceLinesEnabled ? 2 : 1;
                    for(int i = 0; i < numberOfSoundsToPlay && i < soundIds.size(); i++) {
                        soundPool.play(soundIds.get(i), itemSoundVolume, itemSoundVolume, 0, 0, 1);
                    }
                }
            }

            long elapsedTime = 0;
            do {
                if(Thread.interrupted()) {
                    return -2L;
                }
                final long currentTime = SystemClock.elapsedRealtime();
                // Update only every 0.01 seconds (so the UI thread has time)
                if((currentTime - lastTimerUpdate) / 10 >= 1) {
                    elapsedTime = updateTimer(currentTime);
                    lastTimerUpdate = currentTime;
                }
            } while(!isSecondInputLongerThanTwo);

            boolean correct;
            String secondInput = etRespawnSecond.getText().toString();
            try {
                if (minuteInputRequired) {
                    String minuteInput = etRespawnMinute.getText().toString();
                    correct = (Long.parseLong(minuteInput) * 60000 + Long.parseLong(secondInput) * 1000) == respawnMoment;
                } else {
                    correct = Long.parseLong(secondInput) * 1000 == respawnMoment % 60000;
                }
            } catch (NumberFormatException e) { // If input was not a number (could not be parsed)
                correct = false;
            }

            if(correct) {
                return elapsedTime;
            } else {
                return -1L;
            }
        }

        private void displayAndSaveResults() {
            hideKeyboard();

            viewModel.setSolveTimes(solveTimes);

            TimeTrialResultDialog resultDialog = new TimeTrialResultDialog();
            resultDialog.show(getSupportFragmentManager(), "TimeTrialResultDialog");
        }

        private void displayGameOver() {
            setTimerText("WRONG!");
            hideKeyboard();
            showBigReplayButton(true);
        }
    }

    public void restartTimeTrial(Boolean showKeyboard) {
        if(showKeyboard) {
            showKeyboard();
        }

        testThread.interrupt();
        try {
            testThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startTimeTrial();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void showUI(final Boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(show) {
                    tvTestProgress.setVisibility(View.VISIBLE);
                    btnReplayTopRight.setVisibility(View.VISIBLE);
                    tvTimer.setVisibility(View.VISIBLE);
                    tvPickup.setVisibility(View.VISIBLE);
                    tvPickupTime.setVisibility(View.VISIBLE);
                    ivItem.setVisibility(View.VISIBLE);
                    tvRespawn.setVisibility(View.VISIBLE);
                    tvSeparator.setVisibility(View.VISIBLE);
                    etRespawnSecond.setVisibility(View.VISIBLE);
                } else {
                    tvTestProgress.setVisibility(View.GONE);
                    btnReplayTopRight.setVisibility(View.GONE);
                    tvTimer.setVisibility(View.GONE);
                    tvPickup.setVisibility(View.GONE);
                    tvPickupTime.setVisibility(View.GONE);
                    ivItem.setVisibility(View.GONE);
                    tvRespawn.setVisibility(View.GONE);
                    tvSeparator.setVisibility(View.GONE);
                    tvRespawnMinute.setVisibility(View.GONE);
                    etRespawnMinute.setVisibility(View.GONE);
                    etRespawnSecond.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showTvCountdown(final Boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(show) {
                    tvCountdown.setVisibility(View.VISIBLE);
                } else {
                    tvCountdown.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showBigReplayButton(final Boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(show) {
                    btnReplayHidden.setVisibility(View.VISIBLE);
                } else {
                    btnReplayHidden.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setCountdownText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCountdown.setText(text);
            }
        });
    }

    private void setTvTestProgress(final int progress, final int testCount) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTestProgress.setText(progress + "/" + testCount);
            }
        });
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

    private void setEtRespawnMinuteMaxLength(final int length) {
        minuteEtMaxLength = length;
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

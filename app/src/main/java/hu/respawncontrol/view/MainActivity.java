package hu.respawncontrol.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import hu.respawncontrol.helper.MusicManager;
import hu.respawncontrol.R;

public class MainActivity extends AppCompatActivity /*implements OnCompleteListener<GoogleSignInAccount>*/ {

    private static final String TAG = "MainActivity";
//    public static final int RC_SIGN_IN = 1001;
//    public static final int PLAY_SERVICES_AVAILABILITY_REQUEST = 1002;

//    private GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
//                .build();
//        signInClient = GoogleSignIn.getClient(this, signInOptions);

        final Button btnTimeTrial = (Button) findViewById(R.id.btnTimeTrial);
        btnTimeTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimeTrialActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

//        final SignInButton btnSignIn = (SignInButton) findViewById(R.id.btnSignIn);
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startInteractiveSignIn();
//            }
//        });
//
//        final Button btnSignOut = (Button) findViewById(R.id.btnSignOut);
//        btnSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//        });


//        final ImageButton btnLeaderboard = (ImageButton) findViewById(R.id.btnLeaderboard);
//        btnLeaderboard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
////                intent.putExtra(PLAYER_ID, player.getId());
//                startActivity(intent);
//            }
//        });
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
    protected void onResume() {
        super.onResume();

//        int availabilityResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
//        if(availabilityResult == ConnectionResult.SUCCESS) {
//            signInSilently();
//        } else {
//            GoogleApiAvailability.getInstance().getErrorDialog(this, availabilityResult, PLAY_SERVICES_AVAILABILITY_REQUEST).show();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music_toggle", false);
        if(musicEnabled) {
            MusicManager musicManager = MusicManager.getInstance(this);
            musicManager.onStop();
        }
    }

//    private void signInSilently() {
//        // Check if already signed in
//        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if(lastSignedInAccount != null && GoogleSignIn.hasPermissions(lastSignedInAccount)) {
//            displaySignOutButton(lastSignedInAccount);
//            return;
//        }
//        // If not already signed in, try silent sign-in
//        signInClient
//                .silentSignIn()
//                .addOnCompleteListener(
//                        this,
//                        new OnCompleteListener<GoogleSignInAccount>() {
//                            @Override
//                            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
//                                if(task.isSuccessful()) {
//                                    GoogleSignInAccount account = task.getResult();
//                                    displaySignOutButton(account);
//                                } else {
//                                    displaySignInButton();
//                                }
//                            }
//                        }
//                );
//    }

//    private void startInteractiveSignIn() {
//        Intent intent = signInClient.getSignInIntent();
//        startActivityForResult(intent, RC_SIGN_IN);
//    }

//    private void signOut() {
//        signInClient.signOut().addOnCompleteListener(this,
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        displaySignInButton();
//                    }
//                });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            task.addOnCompleteListener(this);
//        }
//    }

//    @Override
//    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
//        handleSignInResult(task);
//    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            displaySignOutButton(account);
//        } catch (ApiException e) {
//            new AlertDialog.Builder(this).setMessage(e.getMessage())
//                        .setNeutralButton(android.R.string.ok, null).show();
//            displaySignInButton();
//        }
//    }

//    private void displaySignOutButton(GoogleSignInAccount account) {
//        PlayersClient playersClient = Games.getPlayersClient(this, account);
//        Task<Player> task = playersClient.getCurrentPlayer();
//        task.addOnCompleteListener(this,
//                new OnCompleteListener<Player>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Player> task) {
//                        if(task.isSuccessful()) {
//                            Player player = task.getResult();
//
//                            final Button btnSignOut = (Button) findViewById(R.id.btnSignOut);
//                            btnSignOut.setVisibility(View.VISIBLE);
//                            btnSignOut.setText(player.getDisplayName());
//
//                            findViewById(R.id.btnSignIn).setVisibility(View.GONE);
//                        } else {
//                            ApiException apiException = (ApiException) task.getException();
//                            int statusCode = apiException.getStatusCode();
//                            if(statusCode == CommonStatusCodes.SIGN_IN_REQUIRED) {
//                                startInteractiveSignIn();
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void displaySignInButton() {
//        findViewById(R.id.btnSignIn).setVisibility(View.VISIBLE);
//        findViewById(R.id.btnSignOut).setVisibility(View.GONE);
//    }
}

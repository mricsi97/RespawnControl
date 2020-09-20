package hu.respawncontrol.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import hu.respawncontrol.helper.MusicManager;
import hu.respawncontrol.R;
import hu.respawncontrol.view.fragment.ItemGroupsFragment;
import hu.respawncontrol.view.fragment.HomeFragment;
import hu.respawncontrol.view.fragment.SettingsFragment;
import hu.respawncontrol.view.fragment.StatsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener/*implements OnCompleteListener<GoogleSignInAccount>*/ {
    private DrawerLayout drawer;

    private static final String TAG = "MainActivity";
//    public static final int RC_SIGN_IN = 1001;
//    public static final int PLAY_SERVICES_AVAILABILITY_REQUEST = 1002;

//    private GoogleSignInClient signInClient;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            switchToHomeFragment();
        }

//        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
//                .build();
//        signInClient = GoogleSignIn.getClient(this, signInOptions);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                setTitle("Home");
                break;
            case R.id.nav_stats:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatsFragment()).commit();
                setTitle("Stats");
                break;
//            case R.id.nav_leaderboards:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new LeaderboardsFragment()).commit();
//            setTitle("Leaderboards");
//                break;
            case R.id.nav_itemgroups:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ItemGroupsFragment()).commit();
                setTitle("Item Groups");
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                setTitle("Settings");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // If something is on the back stack, act normally
            if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
                super.onBackPressed();
                return;
            }
            // If nothing on the back stack (aka we are on a navigation fragment)
            // then check if we are on the home screen.
            // If not, switch to it.
            // If yes, act normally (exit app).
            int itemId = navigationView.getCheckedItem().getItemId();
            if (itemId != R.id.nav_home) {
                switchToHomeFragment();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void switchToHomeFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);
        setTitle("Home");
    }

    public void switchToStatsFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new StatsFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_stats);
        setTitle("Stats");
    }

//    public void switchToLeaderboardsFragment() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new LeaderboardsFragment()).commit();
//        navigationView.setCheckedItem(R.id.nav_leaderboards);
//        setTitle("Leaderboards");
//    }

    public void switchToSettingsFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SettingsFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_settings);
        setTitle("Settings");
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("music_toggle", false);
        if (musicEnabled) {
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
        if (musicEnabled) {
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

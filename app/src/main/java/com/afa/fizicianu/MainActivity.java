package com.afa.fizicianu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afa.fizicianu.database.SQLController;
import com.afa.fizicianu.fragments.ChatFragment;
import com.afa.fizicianu.fragments.GameFragment;
import com.afa.fizicianu.fragments.LeaderboardFragment;
import com.afa.fizicianu.fragments.LectiiFragment;
import com.afa.fizicianu.fragments.SettingsFragment;
import com.google.android.gms.*;
import io.smooch.ui.ConversationActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private static int RC_SIGN_IN = 9001;
    public GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;
    private boolean mAutoStartSignInFlow = true;

    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;
    TextView hsv;
    App app;

    private static final String TAG = "MainActivity";
    private View header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (App) getApplication();
        //Replace ActionBar with toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Joaca");
        //Bindings
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.hamburgerMenu);

        //Set-ups
        setupDrawer(nvDrawer);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle = setupDrawerToggle();



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        header = nvDrawer.getHeaderView(0);
        header.findViewById(R.id.sign_in_button).setOnClickListener(this);
        TextView utv = (TextView) header.findViewById(R.id.username);
        utv.setText(app.getUsername());
        hsv= (TextView) header.findViewById(R.id.TVhighscore);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.sp),MODE_PRIVATE);
        hsv.setText(hsv.getText()+String.valueOf(preferences.getInt("PersonalScore",0)));

        //Track daily login for achivement
        SharedPreferences.Editor editor = preferences.edit();
        Calendar cal = Calendar.getInstance();
        int logintimes = preferences.getInt("AchivTimp",0);
        if(logintimes==0){
            logintimes++;
            editor.putInt("AchivTimpZi",(int) cal.getTimeInMillis()/(1000*60*60*24));
            editor.commit();
        }
        if(logintimes!=0 && preferences.getInt("AchivTimpZi",0)>=24&& preferences.getInt("AchivTimpZi",0)<=48){
            logintimes++;
            editor.putInt("AchivTimp",logintimes);
            editor.commit();
        }
        if(preferences.getInt("AchivTimpZi",0)>=48){
            editor.putInt("AchivTimp",0);
            editor.commit();
            logintimes=0;
        }
        //Check for achivments
        if(mGoogleApiClient.isConnected()) {
            switch (logintimes) {
                case 3:
                    Games.Achievements.unlock(mGoogleApiClient, "CgkI5Oyeu-UdEAIQBg");
                    break;
                case 7:
                    Games.Achievements.unlock(mGoogleApiClient, "CgkI5Oyeu-UdEAIQBw");
                    break;
                case 31:
                    Games.Achievements.unlock(mGoogleApiClient, "CgkI5Oyeu-UdEAIQCA");
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
        nvDrawer.getMenu().getItem(0).setChecked(true);
        selectDrawerItem(nvDrawer.getMenu().getItem(0));
    }

    //Behaviour on selected drawer item
    public void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_first_fragment:
                getFragmentManager().beginTransaction().replace(R.id.contentFrame, new GameFragment()).commit();
                break;
            case R.id.nav_second_fragment:
                if(mGoogleApiClient.isConnected()){
                    Log.v("Clasament","E conectat");
                    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                            "CgkI5Oyeu-UdEAIQAg"), 231);
                }
                else {
                    Toast.makeText(this,"You need to be connected to your Google account",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_third_fragment:
                getFragmentManager().beginTransaction().replace(R.id.contentFrame, new ChatFragment()).commit();
                break;
            case R.id.nav_fourth_fragment:
                getFragmentManager().beginTransaction().replace(R.id.contentFrame, new LectiiFragment()).commit();
                break;
            case R.id.nav_fifth_fragment:
                ConversationActivity.show(this);
                break;
            case R.id.nav_sixth_fragment:
                if(mGoogleApiClient.isConnected())
                startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),99);
                else
                Toast.makeText(this,"You need to be connected to your Google account",Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.v("TEST", "default");
        }

        mDrawer.closeDrawers();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this,"Succesfuly logged in",Toast.LENGTH_LONG).show();
        header.findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.sp),MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USERNAME",Games.Players.getCurrentPlayer(mGoogleApiClient).getDisplayName());
        editor.commit();
        App app = (App) getApplication();
        app.setUsername(Games.Players.getCurrentPlayer(mGoogleApiClient).getDisplayName());
        Log.e("test", Games.Players.getCurrentPlayer(mGoogleApiClient).getHiResImageUrl());
        String url = Games.Players.getCurrentPlayer(mGoogleApiClient).getHiResImageUrl();
        Picasso.with(this).load(url).into((ImageView) header.findViewById(R.id.profile_image));

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(mResolvingConnectionFailure){
            Log.v(TAG,"Already resolving");
        }
        if(mSignInClicked || mAutoStartSignInflow){
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            if (!BaseGameUtils.resolveConnectionFailure(this,mGoogleApiClient,connectionResult,RC_SIGN_IN,"Error failed")) {
                mResolvingConnectionFailure = false;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sign_in_button){
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }

    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.activityResumed();
    }

    public void updateHeader(){

        int hs = getSharedPreferences(getString(R.string.sp),MODE_PRIVATE).getInt("PersonalScore",0);
        hsv.setText("Highscore: "+String.valueOf(hs));
    }
}
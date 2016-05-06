package com.afa.fizicianu;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.smooch.core.Smooch;

public class App extends Application {

    private static boolean activityVisible;

    public String username;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Smooch.init(this, "66ttonyat29f5fu8lrum59qz5");
        sharedPreferences = getSharedPreferences(getString(R.string.sp), Context.MODE_PRIVATE);
        username = sharedPreferences.getString("USERNAME","Player");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
        Log.v("test","onscreen");
    }

    public static void activityPaused() {
        activityVisible = false;
        Log.v("test","offscreen");
    }
}

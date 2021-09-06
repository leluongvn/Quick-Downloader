package com.allvideodownloader.quickdownloader2020.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefs {
    public static String preference = "QuickDownloader";
    public static String sessionId = "session_id";
    public static String userId = "user_id";
    public static String cookies = "Cookies";
    public static String csrf = "csrf";
    public static String isInstaLogin = "IsInstaLogin";
    private static SharePrefs instance;

    private SharedPreferences sharedPreferences;


    public SharePrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(preference, 0);
    }

    public static SharePrefs getInstance(Context ctx) {
        if (instance == null) {
            instance = new SharePrefs(ctx);
        }
        return instance;
    }

    public void putString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putInt(String key, Integer val) {
        sharedPreferences.edit().putInt(key, val).apply();
    }

    public void putBoolean(String key, Boolean val) {
        sharedPreferences.edit().putBoolean(key, val).apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void clearSharePrefs() {
        sharedPreferences.edit().clear().apply();
    }
}

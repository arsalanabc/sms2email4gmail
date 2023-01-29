package com.example.sms2gmail.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sms2gmail.R;

import java.util.Map;

public class PreferenceRepository {
    final int PREFERENCE_FILE_KEY = R.string.preference_file_key;

    private Context appCompatActivity;
    private String preferenceFileKey;

    public PreferenceRepository(Context context) {
        this.appCompatActivity = context.getApplicationContext();
        preferenceFileKey =  appCompatActivity.getString(PREFERENCE_FILE_KEY);
    }

    public void saveSettings(Map<String, String> values){
        SharedPreferences sharedPreferences =   appCompatActivity.getSharedPreferences(
                preferenceFileKey, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        values.forEach((k,v) -> {
            editor.putString(k, v);
        });
        editor.apply();
    }

    public String getSettings(String preferenceKey){
        SharedPreferences sharedPreferences =  appCompatActivity.getSharedPreferences(
                preferenceFileKey, Context.MODE_PRIVATE);

        return sharedPreferences.getString(preferenceKey, null);
    }

    public String getSettings(int preferenceKey){
        return this.getSettings(appCompatActivity.getString(preferenceKey));
    }

}

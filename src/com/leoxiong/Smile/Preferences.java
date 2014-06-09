package com.leoxiong.Smile;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Leo on 6/14/2014.
 */
public class Preferences {
    public static final String ALARMS = "alarms";
    private static final String FILENAME = "preferences";
    private static Preferences instance;
    private SharedPreferences mPrefs;

    private Preferences(Context context) {
        mPrefs = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    }

    public static Preferences getInstance(Context context){
        if (instance == null)
            instance = new Preferences(context);
        return instance;
    }

    public <T> T get(String key, TypeToken type) {
        return new Gson().fromJson(mPrefs.getString(key, null), type.getType());
    }

    public boolean put(String key, Object value){
        return mPrefs.edit().putString(key, new Gson().toJson(value)).commit();
    }

    public boolean remove(String key){
        return mPrefs.edit().remove(key).commit();
    }

    public boolean clear(){
        return mPrefs.edit().clear().commit();
    }
}

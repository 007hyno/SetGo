package com.paras.setgo.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class apm {
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LANGUAGE_IS = "LanguageIs";
    private static final String TEXT_SIZE_IS = "textSize";

    public static boolean getFirstTime(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }
    public static void setFirstTime(Context context,Boolean val){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(IS_FIRST_TIME_LAUNCH,val);
        edit.apply();
    }

    public static String secondsToTimeString(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public static Long timeStringToSeconds(String timeString) {
        String[] timeParts = timeString.split(":");
        long  minutes = Integer.parseInt(timeParts[0]);
        long seconds = Integer.parseInt(timeParts[1]);

        return minutes * 60 + seconds;
    }
}

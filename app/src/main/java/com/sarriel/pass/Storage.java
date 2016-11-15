package com.sarriel.pass;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by matej on 27.12.2014.
 */
public class Storage {
    private static final String PREF = "ui_prefs";
    private static final String PREF_SECRET = "pref_secret";
    private static final String PREF_REMEMBER_SECRET = "pref_remember_secret";
    private static final String PREF_COPY_CLIPBOARD = "pref_clipboard";

    /**
     * Save boolean value of user preference to remember secret.
     * @param context
     * @param value
     */
    public static void setRememberSecret(Context context, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_REMEMBER_SECRET, value);
        editor.apply();
    }

    /**
     * Recall saved preference, whether user wants app to remember secret.
     * @param context
     * @return
     */
    public static boolean getRememberSecret(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(PREF_REMEMBER_SECRET, false);
    }

    /**
     * Save boolean value of user preference to copy password to clipboard automatically after
     * generating it.
     * @param context
     * @param value
     */
    public static void setClipboard(Context context, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_COPY_CLIPBOARD, value);
        editor.apply();
    }

    /**
     * Recall saved preference, whether user wants app to automatically copy password to clipboard
     * after generating it.
     * @param context
     * @return
     */
    public static boolean getClipboard(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(PREF_COPY_CLIPBOARD, false);
    }

    /**
     * Save secret string used for generating a password.
     * @param context
     * @param value
     */
    public static void setSecret(Context context, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_SECRET, value);
        editor.apply();
    }

    /**
     * Recall saved secret, used for generating a password.
     * @param context
     * @return
     */
    public static String getSecret(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPref.getString(PREF_SECRET, "");
    }

}

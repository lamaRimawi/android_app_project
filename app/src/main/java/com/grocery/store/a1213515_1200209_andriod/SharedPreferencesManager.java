package com.grocery.store.a1213515_1200209_andriod;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "grocery_prefs";
    private static final String KEY_REMEMBERED_EMAIL = "remembered_email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_IS_ADMIN = "is_admin";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveRememberedEmail(String email) {
        editor.putString(KEY_REMEMBERED_EMAIL, email);
        editor.apply();
    }

    public String getRememberedEmail() {
        return sharedPreferences.getString(KEY_REMEMBERED_EMAIL, "");
    }

    public void clearRememberedEmail() {
        editor.remove(KEY_REMEMBERED_EMAIL);
        editor.apply();
    }

    public void saveLoginSession(String email, boolean isAdmin) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putBoolean(KEY_IS_ADMIN, isAdmin);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    public boolean isAdmin() {
        return sharedPreferences.getBoolean(KEY_IS_ADMIN, false);
    }

    public void clearLoginSession() {
        editor.remove(KEY_IS_LOGGED_IN);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_IS_ADMIN);
        editor.apply();
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}
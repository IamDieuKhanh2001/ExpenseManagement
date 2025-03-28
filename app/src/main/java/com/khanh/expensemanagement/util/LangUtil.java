package com.khanh.expensemanagement.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LangUtil {

    private static final String EXPENSE_MANAGEMENT_SHARED_PREFS_NAME = "AppPrefs";
    private static final String SHARED_PREFS_LANG = "language";

    // get current language
    public static String getSavedLanguage(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(EXPENSE_MANAGEMENT_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(SHARED_PREFS_LANG, "en"); // default app language
    }

    // save language
    public static Boolean saveLanguage(Activity activity, String langCode) {

        try {

            // save lang code in SharedPreferences
            SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(EXPENSE_MANAGEMENT_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(SHARED_PREFS_LANG, langCode);
            editor.apply();

            // update app locale
            Locale locale = new Locale(langCode);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            activity.getResources().updateConfiguration(config, activity.getResources().getDisplayMetrics());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

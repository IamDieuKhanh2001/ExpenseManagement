package com.khanh.expensemanagement.util;

import androidx.appcompat.app.AppCompatActivity;

import com.khanh.expensemanagement.R;

public class ActivityUtil {

    public static void enableActionBar(AppCompatActivity appCompatActivity, String activityTitle) {

        enableActionBar(appCompatActivity, activityTitle, true);
    }

    public static void enableActionBar(AppCompatActivity appCompatActivity, String activityTitle, Boolean enableBackButton) {

        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setTitle(activityTitle);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(enableBackButton);
            appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }
    }
}

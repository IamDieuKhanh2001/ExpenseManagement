package com.khanh.expensemanagement;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import com.khanh.expensemanagement.exception.CustomExceptionHandler;

public class MainActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.frame_layout));

        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
    }
}
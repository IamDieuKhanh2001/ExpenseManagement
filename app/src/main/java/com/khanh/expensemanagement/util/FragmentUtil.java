package com.khanh.expensemanagement.util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class FragmentUtil {

    public static void setActionBarTitle(FragmentActivity fragmentActivity, String title) {

        AppCompatActivity activity = (AppCompatActivity) fragmentActivity;
        // Lấy ActionBar và thay đổi tiêu đề
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }

    }
}

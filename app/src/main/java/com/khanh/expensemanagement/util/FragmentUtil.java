package com.khanh.expensemanagement.util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.khanh.expensemanagement.R;

public class FragmentUtil {

    public static void setActionBarTitle(FragmentActivity fragmentActivity, String title) {

        AppCompatActivity activity = (AppCompatActivity) fragmentActivity;
        // Lấy ActionBar và thay đổi tiêu đề
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }

    public static void replaceFragment(FragmentManager fragmentManager,Fragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

package com.khanh.expensemanagement.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class FormUtil {

    public static String fncNS(String prmValue) {
        if (prmValue == null || prmValue.trim().isEmpty()) {
            return " ";
        }
        return prmValue;
    }

    public static void openConfirmDialog(Context context, String title, String message, Runnable onConfirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", (dialog, which) -> onConfirm.run());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}

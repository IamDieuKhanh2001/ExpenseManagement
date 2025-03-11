package com.khanh.expensemanagement.util;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

import com.khanh.expensemanagement.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormUtil {

    public static String fncNS(String prmValue) {
        if (prmValue == null || prmValue.trim().isEmpty()) {
            return " ";
        }
        return prmValue;
    }

    public static String fncDecFormat(String number) {

        if (fncIsNumeric(number)) {
            try {

                Number num = Double.parseDouble(number);
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator('.');
                DecimalFormat formatter = new DecimalFormat("#,###", symbols);

                return formatter.format(num);
            } catch (NumberFormatException e) {

                return number;
            }
        }
        return number;
    }

    public static boolean fncIsNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }

    public static String currencyFormat(Context context,Object number) {

        String result;

        try {

            result = number.toString();
            result = fncDecFormat(result);
            result = context.getString(R.string.transaction_amount_currency, result);
            return result;
        } catch (Exception e) {

            result = "";
            return result;
        }
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

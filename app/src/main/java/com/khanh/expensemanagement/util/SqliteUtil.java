package com.khanh.expensemanagement.util;

import android.database.Cursor;

public class SqliteUtil {

    public static void releaseCursor(Cursor cursor) {

        if (cursor != null) {

            cursor.close();
        }
    }
}

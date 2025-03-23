package com.khanh.expensemanagement.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.khanh.expensemanagement.ApplicationProperties;
import com.khanh.expensemanagement.exception.CustomExceptionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SqliteUtil {

    // Release cursor
    public static void releaseCursor(Cursor cursor) {

        if (cursor != null) {

            cursor.close();
        }
    }

    // Execute file.sql from database migration path
    public static void executeSQLScript(SQLiteDatabase db, Context context, String fileName) {

        try {

            InputStream inputStream = context.getAssets().open(ApplicationProperties.DATABASE_MIGRATION_DIR + "/" + fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder statement = new StringBuilder();
            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) {

                    continue; // skip empty line
                }
                statement.append(line);
                if (line.endsWith(";")) {

                    db.execSQL(statement.toString());
                    statement.setLength(0); // Reset
                }
            }
            reader.close();
            inputStream.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}

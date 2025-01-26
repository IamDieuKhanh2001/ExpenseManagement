package com.khanh.expensemanagement.util.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.YearMonth;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "ExpenseManagement.db";
    private static final int DATABASE_VERSION = 1;
    private String TABLE_CATEGORIES = "categories";
    private String TABLE_TRANSACTION = "transactions";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String queryCreateCategories = "CREATE TABLE " + TABLE_CATEGORIES +
                " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
                "name_ss" + " TEXT, " +
                "image" + " TEXT " +
                ");";

        String queryCreateTransaction = "CREATE TABLE " + TABLE_TRANSACTION +
                " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
                "amount" + " INTEGER DEFAULT 0, " +
                "note" + " TEXT, " +
                "category_id" + " INTEGER, " +
                "transaction_dt" + " TEXT, " +
                "source_fund_id" + " INTEGER, " +
                "ins_dttm" + " TEXT, " +
                "upd_dttm" + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(queryCreateCategories);
        sqLiteDatabase.execSQL(queryCreateTransaction);

        seedCategoriesData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        onCreate(sqLiteDatabase);
    }

    private void seedCategoriesData(SQLiteDatabase db) {

        String[] seederQueries = new String[]{

                "INSERT INTO categories (id, name_ss, image) VALUES (1, 'Food', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (2, 'Transport', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (3, 'Beauty', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (4, 'Health', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (5, 'Charity', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (6, 'Shopping', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (7, 'Entertainment', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (8, 'Study', NULL);",

                "INSERT INTO transactions (\"amount\",\"note\",\"category_id\",\"transaction_dt\",\"source_fund_id\",\"ins_dttm\",\"upd_dttm\") VALUES (1000,'text 1 note',1,'2025-12-20',NULL,'2025-01-26 19:42:00','2025-01-26 19:42:00');",
                "INSERT INTO transactions (\"amount\",\"note\",\"category_id\",\"transaction_dt\",\"source_fund_id\",\"ins_dttm\",\"upd_dttm\") VALUES (2000,'text 1 note',1,'2025-01-01',NULL,'2025-01-26 19:42:00','2025-01-26 19:42:00');",
                "INSERT INTO transactions (\"amount\",\"note\",\"category_id\",\"transaction_dt\",\"source_fund_id\",\"ins_dttm\",\"upd_dttm\") VALUES (2000,'text 1 note',1,'2025-01-20',NULL,'2025-01-26 19:42:00','2025-01-26 19:42:00');",
                "INSERT INTO transactions (\"amount\",\"note\",\"category_id\",\"transaction_dt\",\"source_fund_id\",\"ins_dttm\",\"upd_dttm\") VALUES (3000,'text 1 note',1,'2025-01-20',NULL,'2025-01-26 19:42:00','2025-01-26 19:42:00');",
        };

        for (String query : seederQueries) {
            db.execSQL(query);
        }
    }

    public Cursor categoryFindAll(){
        String query = "SELECT " + "*"
                + " FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor transactionFindAll(){
        String query = "SELECT " + "*"
                + " FROM " + TABLE_TRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor transactionFindByDate(LocalDate date){
        String query = "SELECT " + "id, amount, note, category_id, transaction_dt, source_fund_id, ins_dttm, upd_dttm" +
                " FROM " + TABLE_TRANSACTION +
                " WHERE transaction_dt LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{String.valueOf(date)});
        }
        return cursor;
    }
}

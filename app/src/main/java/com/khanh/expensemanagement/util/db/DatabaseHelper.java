package com.khanh.expensemanagement.util.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.khanh.expensemanagement.util.DateTimeUtil;

import java.time.LocalDate;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "ExpenseManagement.db";
    private static final int DATABASE_VERSION = 1;
    private String TABLE_CATEGORIES = "categories";
    private String TABLE_TRANSACTION = "transactions";
    private String TABLE_SOURCES = "sources";

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
                "source_id" + " INTEGER, " +
                "ins_dttm" + " TEXT, " +
                "upd_dttm" + " TEXT " +
                ");";

        String queryCreateSources = "CREATE TABLE " + TABLE_SOURCES +
                " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
                "name_ss" + " TEXT, " +
                "image" + " TEXT, " +
                "ins_dttm" + " TEXT, " +
                "upd_dttm" + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(queryCreateCategories);
        sqLiteDatabase.execSQL(queryCreateTransaction);
        sqLiteDatabase.execSQL(queryCreateSources);

        createSeedData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        onCreate(sqLiteDatabase);
    }

    private void createSeedData(SQLiteDatabase db) {

        String[] seederQueries = new String[]{

                "INSERT INTO categories (id, name_ss, image) VALUES (1, '食べ物', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (2, '輸送', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (3, '美', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (4, '健康', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (5, 'チャリティー', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (6, '買い物', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (7, 'エンターテインメント', NULL);",
                "INSERT INTO categories (id, name_ss, image) VALUES (8, '勉強', NULL);",

                "INSERT INTO transactions (\"amount\",\"note\",\"category_id\",\"transaction_dt\",\"source_id\",\"ins_dttm\",\"upd_dttm\") VALUES (1000,'text 1 note',1,'2025-12-20',NULL,'2025-01-26 19:42:00','2025-01-26 19:42:00');",
                "INSERT INTO transactions (\"amount\",\"note\",\"category_id\",\"transaction_dt\",\"source_id\",\"ins_dttm\",\"upd_dttm\") VALUES (2000,'text 1 note',1,'2025-01-01',NULL,'2025-01-26 19:42:00','2025-01-26 19:42:00');",
                "INSERT INTO transactions (\"amount\",\"note\",\"category_id\",\"transaction_dt\",\"source_id\",\"ins_dttm\",\"upd_dttm\") VALUES (2000,'text 1 note',1,'2025-01-20',NULL,'2025-01-26 19:42:00','2025-01-26 19:42:00');",
                "INSERT INTO transactions (\"amount\",\"note\",\"category_id\",\"transaction_dt\",\"source_id\",\"ins_dttm\",\"upd_dttm\") VALUES (3000,'text 1 note',1,'2025-01-20',NULL,'2025-01-26 19:42:00','2025-01-26 19:42:00');",

                "INSERT INTO sources (\"id\",\"name_ss\",\"image\") VALUES (1,'現金','ic_source_cash');",
                "INSERT INTO sources (\"id\",\"name_ss\",\"image\") VALUES (2,'振込','ic_source_card');",
                "INSERT INTO sources (\"id\",\"name_ss\",\"image\") VALUES (3,'スイカ','ic_source_suica');",
                "INSERT INTO sources (\"id\",\"name_ss\",\"image\") VALUES (4,'その他',NULL);",
        };

        for (String query : seederQueries) {
            db.execSQL(query);
        }
    }

    public Cursor sourceFindAll() {
        String query = "SELECT " + "id, name_ss, image, ins_dttm, upd_dttm"
                + " FROM " + TABLE_SOURCES;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor categoryFindAll() {
        String query = "SELECT " + "*"
                + " FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor transactionFindAll() {
        String query = "SELECT " + "*"
                + " FROM " + TABLE_TRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor transactionFindByDate(LocalDate date) {
        String query = "SELECT " + "id, amount, note, category_id, transaction_dt, source_id, ins_dttm, upd_dttm" +
                " FROM " + TABLE_TRANSACTION +
                " WHERE transaction_dt LIKE ?" +
                " ORDER BY ins_dttm DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(date)});
        }
        return cursor;
    }

    public void registerTransaction(Integer amount, String note, Integer category_id, String transaction_dt, Integer source_id) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("amount", amount);
            cv.put("note", note.trim());
            cv.put("category_id", category_id);
            cv.put("transaction_dt", transaction_dt);
            cv.put("source_id", source_id);
            cv.put("ins_dttm", DateTimeUtil.getCurrentDateTime());
            cv.put("upd_dttm", DateTimeUtil.getCurrentDateTime());

            long result = db.insert(TABLE_TRANSACTION,null, cv);
            if(result == -1){
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("registerTransaction", "registerTransaction: " + "error");
        }
    }

}

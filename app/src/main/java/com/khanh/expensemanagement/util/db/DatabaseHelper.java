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
    private static final int DATABASE_VERSION = 2;
    private final String TABLE_TRANSACTION = "transactions";
    private final String TABLE_M_NAME = "m_name";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

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

        String queryCreateMName = "CREATE TABLE " + TABLE_M_NAME +
                " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
                "name_ident_cd" + " TEXT NOT NULL, " +
                "name_cd" + " TEXT NOT NULL, " +
                "name_ident_name" + " TEXT NOT NULL, " +
                "name_ident_note" + " TEXT NOT NULL, " +
                "name_display_seq" + " INTEGER NOT NULL, " +
                "name_ss" + " TEXT NOT NULL, " +
                "name_rk" + " TEXT NOT NULL, " +
                "drawable_icon_url" + " TEXT, " +
                "ins_dttm" + " TEXT, " +
                "upd_dttm" + " TEXT, " +
                "CONSTRAINT m_name_index1 UNIQUE (name_ident_cd, name_cd)" +
                ");";

        sqLiteDatabase.execSQL(queryCreateTransaction);
        sqLiteDatabase.execSQL(queryCreateMName);

        createSeedData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_M_NAME);
        onCreate(sqLiteDatabase);
    }

    private void createSeedData(SQLiteDatabase db) {

        String[] seederQueries = new String[]{

                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (1,'sourcePaymentKbn','1','支払い方','',1,'現金','現金','ic_source_cash','2025-02-01','2025-02-01');",
                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (2,'sourcePaymentKbn','2','支払い方','',2,'振込','振込','ic_source_card','2025-02-01','2025-02-01');",
                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (3,'sourcePaymentKbn','3','支払い方','',3,'スイカ','スイカ','ic_source_suica','2025-02-01','2025-02-01');",
                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (4,'sourcePaymentKbn','4','支払い方','',4,'その他','他',NULL,'2025-02-01','2025-02-01');",

                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (5,'categoryKbn','1','カテゴリー','',1,'食べ物','食べ物','ic_category_food','2025-02-01','2025-02-01');",
                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (6,'categoryKbn','2','カテゴリー','',2,'輸送','輸送','ic_category_transport','2025-02-01','2025-02-01');",
                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (7,'categoryKbn','3','カテゴリー','',3,'美','美',NULL,'2025-02-01','2025-02-01');",
                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (8,'categoryKbn','4','カテゴリー','',4,'勉強','勉強',NULL,'2025-02-01','2025-02-01');",
                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (9,'categoryKbn','5','カテゴリー','',5,'買い物','買い物',NULL,'2025-02-01','2025-02-01');",
                "INSERT INTO " + TABLE_M_NAME + " (\"id\",\"name_ident_cd\",\"name_cd\",\"name_ident_name\",\"name_ident_note\",\"name_display_seq\",\"name_ss\",\"name_rk\",\"drawable_icon_url\",\"ins_dttm\",\"upd_dttm\") VALUES (10,'categoryKbn','6','カテゴリー','',6,'エンターテインメント','エンターテインメント',NULL,'2025-02-01','2025-02-01');",
        };

        for (String query : seederQueries) {
            db.execSQL(query);
        }
    }

    public Cursor mNameFindAll(String nameIdentCd) {
        String query = "SELECT " + "name_ident_cd, name_cd, name_ident_name, name_ss, drawable_icon_url" +
                " FROM " + TABLE_M_NAME +
                " WHERE m_name.name_ident_cd = ?" +
                " ORDER BY m_name.name_display_seq ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(nameIdentCd)});
        }
        return cursor;
    }

    public Cursor mNameSelectByUk1(String nameIdentCd, String nameCd) {

        String query = "SELECT " + "name_ident_cd, name_cd, name_ident_name, name_ss, drawable_icon_url" +
                " FROM " + TABLE_M_NAME +
                " WHERE m_name.name_ident_cd = ?" +
                " AND m_name.name_cd = ? ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(nameIdentCd), String.valueOf(nameCd)});
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

    public Cursor transactionFindById(Integer id) {
        String query = "SELECT " + "id, amount, note, category_id, transaction_dt, source_id, ins_dttm, upd_dttm" +
                " FROM " + TABLE_TRANSACTION +
                " WHERE id = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{id.toString()});
        }
        return cursor;
    }

    public void registerTransaction(Integer amount, String note, Integer category_id, String transaction_dt, Integer source_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("amount", amount);
        cv.put("note", note.trim());
        cv.put("category_id", category_id);
        cv.put("transaction_dt", transaction_dt);
        cv.put("source_id", source_id);
        cv.put("ins_dttm", DateTimeUtil.getCurrentDateTime());
        cv.put("upd_dttm", DateTimeUtil.getCurrentDateTime());

        long result = db.insert(TABLE_TRANSACTION, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteTransactionById(Integer id) {

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_TRANSACTION, "id=?", new String[]{id.toString()});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

}

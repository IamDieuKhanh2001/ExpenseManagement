package com.khanh.expensemanagement.domain.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.khanh.expensemanagement.ApplicationProperties;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.util.DateTimeUtil;
import com.khanh.expensemanagement.util.LangUtil;
import com.khanh.expensemanagement.util.SqliteUtil;
import com.khanh.expensemanagement.util.db.SqlParamsUtil;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.YearMonth;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private final String TABLE_TRANSACTION = "transactions";
    private final String TABLE_M_NAME = "m_name";
    private final String TABLE_BUDGET = "budgets";

    public DatabaseHelper(@Nullable Context context) {
        super(context, ApplicationProperties.DATABASE_NAME, null, ApplicationProperties.DATABASE_VERSION);
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
                "name_ss_ja" + " TEXT NOT NULL, " +
                "name_ss_en" + " TEXT NOT NULL, " +
                "name_ss_vi" + " TEXT NOT NULL, " +
                "name_rk" + " TEXT NOT NULL, " +
                "drawable_icon_url" + " TEXT, " +
                "ins_dttm" + " TEXT, " +
                "upd_dttm" + " TEXT, " +
                "CONSTRAINT m_name_index1 UNIQUE (name_ident_cd, name_cd)" +
                ");";

        String queryCreateBudget = "CREATE TABLE " + TABLE_BUDGET +
                " (" +
                "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
                "limit_amount" + " INTEGER NOT NULL DEFAULT 1000, " +
                "category_id" + " INTEGER UNIQUE, " +
                "ins_dttm" + " TEXT, " +
                "upd_dttm" + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(queryCreateTransaction);
        sqLiteDatabase.execSQL(queryCreateMName);
        sqLiteDatabase.execSQL(queryCreateBudget);

        SqliteUtil.executeSQLScript(sqLiteDatabase, context, "m_name.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_M_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        onCreate(sqLiteDatabase);
    }

    public Cursor mNameFindAll(String nameIdentCd) {
        String query = "SELECT " + "name_ident_cd, name_cd, name_ident_name, name_ss_" + LangUtil.getSavedLanguage(context) + ", drawable_icon_url" +
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

        String query = "SELECT " + "name_ident_cd, name_cd, name_ident_name, name_ss_" + LangUtil.getSavedLanguage(context) + ", drawable_icon_url" +
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

    public Cursor budgetTotalSpendingMonth() {

        // -99 is total spending month (-99 not in m name)
        return budgetFindByCategoryId(-99);
    }

    public Cursor budgetFindByCategoryId(Integer categoryId) {

        String query = "SELECT " + "limit_amount, category_id, ins_dttm, upd_dttm" +
                " FROM " + TABLE_BUDGET +
                " WHERE category_id IS ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});
        }
        return cursor;
    }

    public Cursor budgetCategoryFindAll() {

        String query = "SELECT budgets.id, limit_amount, category_id, budgets.ins_dttm, budgets.upd_dttm, m_name.name_ss_" + LangUtil.getSavedLanguage(context) + ", m_name.drawable_icon_url" +
                " FROM budgets" +
                " LEFT JOIN m_name" +
                " ON budgets.category_id = m_name.name_cd" +
                " AND m_name.name_ident_cd = '" + CategoryClass.NAME_IDENT_CD + "'" +
                " WHERE category_id IS NOT -99" +
                " ORDER BY m_name.name_display_seq ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void registerBudget(Integer limitAmount, Integer categoryId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("limit_amount", limitAmount);
        cv.put("category_id", categoryId);
        cv.put("ins_dttm", DateTimeUtil.getCurrentDateTime());
        cv.put("upd_dttm", DateTimeUtil.getCurrentDateTime());

        long result = db.insert(TABLE_BUDGET, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateBudget(Integer limitAmount, Integer categoryId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("limit_amount", limitAmount);
        cv.put("ins_dttm", DateTimeUtil.getCurrentDateTime());
        cv.put("upd_dttm", DateTimeUtil.getCurrentDateTime());

        long result = db.update(TABLE_BUDGET, cv, "category_id=?", new String[]{categoryId.toString()});

        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Edit Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteBudgetTotalSpendingMonth() {

        deleteBudgetByCategoryId(-99);
    }

    public void deleteBudgetByCategoryId(Integer categoryId) {

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_BUDGET, "category_id=?", new String[]{categoryId.toString()});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
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

    public Cursor transactionExcelData() {

        String query = "SELECT " + "transactions.id, amount, note, category_id, m_name_category.name_ss_en, transaction_dt, source_id, m_name_source.name_ss_en, transactions.ins_dttm, transactions.upd_dttm" +
                " FROM " + TABLE_TRANSACTION +
                " LEFT JOIN m_name AS m_name_category" +
                " on m_name_category.name_ident_cd = 'categoryKbn' AND m_name_category.name_cd = transactions.category_id" +
                " LEFT JOIN m_name AS m_name_source" +
                " on m_name_source.name_ident_cd = 'sourcePaymentKbn' AND m_name_source.name_cd = transactions.source_id" +
                " ORDER BY transactions.ins_dttm DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {

            cursor = db.rawQuery(query, new String[]{});
        }
        return cursor;
    }

    public Cursor transactionExcelData(YearMonth yearMonth) {

        String query = "SELECT " + "transactions.id, amount, note, category_id, m_name_category.name_ss_en, transaction_dt, source_id, m_name_source.name_ss_en, transactions.ins_dttm, transactions.upd_dttm" +
                " FROM " + TABLE_TRANSACTION +
                " LEFT JOIN m_name AS m_name_category" +
                " on m_name_category.name_ident_cd = 'categoryKbn' AND m_name_category.name_cd = transactions.category_id" +
                " LEFT JOIN m_name AS m_name_source" +
                " on m_name_source.name_ident_cd = 'sourcePaymentKbn' AND m_name_source.name_cd = transactions.source_id" +
                " WHERE transaction_dt LIKE ?" +
                " ORDER BY ins_dttm DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {

            cursor = db.rawQuery(query, new String[]{SqlParamsUtil.forward(yearMonth.toString())});
        }
        return cursor;
    }

    public BigInteger transactionTotalSpentOnMonth(YearMonth yearMonth) {

        // Null category id is get total spent in month
        return transactionTotalSpentOnMonth(yearMonth, null);
    }

    public BigInteger transactionTotalSpentOnMonth(YearMonth yearMonth, Integer categoryId) {

        BigInteger totalSpent = BigInteger.valueOf(0);
        String query = "SELECT " + "COALESCE(SUM(amount), 0) AS total_spent" +
                " FROM " + TABLE_TRANSACTION +
                " WHERE transaction_dt LIKE ?";
        if (categoryId != null) {

            // Get total spent in specific category
            query += " AND category_id IS " + categoryId;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {

            cursor = db.rawQuery(query, new String[]{SqlParamsUtil.forward(yearMonth.toString())});
        }

        if (cursor != null && cursor.moveToFirst()) {

            totalSpent = BigInteger.valueOf(cursor.getInt(0));
        }
        return totalSpent;
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

    public void updateTransaction(Integer id, Integer amount, String note, Integer category_id, String transaction_dt, Integer source_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("amount", amount);
        cv.put("note", note.trim());
        cv.put("category_id", category_id);
        cv.put("transaction_dt", transaction_dt);
        cv.put("source_id", source_id);
        cv.put("upd_dttm", DateTimeUtil.getCurrentDateTime());

        long result = db.update(TABLE_TRANSACTION, cv, "id=?", new String[]{id.toString()});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
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

package com.kasi.cashmate.tables;

import android.content.ContentValues;

import com.kasi.cashmate.common.CommonFun;

/**
 * @author kasi
 */
public class SavedTransaction {

    final String table_name = "SAVED_TRANSACTION";
    final String[] columns = new String[] { "_id", "transaction_name", "transaction_dt", "teller_amount", "cash_total", "created_at", "has_deleted" };
    final String[] types = new String[] { " INTEGER PRIMARY KEY", " TEXT", " TEXT", " NUMERIC", " NUMERIC", " TEXT", " INTEGER" };

    public SavedTransaction() {
    }

    public String tableName() {
        return table_name;
    }

    public String[] columnColumns() {
        return columns;
    }

    public String[] columnTypes() {
        return types;
    }

    public ContentValues getContentValues(String transction_name, String transaction_dt, float teller_amt, float cash_total, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[1], transction_name);
        contentValue.put(columns[2], transaction_dt);
        contentValue.put(columns[3], teller_amt);
        contentValue.put(columns[4], cash_total);
        contentValue.put(columns[5], CommonFun.getCurrentDateTime());
        contentValue.put(columns[6], has_deleted);
        return contentValue;
    }

    public ContentValues getContentValues(long id, String transction_name, String transaction_dt, float teller_amt, float cash_total, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[0], id);
        contentValue.put(columns[1], transction_name);
        contentValue.put(columns[2], transaction_dt);
        contentValue.put(columns[3], teller_amt);
        contentValue.put(columns[4], cash_total);
        contentValue.put(columns[5], CommonFun.getCurrentDateTime());
        contentValue.put(columns[6], has_deleted);
        return contentValue;
    }

    public ContentValues getContentValues(String transction_name, String transaction_dt, float teller_amt, float cash_total, String createdAt, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[1], transction_name);
        contentValue.put(columns[2], transaction_dt);
        contentValue.put(columns[3], teller_amt);
        contentValue.put(columns[4], cash_total);
        contentValue.put(columns[5], createdAt);
        contentValue.put(columns[6], has_deleted);
        return contentValue;
    }

    public ContentValues getContentValues(long id, String transction_name, String transaction_dt, float teller_amt, float cash_total, String createdAt, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[0], id);
        contentValue.put(columns[1], transction_name);
        contentValue.put(columns[2], transaction_dt);
        contentValue.put(columns[3], teller_amt);
        contentValue.put(columns[4], cash_total);
        contentValue.put(columns[5], createdAt);
        contentValue.put(columns[6], has_deleted);
        return contentValue;
    }
}

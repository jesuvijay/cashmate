package com.kasi.cashmate.tables;

import android.content.ContentValues;

import com.kasi.cashmate.common.CommonFun;

/**
 * @author kasi
 */
public class Transactions {

    final String table_name = "TRANSACTIONS";
    final String[] columns = new String[] { "_id", "transaction_id", "description", "denomination", "pieces", "bags", "loose", "type", "created_at", "has_deleted" };
    final String[] types = new String[] { " INTEGER PRIMARY KEY", " INTEGER", " TEXT", " INTEGER", " INTEGER"," INTEGER", " NUMERIC", " TEXT", " TEXT", " INTEGER" };

    public Transactions() {
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

    public ContentValues getContentValues(int transaction_id, String description, int denomination, int pieces, int bags, float loose, String type, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[1], transaction_id);
        contentValue.put(columns[2], description);
        contentValue.put(columns[3], denomination);
        contentValue.put(columns[4], pieces);
        contentValue.put(columns[5], bags);
        contentValue.put(columns[6], loose);
        contentValue.put(columns[7], type);
        contentValue.put(columns[8], CommonFun.getCurrentDateTime());
        contentValue.put(columns[9], has_deleted);
        return contentValue;
    }

    public ContentValues getContentValues(long id, int transaction_id, String description, int denomination, int pieces, int bags, float loose, String type, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[0], id);
        contentValue.put(columns[1], transaction_id);
        contentValue.put(columns[2], description);
        contentValue.put(columns[3], denomination);
        contentValue.put(columns[4], pieces);
        contentValue.put(columns[5], bags);
        contentValue.put(columns[6], loose);
        contentValue.put(columns[7], type);
        contentValue.put(columns[8], CommonFun.getCurrentDateTime());
        contentValue.put(columns[9], has_deleted);
        return contentValue;
    }
}

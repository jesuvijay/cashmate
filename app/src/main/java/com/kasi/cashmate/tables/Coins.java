package com.kasi.cashmate.tables;

import android.content.ContentValues;

/**
 * @author kasi
 */
public class Coins {

    final String table_name = "COINS";
    final String[] columns = new String[] { "_id", "denomination", "pieces", "has_deleted" };
    final String[] types = new String[] { " INTEGER PRIMARY KEY", " INTEGER", " INTEGER", " INTEGER" };

    public Coins() {
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

    public ContentValues getContentValues(int denomination, int pieces, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[1], denomination);
        contentValue.put(columns[2], pieces);
        contentValue.put(columns[3], has_deleted);
        return contentValue;
    }

    public ContentValues getContentValues(int id, int denomination, int pieces, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[0], id);
        contentValue.put(columns[1], denomination);
        contentValue.put(columns[2], pieces);
        contentValue.put(columns[3], has_deleted);
        return contentValue;
    }
}

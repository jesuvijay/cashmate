package com.kasi.cashmate.tables;

import android.content.ContentValues;

/**
 * @author kasi
 */
public class Others {

    final String table_name = "OTHERS";
    final String[] columns = new String[] { "_id", "denomination", "has_deleted" };
    final String[] types = new String[] { " INTEGER PRIMARY KEY", " TEXT", " INTEGER" };

    public Others() {
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

    public ContentValues getContentValues(String description, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[1], description);
        contentValue.put(columns[2], has_deleted);
        return contentValue;
    }

    public ContentValues getContentValues(int id, String description, int has_deleted) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(columns[0], id);
        contentValue.put(columns[1], description);
        contentValue.put(columns[2], has_deleted);
        return contentValue;
    }
}

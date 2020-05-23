package com.kasi.cashmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author kasi
 */
public class DBManager {

    private DatabaseHelper dbHelper;
    public String TABLE_NAME = "";

    public final String ID = "_id";
    public String[] COLUMNS = {};
    public String[] COLUMN_TYPES = {};

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c, String tableName, String [] columns, String [] types) {
        context = c;
        TABLE_NAME = tableName;
        COLUMNS = columns;
        COLUMN_TYPES = types;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context, TABLE_NAME, COLUMNS, COLUMN_TYPES);
        database = dbHelper.getWritableDatabase();
        dbHelper.onCreate(database);
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(ContentValues contentValues) {
        long id = database.insert(TABLE_NAME, null, contentValues);
        return id;
    }

    public void insertDatas(ContentValues[] contentValue) {
        for (int pos=1; pos<contentValue.length; pos++) {
            database.insert(TABLE_NAME, null, contentValue[pos]);
        }
    }

    public Cursor fetch() {
        Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) throws SQLException {
        Cursor cursor = database.query(TABLE_NAME, COLUMNS, whereClause, whereArgs, groupBy, having, orderBy);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit) throws SQLException {
        Cursor cursor = database.query(TABLE_NAME, COLUMNS, whereClause, whereArgs, groupBy, having, orderBy, limit);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(int _id, ContentValues contentValues) {
        int i = database.update(TABLE_NAME, contentValues, ID + " = " + _id, null);
        return i;
    }

    public void delete(int _id) {
        database.delete(TABLE_NAME, ID + "=" + _id, null);
    }


}

package com.kasi.cashmate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author kasi
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name

    public String TABLE_NAME = "";

    // Table Columns

    public final String ID = "_id";
    public String[] COLUMNS = {};
    public String[] COLUMN_TYPES = {};

    // Database Information

    static final String DB_NAME = "CASHMATE.DB";

    // database version

    static final int DB_VERSION = 1;

    // Creating table query

    String CREATE_TABLE = "";


    public DatabaseHelper(Context c, String tableName, String [] columns, String [] types) {
        super(c, DB_NAME, null, DB_VERSION);
        TABLE_NAME = tableName;
        COLUMNS = columns;
        COLUMN_TYPES = types;

        if (COLUMNS.length == COLUMN_TYPES.length && TABLE_NAME.length() > 0 && COLUMNS.length > 0) {
            String _columns = "";
            for (int pos=0; pos < COLUMNS.length; pos++) {
                if (_columns.length() == 0) {
                    _columns = (COLUMNS[pos].toLowerCase() + " " + COLUMN_TYPES[pos] + " ");
                } else {
                    _columns += (", " + COLUMNS[pos].toLowerCase() + " " + COLUMN_TYPES[pos] + " ");
                }
            }
            CREATE_TABLE += ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + _columns + " );");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

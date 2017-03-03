package ru.drsk.progserega.defectlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StationDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "station.db";


    public StationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        String sql;
        // Таблица подстанций:
        sql = "CREATE TABLE station_tbl (" +
                "id integer PRIMARY KEY NOT NULL," +
                "name VARCHAR(255)," +
                "uniq_id integer," +
                "sp_id integer," +
                "res_id integer," +
                "np_id integer);";
        db.execSQL(sql);
        // Таблица СП:
        sql = "CREATE TABLE sp_tbl ("+
        "id integer PRIMARY KEY NOT NULL, "+
        "name VARCHAR(255) "+
        ");";
        db.execSQL(sql);
        // Таблица РЭС-ов:
        sql = "CREATE TABLE res_tbl ("+
        "id integer PRIMARY KEY NOT NULL, "+
        "name VARCHAR(255), "+
        "sp_id integer"+
        ");";
        db.execSQL(sql);
        // Населённые пункты
        sql = "CREATE TABLE np_tbl ("+
        "id integer PRIMARY KEY NOT NULL, "+
        "name VARCHAR(255) "+
        ");";
        db.execSQL(sql);
        // Таблица дефектов подстанций:
        sql = "CREATE TABLE station_defect_tbl (" +
                "id integer PRIMARY KEY NOT NULL," +
                "station_id integer," +
                "uniq_id integer," +
                "comment VARCHAR(512));";
        db.execSQL(sql);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        String sql;
        sql="drop table station_tbl;";
        db.execSQL(sql);
        sql="drop table station_defect_tbl;";
        db.execSQL(sql);
        sql="drop table sp_tbl;";
        db.execSQL(sql);
        sql="drop table res_tbl;";
        db.execSQL(sql);
        sql="drop table np_tbl;";
        db.execSQL(sql);

        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

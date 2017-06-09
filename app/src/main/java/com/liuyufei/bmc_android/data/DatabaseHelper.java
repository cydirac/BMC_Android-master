package com.liuyufei.bmc_android.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.liuyufei.bmc_android.R;

import static com.liuyufei.bmc_android.data.BMCContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BMC.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STAFF_CREATE =
            "CREATE TABLE " + StaffEntry.TABLE_NAME + " (" +
                    StaffEntry._ID + " INTEGER PRIMARY KEY, " +
                    StaffEntry.COLUMN_NAME + " TEXT, " +
                    StaffEntry.COLUMN_TITLE + " TEXT, " +
                    StaffEntry.COLUMN_DEPARTMENT + " TEXT, " +
                    StaffEntry.COLUMN_PHOTO + " TEXT, " +
                    StaffEntry.COLUMN_MOBILE + " TEXT" +
                    ")";

    private static final String TABLE_VISITOR_CREATE =
            "CREATE TABLE " + VisitorEntry.TABLE_NAME + " (" +
                    VisitorEntry._ID + " INTEGER PRIMARY KEY, " +
                    VisitorEntry.COLUMN_NAME + " TEXT, " +
                    VisitorEntry.COLUMN_BUSINESS_NAME + " TEXT, " +
                    VisitorEntry.COLUMN_MOBILE + " TEXT " +
                    ")";


    private static final String TABLE_APPOINTMENT_CREATE =
            "CREATE TABLE " + AppointmentEntry.TABLE_NAME + " (" +
                    AppointmentEntry._ID + " INTEGER PRIMARY KEY, " +
                    AppointmentEntry.COLUMN_DESCRIPTION + " TEXT, " +
                    AppointmentEntry.COLUMN_DATETIME + " TEXT default CURRENT_TIMESTAMP, " +
                    AppointmentEntry.COLUMN_STAFF + " INTEGER NOT NULL, " +
                    AppointmentEntry.COLUMN_VISITOR + " INTEGER NOT NULL, " +
                    " FOREIGN KEY(" + AppointmentEntry.COLUMN_VISITOR + ") REFERENCES " + VisitorEntry.TABLE_NAME + "(" + VisitorEntry._ID + "), " +
                    " FOREIGN KEY(" + AppointmentEntry.COLUMN_STAFF + ") REFERENCES " + StaffEntry.TABLE_NAME + "(" + StaffEntry._ID + ") " +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_STAFF_CREATE);
        db.execSQL(TABLE_VISITOR_CREATE);
        db.execSQL(TABLE_APPOINTMENT_CREATE);
        initData(db);
    }


    private void initData(SQLiteDatabase db) {
        Log.i("DatabaseHelper", "init data...");
        ContentValues values = new ContentValues();
        for(int i=0;i<10;i++){
            values.put(StaffEntry.COLUMN_DEPARTMENT, "USA");
            values.put(StaffEntry.COLUMN_NAME, "Trump"+i);
            values.put(StaffEntry.COLUMN_MOBILE, "123456");
            values.put(StaffEntry.COLUMN_TITLE, "President");
            values.put(StaffEntry.COLUMN_PHOTO, R.drawable.staff);

            db.insert(StaffEntry.TABLE_NAME, null, values);
            values.clear();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StaffEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VisitorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AppointmentEntry.TABLE_NAME);
        onCreate(db);
    }
}

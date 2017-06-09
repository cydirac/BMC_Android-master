package com.liuyufei.bmc_android.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.liuyufei.bmc_android.data.BMCContract.*;

/**
 * Created by liuyufei on 27/05/17.
 */

public class BMCProvider extends ContentProvider {

    //constants for the operation
    private static final int STAFF = 1;
    private static final int STAFF_ID = 2;
    private static final int VISITOR = 3;
    private static final int VISITOR_ID = 4;
    private static final int APPOINTMENT = 5;
    private static final int APPOINTMENT_ID = 6;
    //urimatcher
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_BMC_STAFF, STAFF);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_BMC_STAFF + "/#", STAFF_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_BMC_VISITOR, VISITOR);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_BMC_VISITOR + "/#", VISITOR_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_BMC_APPOINTMENT, APPOINTMENT);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_BMC_APPOINTMENT + "/#", APPOINTMENT_ID);
    }

    private DatabaseHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        //get db
        SQLiteDatabase db = helper.getReadableDatabase();
        //cursor
        Cursor cursor;
        //our integer
        int match = uriMatcher.match(uri);
        //intables
        String inTables = AppointmentEntry.TABLE_NAME
                + " inner join "
                + StaffEntry.TABLE_NAME
                + " on " + AppointmentEntry.COLUMN_STAFF + " = "
                + StaffEntry.TABLE_NAME + "." + StaffEntry._ID
                + " inner join "
                + VisitorEntry.TABLE_NAME
                + " on " + AppointmentEntry.COLUMN_VISITOR + " = "
                + VisitorEntry.TABLE_NAME + "." + VisitorEntry._ID;
        SQLiteQueryBuilder builder;

        switch (match) {
            case APPOINTMENT:
                builder = new SQLiteQueryBuilder();
                builder.setTables(inTables);
                cursor = builder.query(db, projection, selection, selectionArgs,
                        null, null, orderBy);
                break;
            case APPOINTMENT_ID:
                builder = new SQLiteQueryBuilder();
                builder.setTables(inTables);
                selection = AppointmentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = builder.query(db, projection, selection, selectionArgs, null, null, orderBy);
                break;
            case STAFF:
                cursor = db.query(StaffEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, orderBy);
                break;
            case STAFF_ID:
                selection = StaffEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(StaffEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, orderBy);
                break;
            case VISITOR:
                cursor = db.query(VisitorEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, orderBy);
                break;
            case VISITOR_ID:
                selection = VisitorEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(VisitorEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, orderBy);
                break;
            default:
                throw new IllegalArgumentException("Query unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case VISITOR:
                return insertRecord(uri, contentValues, VisitorEntry.TABLE_NAME);
            case STAFF:
                return insertRecord(uri, contentValues, StaffEntry.TABLE_NAME);
            case APPOINTMENT:
                return insertRecord(uri, contentValues, AppointmentEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private Uri insertRecord(Uri uri, ContentValues values, String table) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(table, null, values);
        if (id == -1) {
            Log.e("Error", "insert error for URI " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case STAFF_ID:
                return deleteRecord(uri, selection, selectionArgs, StaffEntry.TABLE_NAME);
            case VISITOR_ID:
                return deleteRecord(uri, selection, selectionArgs, VisitorEntry.TABLE_NAME);
            case APPOINTMENT_ID:
                return deleteRecord(uri, selection, selectionArgs, AppointmentEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private int deleteRecord(Uri uri, String selection, String[] selectionArgs, String tableName) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = db.delete(tableName, selection, selectionArgs);
        if (id == -1) {
            Log.e("Error", "delete unknown URI " + uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case STAFF:
                return updateRecord(uri, values, selection, selectionArgs, StaffEntry.TABLE_NAME);
            case VISITOR:
                return updateRecord(uri, values, selection, selectionArgs, VisitorEntry.TABLE_NAME);
            case APPOINTMENT:
                return updateRecord(uri, values, selection, selectionArgs, AppointmentEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Update unknown URI: " + uri);
        }
    }

    private int updateRecord(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tableName) {
        //this time we need a writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = db.update(tableName, values, selection, selectionArgs);
        if (id == 0) {
            Log.e("Error", "update error for URI " + uri);
            return -1;
        }
        return id;
    }
}

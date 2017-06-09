package com.liuyufei.bmc_android.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class BMCContract {
    //URI section
    public static final String CONTENT_AUTHORITY = "com.bmc.dataprovider";
    public static final String PATH_BMC_STAFF="STAFF";
    public static final String PATH_BMC_APPOINTMENT="APPOINTMENT";
    public static final String PATH_BMC_VISITOR="VISITOR";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public String concatContent(String path){
        return "content://" + path;
    }

    public static final class StaffEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BMC_STAFF);

        // Table name
        public static final String TABLE_NAME = "BMC_STAFF";
        //column (field) names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOBILE = "mobile";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_PHOTO = "photo";
    }

    public static final class AppointmentEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BMC_APPOINTMENT);

        // Table name
        public static final String TABLE_NAME = "BMC_APPOINTMENT";
        //column names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_STAFF = "staff";
        public static final String COLUMN_VISITOR = "visitor";
        public static final String COLUMN_DATETIME = "app_datetime";
    }

    public static final class VisitorEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BMC_VISITOR);
        // Table name
        public static final String TABLE_NAME = "BMC_VISITOR";
        //column names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BUSINESS_NAME = "business_name";
        public static final String COLUMN_MOBILE = "mobile";
    }
}

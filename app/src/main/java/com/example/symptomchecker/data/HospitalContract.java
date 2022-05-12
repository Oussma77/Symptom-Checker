package com.example.symptomchecker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class HospitalContract {

    private HospitalContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.symptomchecker.data.HospitalProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_HOSPITALS = "hospitals";

    public static final class HospitalEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HOSPITALS);

        public final static String TABLE_NAME_HOSPITAL = "hospitals";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_HOSPITAL_NAME ="name";
        public final static String COLUMN_HOSPITAL_WORKTIME ="work_time";
        public final static String COLUMN_HOSPITAL_PHONE = "phone";
        public final static String COLUMN_HOSPITAL_ADDRESS ="address";




        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of hospital.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HOSPITALS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single hospital.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HOSPITALS;


    }


}

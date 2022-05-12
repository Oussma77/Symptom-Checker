package com.example.symptomchecker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DoctorContract {

    private DoctorContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.symptomchecker.data.DoctorProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DOCTORS = "doctors";

    public static final class DoctorEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DOCTORS);

        public final static String TABLE_NAME_DOCTOR = "doctors";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DOCTOR_NAME ="name";
        public final static String COLUMN_DOCTOR_AGE ="age";
        public final static String COLUMN_DOCTOR_PHONE = "phone";
        public final static String COLUMN_DOCTOR_ADDRESS ="address";
        public final static String COLUMN_DOCTOR_EMAIL = "email";




        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of doctors.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOCTORS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single doctors.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOCTORS;




    }

}

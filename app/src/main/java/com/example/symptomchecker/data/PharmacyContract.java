package com.example.symptomchecker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PharmacyContract {

    private PharmacyContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.symptomchecker.data.PharmacyProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PHARMACYS = "pharmacys";

    public static final class PharmacyEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PHARMACYS);

        public final static String TABLE_NAME_PHARMACYS = "pharmacys";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PHARMACYS_NAME ="name";
        public final static String COLUMN_PHARMACYS_WORKTIME ="work_time";
        public final static String COLUMN_PHARMACYS_PHONE = "phone";
        public final static String COLUMN_PHARMACYS_ADDRESS ="address";




        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of PHARMACY.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHARMACYS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single PHARMACY.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHARMACYS;
    }

}

package com.example.symptomchecker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class IllnessContracr {

    private IllnessContracr(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.symptomchecker.data.IllnessProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ILLNESSES = "illnesses";

    public static final class IllnessEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ILLNESSES);

        public final static String TABLE_NAME_ILLNESS = "illnesses";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ILLNESS_NAME ="name_illness";
        public final static String COLUMN_ILLNESS_TYPE ="type";
        public final static String COLUMN_ILLNESS_DESC ="description";
        public final static String COLUMN_ILLNESS_CAUSES ="causes";
        public final static String COLUMN_ILLNESS_RISK ="who_risk";
        public final static String COLUMN_ILLNESS_SYMPTOMS ="symptoms";
        public final static String COLUMN_ILLNESS_MEDICINES ="medicines";
        public final static String COLUMN_ILLNESS_ASKDOCTOR ="ask_doctor";




        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of PHARMACY.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ILLNESSES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single PHARMACY.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ILLNESSES;


    }

}

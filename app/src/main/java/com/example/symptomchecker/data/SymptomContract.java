package com.example.symptomchecker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class SymptomContract {

    private  SymptomContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.symptomchecker.data.SymptomProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SYMPTOMS = "symptoms";

    public static final class SymptomEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SYMPTOMS);

        public final static String TABLE_NAME_SYMPTOMS = "symptoms";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SYMPTOM_NAME ="name_symptom";
        public final static String COLUMN_SYMPTOM_DESCRIPTION ="description";
        public final static String COLUMN_SYMPTOM_NEEDHELP ="need_help";
        public final static String COLUMN_SYMPTOM_CLASIFICATION ="classification";
        public final static String COLUMN_SYMPTOM_ISRECOMMENDED ="isrecommended";
        public final static String COLUMN_SYMPTOM_ISNOTRECOMMENDED ="isnotrecommended";
        public final static String COLUMN_SYMPTOM_REASONS ="reasons";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of PHARMACY.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYMPTOMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single PHARMACY.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYMPTOMS;


    }

}

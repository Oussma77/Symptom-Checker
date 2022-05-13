package com.example.symptomchecker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.symptomchecker.data.SymptomContract.SymptomEntry;

public class SymptomCursorAdapter  extends CursorAdapter {

    public SymptomCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_symptom, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView name_symptomTextView = (TextView) view.findViewById(R.id.name_symptom);
        TextView desc_symptomTextView = (TextView) view.findViewById(R.id.desc_symptom);
        TextView need_help_symptomTextView = (TextView) view.findViewById(R.id.need_help_symptom);
        TextView classification_symptomTextView = (TextView) view.findViewById(R.id.classification_symptom);
        TextView isrecommended_symptomTextView = (TextView) view.findViewById(R.id.isrecommended_symptom);
        TextView isnotrecommended_symptomTextView = (TextView) view.findViewById(R.id.isnotrecommended_symptom);
        TextView reasons_symptomTextView = (TextView) view.findViewById(R.id.reasons_symptom);



        // Find the columns of hosp attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_NAME);
        int descColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION);
        int need_helpColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP);
        int classificationColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION);
        int isrecommendedColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED);
        int isnotrecommendedColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED);
        int reasonsColumnIndex = cursor.getColumnIndex(SymptomEntry.COLUMN_SYMPTOM_REASONS);

        // Read the hosp attributes from the Cursor for the current hos
        String a1 = cursor.getString(nameColumnIndex);
        String a2 = cursor.getString(descColumnIndex);
        String a3 = cursor.getString(need_helpColumnIndex);
        String a4 = cursor.getString(classificationColumnIndex);
        String a5 = cursor.getString(isrecommendedColumnIndex);
        String a6 = cursor.getString(isnotrecommendedColumnIndex);
        String a7 = cursor.getString(reasonsColumnIndex);

        name_symptomTextView.setText(a1);
        desc_symptomTextView.setText(a2);
        need_help_symptomTextView.setText(a3);
        classification_symptomTextView.setText(a4);
        isrecommended_symptomTextView.setText(a5);
        isnotrecommended_symptomTextView.setText(a6);
        reasons_symptomTextView.setText(a7);

    }
}

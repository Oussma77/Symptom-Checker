package com.example.symptomchecker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;

public class MainQuizOneIllnessCursorAdapter extends CursorAdapter {

    public MainQuizOneIllnessCursorAdapter (Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_search_illnesses, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView name_illnessTextView = (TextView) view.findViewById(R.id.TextView_name_search_illness);

        // Find the columns of hosp attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_NAME);

        // Read the hosp attributes from the Cursor for the current hos
        String a1 = cursor.getString(nameColumnIndex);

        name_illnessTextView.setText(a1);

    }

}

package com.example.symptomchecker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;

public class IllnessCursorAdapter extends CursorAdapter {

    public IllnessCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_illness, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name_illness);
        TextView typeTextView = (TextView) view.findViewById(R.id.type_illness);
        TextView descTextView = (TextView) view.findViewById(R.id.desc_illness);
        TextView causesTextView = (TextView) view.findViewById(R.id.causes_illness);
        TextView riskTextView = (TextView) view.findViewById(R.id.risk_illness);
        TextView symptomsTextView = (TextView) view.findViewById(R.id.symptoms_illness);
        TextView medicinesTextView = (TextView) view.findViewById(R.id.medicines_illness);
        TextView askTextView = (TextView) view.findViewById(R.id.ask_illness);



        // Find the columns of hosp attributes that we're interested in
        int ColumnIndex1 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_NAME);
        int ColumnIndex2 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_TYPE);
        int ColumnIndex3 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_DESC);
        int ColumnIndex4 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_CAUSES);
        int ColumnIndex5 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_RISK);
        int ColumnIndex6 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS);
        int ColumnIndex7 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_MEDICINES);
        int ColumnIndex8 = cursor.getColumnIndex(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR);

        // Read the hosp attributes from the Cursor for the current hos
        String a1 = cursor.getString(ColumnIndex1);
        String a2 = cursor.getString(ColumnIndex2);
        String a3 = cursor.getString(ColumnIndex3);
        String a4 = cursor.getString(ColumnIndex4);
        String a5 = cursor.getString(ColumnIndex5);
        String a6 = cursor.getString(ColumnIndex6);
        String a7 = cursor.getString(ColumnIndex7);
        String a8 = cursor.getString(ColumnIndex8);

        nameTextView.setText(a1);
        typeTextView.setText(a2);
        descTextView.setText(a3);
        causesTextView.setText(a4);
        riskTextView.setText(a5);
        symptomsTextView.setText(a6);
        medicinesTextView.setText(a7);
        askTextView.setText(a7);

    }

}

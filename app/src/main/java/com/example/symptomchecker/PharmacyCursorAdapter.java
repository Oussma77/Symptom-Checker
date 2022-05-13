package com.example.symptomchecker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.symptomchecker.data.PharmacyContract.PharmacyEntry;

public class PharmacyCursorAdapter extends CursorAdapter {


    public PharmacyCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_pharmacy, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView nameTextView1 = (TextView) view.findViewById(R.id.name_pharmacy);
        TextView timeTextView1 = (TextView) view.findViewById(R.id.time_pharmacy);
        TextView phoneTextView1 = (TextView) view.findViewById(R.id.phone_pharmacy);
        TextView addressTextView1 = (TextView) view.findViewById(R.id.address_pharmacy);


        // Find the columns of hosp attributes that we're interested in
        int nameColumnIndex1 = cursor.getColumnIndex(PharmacyEntry.COLUMN_PHARMACYS_NAME);
        int timeColumnIndex1 = cursor.getColumnIndex(PharmacyEntry.COLUMN_PHARMACYS_WORKTIME);
        int phoneColumnIndex1 = cursor.getColumnIndex(PharmacyEntry.COLUMN_PHARMACYS_PHONE);
        int addressColumnIndex1 = cursor.getColumnIndex(PharmacyEntry.COLUMN_PHARMACYS_ADDRESS);

        // Read the hosp attributes from the Cursor for the current hos
        String mName = cursor.getString(nameColumnIndex1);
        String mTime = cursor.getString(timeColumnIndex1);
        String mPhone = cursor.getString(phoneColumnIndex1);
        String mAddress = cursor.getString(addressColumnIndex1);

        nameTextView1.setText(mName);
        timeTextView1.setText(mTime);
        phoneTextView1.setText(mPhone);
        addressTextView1.setText(mAddress);

    }
}

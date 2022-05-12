package com.example.symptomchecker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.symptomchecker.data.HospitalContract.HospitalEntry;

public class HospitalCursorAdapter extends CursorAdapter {


    public HospitalCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_hospital, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name_hospital);
        TextView timeTextView = (TextView) view.findViewById(R.id.time_hospital);
        TextView phoneTextView = (TextView) view.findViewById(R.id.phone_hospital);
        TextView addressTextView = (TextView) view.findViewById(R.id.address_hospital);


        // Find the columns of hosp attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(HospitalEntry.COLUMN_HOSPITAL_NAME);
        int timeColumnIndex = cursor.getColumnIndex(HospitalEntry.COLUMN_HOSPITAL_WORKTIME);
        int phoneColumnIndex = cursor.getColumnIndex(HospitalEntry.COLUMN_HOSPITAL_PHONE);
        int addressColumnIndex = cursor.getColumnIndex(HospitalEntry.COLUMN_HOSPITAL_ADDRESS);

        // Read the hosp attributes from the Cursor for the current hos
        String hospitalName = cursor.getString(nameColumnIndex);
        String hospitalTime = cursor.getString(timeColumnIndex);
        String hospitalPhone = cursor.getString(phoneColumnIndex);
        String hospitalAddress = cursor.getString(addressColumnIndex);

        nameTextView.setText(hospitalName);
        timeTextView.setText(hospitalTime);
        phoneTextView.setText(hospitalPhone);
        addressTextView.setText(hospitalAddress);


    }
}

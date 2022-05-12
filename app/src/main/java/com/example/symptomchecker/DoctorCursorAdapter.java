package com.example.symptomchecker;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.symptomchecker.data.DoctorContract.DoctorEntry;

public class DoctorCursorAdapter extends CursorAdapter {

    public DoctorCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_doctor, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView ageTextView = (TextView) view.findViewById(R.id.age);
        TextView phoneTextView = (TextView) view.findViewById(R.id.phone);
        TextView addressTextView = (TextView) view.findViewById(R.id.address);
        TextView emailTextView = (TextView) view.findViewById(R.id.email);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(DoctorEntry.COLUMN_DOCTOR_NAME);
        int ageColumnIndex = cursor.getColumnIndex(DoctorEntry.COLUMN_DOCTOR_AGE);
        int phoneColumnIndex = cursor.getColumnIndex(DoctorEntry.COLUMN_DOCTOR_PHONE);
        int addressColumnIndex = cursor.getColumnIndex(DoctorEntry.COLUMN_DOCTOR_ADDRESS);
        int emailColumnIndex = cursor.getColumnIndex(DoctorEntry.COLUMN_DOCTOR_EMAIL);

        // Read the pet attributes from the Cursor for the current pet
        String doctorName = cursor.getString(nameColumnIndex);
        int doctorAge = Integer.parseInt(cursor.getString(ageColumnIndex));
        String doctorPhone = cursor.getString(phoneColumnIndex);
        String doctorAddress = cursor.getString(addressColumnIndex);
        String doctorEmail = cursor.getString(emailColumnIndex);

        nameTextView.setText(doctorName);
        ageTextView.setText(doctorAge+"");
        phoneTextView.setText(doctorPhone);
        addressTextView.setText(doctorAddress);
        emailTextView.setText(doctorEmail);

    }
}

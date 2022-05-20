package com.example.symptomchecker;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class wLoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    wLoadingDialog(Activity myActivity){
        this.activity = myActivity;
    }


    void startLoadingDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this.activity);

        LayoutInflater inflater= activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

    }

    void dismissDialog(){
        alertDialog.dismiss();
    }


}

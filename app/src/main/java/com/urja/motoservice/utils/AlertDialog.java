package com.urja.motoservice.utils;

import android.content.Context;

/**
 * Created by BAPI1 on 8/27/2016.
 */
public class AlertDialog {
    public static void Alert(Context context, String title, String Message) {
        android.support.v7.app.AlertDialog.Builder alertbox = new android.support.v7.app.AlertDialog.Builder(context);
        alertbox.setTitle(title);
        alertbox.setMessage(Message);
        alertbox.show();
    }
}

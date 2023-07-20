package com.numinha.pabb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;

import java.io.UnsupportedEncodingException;

public class Tests {
    Activity activity;

    public Tests(Activity t_activity){
        this.activity = t_activity;
    }

    public void alertPopup(String text, boolean fast){
        Toast.makeText(activity, text, (fast) ? Toast.LENGTH_SHORT: Toast.LENGTH_LONG).show();
    }
    public void alertDialog(String text, String title){
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(text);
        alert = builder.create();
        alert.show();

    }
    public void errorAlert(VolleyError error){
        String body = "";
        final String statusCode = String.valueOf(error.networkResponse.statusCode);
        try {
            body = new String(error.networkResponse.data,"UTF-8");
            alertDialog(body,"Error");

        } catch (UnsupportedEncodingException e) {
            alertPopup("Strange error",false);
        }
    }
}

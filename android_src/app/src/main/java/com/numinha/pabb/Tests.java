package com.numinha.pabb;

import android.app.Activity;
import android.widget.Toast;

public class Tests {
    Activity activity;

    public Tests(Activity t_activity){
        this.activity = t_activity;
    }

    public void alertPopup(String text, boolean fast){
        Toast.makeText(activity, text, (fast) ? Toast.LENGTH_SHORT: Toast.LENGTH_LONG).show();
    }
}

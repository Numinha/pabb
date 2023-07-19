package com.numinha.pabb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Configs {

    Activity activity;

    public Configs(Activity t_activity){
        this.activity = t_activity;
    }

    public void main(){
        setReturnButton();

    }
    private void setReturnButton(){
        Button imgv_search = (Button) activity.findViewById(R.id.btn_return);
        imgv_search.setOnClickListener(view -> {
            activity.setContentView(R.layout.activity_main);
            ((MainActivity) activity).ChangeMainPage();
        });
    }
}

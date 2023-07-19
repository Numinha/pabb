package com.numinha.pabb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.ImageView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    WebCrawler wCraw;
    Tests tests;
    Rule34 r34;
    String booru = "rule34";
    Configs configs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main();
    }

    private void main(){
        wCraw = new WebCrawler(this,this);
        tests = new Tests(this);
        configs = new Configs(this);
        r34 = new Rule34(this, wCraw, tests);

        //Test internet
        if(!wCraw.checkNet()){
            tests.alertPopup("Without connection",false);
        }

        ChangeMainPage();

    }
    public void ChangeMainPage(){
        //Set configuration screen
        setContentView(R.layout.activity_main);

        ImageView imgv_search = (ImageView) findViewById(R.id.imgv_config);
        imgv_search.setOnClickListener(view -> {
            wCraw.cancelAll();
            setContentView(R.layout.configs);
            configs.main();
        });

        if(Objects.equals(booru, "rule34")){
            r34.setImageQuality(400,300);
            r34.setAutoCompleteText();
        }
    }

}
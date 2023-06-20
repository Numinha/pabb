package com.numinha.pabb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    WebCrawler wCraw;
    Tests tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main();
    }
    private void main(){
        wCraw = new WebCrawler(this);
        tests = new Tests(this);

        String result_conn = wCraw.checkNet() ? "Connected" : "Without Internet";
        tests.alertPopup(result_conn,false);
    }
}
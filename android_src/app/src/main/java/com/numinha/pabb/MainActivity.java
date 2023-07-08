package com.numinha.pabb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    WebCrawler wCraw;
    Tests tests;
    String rule34_base = "https://api.rule34.xxx/index.php?page=dapi&s=post&q=index&tags=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main();
    }
    private void main(){
        wCraw = new WebCrawler(this,this);
        tests = new Tests(this);

        //Test internet
        String result_conn = wCraw.checkNet() ? "Connected" : "Without Internet";
        tests.alertPopup(result_conn,true);

        scrollRule34(rule34_base + "%2a");

        EditText url_edt = findViewById(R.id.url_edtTxv);
        url_edt.setOnKeyListener((view, i, keyEvent) -> {
            LinearLayout linearLayout = findViewById(R.id.layout_scroll);
            linearLayout.removeAllViews();
            wCraw.cancelAll();
            scrollRule34(rule34_base + url_edt.getText().toString() + "%2a");
            searchTags(url_edt.getText().toString() + "*");
            return false;
        });
    }
    private void scrollRule34(String url){
        LinearLayout linearLayout = findViewById(R.id.layout_scroll);
        Response.ErrorListener error = error1 -> tests.alertPopup(error1.toString(),false);

        Response.Listener<String> response = response1 -> {
            int cont = 1;
            String url_image = wCraw.searchIMG(response1,cont,url,"rule34");

            while (url_image != null)
            {
                Response.Listener<Bitmap> response2 = response3 -> {
                    ImageView image = new ImageView(wCraw.context);
                    image.setImageBitmap(response3);
                    image.setMinimumHeight(480);
                    image.setMinimumWidth( Resources.getSystem().getDisplayMetrics().widthPixels);
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.addView(image);
                };

                wCraw.getImage(url_image, response2, error, Resources.getSystem().getDisplayMetrics().widthPixels,480);

                cont++;
                url_image = wCraw.searchIMG(response1,cont,url,"rule34");
            }
        };

        wCraw.getHTML(url, response, error);
    }

    public void searchTags(String tags){
        LinearLayout linearLayout = findViewById(R.id.layout_scroll);
        LinearLayout search_layout = findViewById(R.id.search_layout);

        Response.ErrorListener error = error1 -> tests.alertPopup(error1.toString(),false);

        Response.Listener<String> response = response1 -> {
            int number = 5;
            String result;
            String result2;
            EditText url_edt = findViewById(R.id.url_edtTxv);
            search_layout.removeAllViews();
            for (int i = 1; i <= number; i++){
                if (response1.indexOf("<tr><td>") > 0){
                    response1 = response1.substring(response1.indexOf("<tr><td>") + 8);
                    result = response1;
                    result = result.substring(result.indexOf("tags=") + 5);
                    result2 = result.substring(result.indexOf('"')+2,result.indexOf("</a>"));
                    result = result.substring(0,result.indexOf('"'));

                    Button btn_temp = new Button(wCraw.context);
                    btn_temp.setText(result2);
                    String finalResult = result;
                    String finalResult2 = result2;
                    btn_temp.setOnTouchListener((View view, MotionEvent motionEvent) -> {
                            linearLayout.removeAllViews();
                            wCraw.cancelAll();

                            url_edt.setText(finalResult2);
                            scrollRule34(rule34_base + finalResult + "%2a");
                            search_layout.removeAllViews();
                            return false;
                        }
                    );
                    btn_temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    search_layout.addView(btn_temp);
                }
            }

        };

        wCraw.getHTML("https://rule34.xxx/index.php?page=tags&s=list&tags=" + tags + "&sort=desc&order_by=index_count", response, error);
    }
}
package com.numinha.pabb;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.KeyEvent;
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

        scrollRule34("https://api.rule34.xxx/index.php?page=dapi&s=post&q=index");

        EditText url_edt = findViewById(R.id.url_edtTxv);
        url_edt.setImeActionLabel("Ir", KeyEvent.KEYCODE_ENTER);
        url_edt.setText("https://api.rule34.xxx/index.php?page=dapi&s=post&q=index");
        url_edt.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)) {
                String url = url_edt.getText().toString();

                LinearLayout linearLayout = findViewById(R.id.layout_scroll);
                linearLayout.removeAllViews();

                EditText url_new = new EditText(wCraw.context);
                url_new.setId(R.id.url_edtTxv);
                url_new.setText(url);

                linearLayout.addView(url_new);
                scrollRule34(url_edt.getText().toString());
                return true;
            }
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
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.addView(image);
                };

                wCraw.getImage(url_image, response2, error,640,480);

                cont++;
                url_image = wCraw.searchIMG(response1,cont,url,"rule34");
            }
        };

        wCraw.getHTML(url, response, error);
    }
}
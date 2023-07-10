package com.numinha.pabb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.io.FileOutputStream;
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
        if(wCraw.checkNet()){
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
        }else {
            tests.alertPopup("Without connection",false);
        }
    }

    private void scrollRule34(String url){
        LinearLayout linearLayout = findViewById(R.id.layout_scroll);
        Response.ErrorListener error = error1 -> {};

        Response.Listener<String> response = response1 -> {
            int cont = 1;
            String url_image = wCraw.searchIMG(response1,cont,url,"rule34");

            while (url_image != null)
            {
                Response.Listener<Bitmap> response2 = response3 -> {
                    ImageView image = new ImageView(wCraw.context);
                    image.setImageBitmap(response3);
                    image.setMinimumHeight(640);
                    image.setMinimumWidth( Resources.getSystem().getDisplayMetrics().widthPixels);
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    image.setOnClickListener(view -> {

                        String root = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                        File myDir = new File(root + "/saved_images");
                        myDir.mkdirs();

                        String fname = "Image.jpg";
                        tests.alertPopup("saved: " + root + "/saved_images/" + fname,true);
                        File file = new File (myDir, fname);
                        if (file.exists ()) file.delete ();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            response3.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.addView(image);
                };

                wCraw.getImage(url_image, response2, error, Resources.getSystem().getDisplayMetrics().widthPixels-50,
                        Resources.getSystem().getDisplayMetrics().heightPixels-50);
                cont++;
                url_image = wCraw.searchIMG(response1,cont,url,"rule34");
            }
        };

        wCraw.getHTML(url, response, error);
    }

    public void searchTags(String tags){
        LinearLayout linearLayout = findViewById(R.id.layout_scroll);
        LinearLayout search_layout = findViewById(R.id.search_layout);

        Response.ErrorListener error = error1 -> {};

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
                    btn_temp.setBackgroundColor(wCraw.context.getColor(R.color.purple_700));
                    btn_temp.setTextColor(wCraw.context.getColor(R.color.white));
                    String finalResult = result;
                    String finalResult2 = result2;

                    btn_temp.setOnClickListener((View view) -> {
                            linearLayout.removeAllViews();
                            wCraw.cancelAll();

                            url_edt.setText(finalResult2);
                            scrollRule34(rule34_base + finalResult + "%2a");
                            search_layout.removeAllViews();
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
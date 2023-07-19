package com.numinha.pabb;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.android.volley.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

public class Rule34 {

    WebCrawler wCraw;
    Tests tests;
    Activity activity;
    int width;
    int height;
    Response.ErrorListener error;

    String rule34_base = "https://api.rule34.xxx/index.php?page=dapi&s=post&q=index&limit=2&pid=0&tags=";

    public Rule34(Activity t_activity, WebCrawler t_wCraw, Tests t_tests){
        this.wCraw = t_wCraw;
        this.tests = t_tests;
        this.activity = t_activity;
        this.width = Resources.getSystem().getDisplayMetrics().widthPixels-50;
        this.height = Resources.getSystem().getDisplayMetrics().heightPixels-50;
        error = e -> tests.alertDialog(e.getMessage(), e.toString());

    }
    public void setAutoCompleteText(){
        AutoCompleteTextView actv_search = (AutoCompleteTextView) activity.findViewById(R.id.actv_search);

        //Add Search Tags by Enter
        actv_search.setOnKeyListener((view, i, keyEvent) -> {
            if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP){
                Response.Listener<String> response = response1 -> {

                    String[] result_return = {"All","","","","",""};
                    for (int cont = 1; cont <= 5; cont++){
                        if (response1.indexOf("<tr><td>") > 0){
                            String result;
                            String result2;
                            response1 = response1.substring(response1.indexOf("<tr><td>") + 8);
                            result = response1;
                            result = result.substring(result.indexOf("tags=") + 5);
                            result2 = result.substring(result.indexOf('"')+2,result.indexOf("</a>"));
                            result = result.substring(0,result.indexOf('"'));

                            result_return[cont] = result2;
                        }
                    }
                    ArrayAdapter<String> searchs2 = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, result_return);
                    actv_search.setAdapter(searchs2);
                    actv_search.showDropDown();

                };
                wCraw.getHTML("https://rule34.xxx/index.php?page=tags&s=list&tags=" + actv_search.getText().toString() + "*&sort=desc&order_by=index_count", response, error);

            }
            return false;
        });

        search(rule34_base + "*");

        actv_search.setOnItemClickListener((adapterView, view, i, l) -> search(rule34_base + actv_search.getText() + "*"));
    }

    private void search(String url){
        //load actual image
        Response.Listener<String> response = response1 -> {
            Response.Listener<Bitmap> response2 = response3 -> {
                ImageView image = activity.findViewById(R.id.imgv_base);
                image.setImageBitmap(response3);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                //Save image
                image.setOnClickListener(view -> {
                    String root = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                    File myDir = new File(root + "/saved_images");
                    Date currentTime = Calendar.getInstance().getTime();
                    String time = currentTime.toString();
                    time = time.substring(0,19).replaceAll("[: ]","-");
                    String fname = "Image_" + time + "_.jpg";

                    File file = new File (myDir, fname);
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        response3.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        tests.alertPopup("saved",true);

                    } catch (Exception e) {
                        e.printStackTrace();
                        tests.alertDialog(e.getMessage(), e.toString());
                    }
                });
            };

            String url_image = wCraw.searchIMG(response1,1,rule34_base,"rule34");
            wCraw.getImage(url_image, response2, error, width, height);
        };
        wCraw.getHTML(url, response, error);
    }
    public void setImageQuality(int t_Width,int t_Height){
        this.width = t_Height;
        this.height = t_Width;
    }
}

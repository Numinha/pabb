package com.numinha.pabb;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    int posts_number;
    int post_id = 1;
    Posts[] posts_cache;
    Response.ErrorListener error;

    String rule34_base = "https://api.rule34.xxx/index.php?page=dapi&s=post&q=index&limit=2&pid=0&tags=";

    public Rule34(Activity t_activity, WebCrawler t_wCraw, Tests t_tests){
        this.wCraw = t_wCraw;
        this.tests = t_tests;
        this.activity = t_activity;
        this.width = Resources.getSystem().getDisplayMetrics().widthPixels-50;
        this.height = Resources.getSystem().getDisplayMetrics().heightPixels-50;
        this.posts_number = 5;
        error = e -> tests.errorAlert(e);
        posts_cache = new Posts[this.posts_number];

    }
    public void setAutoCompleteText(){
        AutoCompleteTextView actv_search = activity.findViewById(R.id.actv_search);
        String[] result_return = {"All","female","breasts","penis","male","nipples"};
        //Add Search Tags
        actv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Response.Listener<String> response = response1 -> {

                    if(!response1.contains("refine")){
                        for (int cont = 1; cont <= 5; cont++){
                            if (response1.indexOf("<tr><td>") > 0){
                                String result;
                                response1 = response1.substring(response1.indexOf("<tr><td>") + 8);
                                result = response1;
                                result = result.substring(result.indexOf("tags=") + 5);
                                result = result.substring(result.indexOf('"')+2,result.indexOf("</a>"));
                                result_return[cont] = result;
                            }
                        }
                    }
                    ArrayAdapter<String> searchs2 = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, result_return);
                    actv_search.setAdapter(searchs2);
                    post_id = 1;
                };
                wCraw.cancelTags("tags");
                wCraw.getTags("https://rule34.xxx/index.php?page=tags&s=list&tags=" + actv_search.getText().toString() + "*&sort=desc&order_by=index_count", response, error);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                actv_search.showDropDown();
            }
        });

        search(rule34_base + "*",post_id);

        actv_search.setOnItemClickListener((adapterView, view, i, l) -> search(rule34_base + actv_search.getText() + "*",post_id));
    }

    private void search(String url, int id){
        //load search page
        Response.Listener<String> gethtml = html -> {
            boolean test = true;
            int cont = 0;
            while(test){
                Response.Listener<Bitmap> getimage = image -> {
                    Posts temp_post = new Posts(activity);
                    temp_post.setImage(image);
                    posts_cache[cont] = temp_post;
                };
                test =  wCraw.getImage(html, getimage, error, width, height, post_id);
            }
            refreshImage(0);
        };
        wCraw.getHTML(url, gethtml, error);

    }
    public void refreshImage(int id_p){
        LinearLayout search_layout = activity.findViewById(R.id.search_layout);
        search_layout.removeAllViews();
        if(posts_cache[id_p] != null){
            ImageView imagev =  posts_cache[id_p].getImage();
            imagev.setOnTouchListener((view, motionEvent) -> {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    refreshImage(id_p+1);
                }
                return false;
            });
            search_layout.addView(imagev);
        }
    }
    public void setImageQuality(int t_Width,int t_Height){
        this.width = t_Height;
        this.height = t_Width;
    }
    public void setPostsNumber(int quant){
        this.posts_number = quant;
        posts_cache = new Posts[this.posts_number];
    }

}

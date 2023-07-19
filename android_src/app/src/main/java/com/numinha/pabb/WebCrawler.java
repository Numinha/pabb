package com.numinha.pabb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WebCrawler {
    Context context;
    Activity activity;
    RequestQueue queue;

    public WebCrawler(Context t_context, Activity t_activity){
        this.context = t_context;
        this.activity = t_activity;
        queue = Volley.newRequestQueue(context);
    }
    public boolean checkNet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getHTML(String url_string,Response.Listener<String> response, Response.ErrorListener error){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_string, response, error);
        queue.add(stringRequest);
    }

    public void getImage(String url_string,Response.Listener<Bitmap> response, Response.ErrorListener error, int width, int height){
        ImageRequest ir = new ImageRequest(
                url_string, response, width,
                height, ImageView.ScaleType. CENTER_INSIDE, Bitmap.Config.RGB_565, error);
        queue.add(ir);
    }

    public void cancelAll(){
        queue.cancelAll(request -> true);
    }

    public String searchIMG(String result, int number, String url,String booru){
        for (int i = 1; i <= number; i++){
            if (result.indexOf("<post ") > 0){
                result = result.substring(result.indexOf("<post") + 5);
            }else {
                return null;
            }
        }
        result = result.substring(result.indexOf("file_url=\"") + 10);
        result = result.substring(0,result.indexOf('"'));

        return result;
    }
}

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
import com.android.volley.VolleyError;
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
                height, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565, error);
        queue.add(ir);
    }

    public String searchIMG(String result, int number, String url,String booru){

        switch (booru){
            case "google":
                for (int i = 1; i <= number; i++){
                    if (result.indexOf("<img") > 0){
                        result = result.substring(result.indexOf("<img") + 4);
                    }else {
                        return null;
                    }
                }
                result = result.substring(result.indexOf("src=\"") + 5);
                result = result.substring(0,result.indexOf('"'));
                if(!result.contains("https")){
                    result = " https://" + result;
                }
                break;
            case "rule34":
                for (int i = 1; i <= number; i++){
                    if (result.indexOf("<post ") > 0){
                        result = result.substring(result.indexOf("<post") + 5);
                    }else {
                        return null;
                    }
                }
                result = result.substring(result.indexOf("file_url=\"") + 10);
                result = result.substring(0,result.indexOf('"'));
                break;
            default:

                break;
        }

        return result;
    }
}

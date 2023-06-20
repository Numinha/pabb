package com.numinha.pabb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WebCrawler {
    Context context;
    public WebCrawler(Context t_context){
        this.context = t_context;
    }
    public boolean checkNet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

package com.numinha.pabb;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

public class Posts {
    ImageView post;
    Activity activity;

    public Posts(Activity t_activity){
        this.activity = t_activity;
        post = new ImageView(activity);
    }

    public void setImage(Bitmap bitmap){
        post.setImageBitmap(bitmap);
        post.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //Save image
        post.setOnClickListener(view -> {
            String root = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            File myDir = new File(root + "/saved_images");
            Date currentTime = Calendar.getInstance().getTime();
            String time = currentTime.toString();
            time = time.substring(0,19).replaceAll("[: ]","-");
            String fname = "Image_" + time + "_.jpg";

            File file = new File (myDir, fname);
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        post.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
    }
    public ImageView getImage(){
        return post;
    }
}

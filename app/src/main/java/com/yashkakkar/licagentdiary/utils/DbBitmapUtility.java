package com.yashkakkar.licagentdiary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Yash Kakkar on 04-06-2017.
 */

public class DbBitmapUtility {

    // convert ByteArray into Bitmap
    public Bitmap getBitmap(byte[] image){
        byte[] imgbytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imgbytes,0,imgbytes.length);
    }

    // convert Bitmap into ByteArray
    public byte[] getByteArray(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,stream);
        return stream.toByteArray();
    }
}

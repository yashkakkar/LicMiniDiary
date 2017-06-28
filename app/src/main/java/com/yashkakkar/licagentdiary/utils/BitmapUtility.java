package com.yashkakkar.licagentdiary.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Yash Kakkar on 18-06-2017.
 */

public class BitmapUtility {

    // resize bitmap image
    public Bitmap getScaledBitmap(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width/ (float)height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        //Passing filter = false will result in a blocky, pixellated image.
        //Passing filter = true will give you smoother edges.
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

/*
    Skills of full-stack developer having several years of experience with android applications and web applications.
    Solving problem in real life and leaning new things each day is my goal. I love to code in java.
    My day starts with a coffee :) and ends when i am done coding...

        */// resize a bitmap to a particular width and height
    public Bitmap getNewBitmap(Bitmap image, int width, int height){
        int image_width = image.getWidth();
        int image_height = image.getHeight();
        float scale_width = ((float) width) / image_width;
        float scale_height = ((float) height) / image_height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scale_width, scale_height);
        // recreate the new Bitmap
        return Bitmap.createBitmap(image, 0, 0, width, height, matrix, false);
    }

}

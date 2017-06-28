package com.yashkakkar.licagentdiary.utils;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Yash Kakkar on 26-05-2017.
 */

public class AssetUtils {


    public static boolean fileExistsOrNot(String fileName, String path, AssetManager assetManager) throws IOException {

        for (String currentFileName : assetManager.list(path)){
            if (currentFileName.equals(fileName)){
                return true;
            }
        }

        return false;
    }

    public static String[] listOfAllFiles(String path, AssetManager assetManager) throws IOException {
        String[] files = assetManager.list(path);
        Arrays.sort(files);
        return files;
    }

    public static String readFile(String fileName, AssetManager assetManager) throws IOException {
        InputStream inputStream;
        inputStream = assetManager.open(fileName);
        int size = inputStream.available();
        byte[] bytes = new byte[size];
        inputStream.read(bytes);
        inputStream.close();

        return new String(bytes);
    }


}


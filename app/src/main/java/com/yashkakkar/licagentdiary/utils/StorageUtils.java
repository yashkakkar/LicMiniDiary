package com.yashkakkar.licagentdiary.utils;

import android.os.Environment;

/**
 * Created by Yash Kakkar on 18-06-2017.
 */

public class StorageUtils {


    // create storage directory for images/ videos / documents / others files

    // check for the which storage is available external(sdcard)

    public boolean checkStorage(){
        boolean isExternalAvialable;
        boolean isExternalWriteable;
        String state = Environment.getExternalStorageState();
        switch (state){
            case Environment.MEDIA_MOUNTED:
                isExternalAvialable = isExternalWriteable = true;
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                isExternalAvialable = true;
                isExternalWriteable = false;
                break;
            default:
                isExternalAvialable = isExternalWriteable = false;
        }
        return isExternalAvialable && isExternalWriteable;
    }
    // use internal storage if internal memory is less than 80% memory



    // use external storage if internal memory got fill

    // transfer data from internal to external

    // transfer data from external to internal



}

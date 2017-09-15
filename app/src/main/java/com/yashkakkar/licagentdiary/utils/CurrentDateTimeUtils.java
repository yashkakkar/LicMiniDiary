package com.yashkakkar.licagentdiary.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Yash Kakkar on 13-06-2017.
 */

public class CurrentDateTimeUtils {

    /* date format -->
    (STANDARD)           22/07/2017
    (STANDARD_TABLE)     7-Jun-2017
    (STANDARD_TEXT_1)    7th June, 2017
    (STANDARD_TEXT_2)    7, Feb 2017
    */
    /* time format -->
    (STANDARD)           07:00 AM      HR:MIN AM/PM
    (FULL)               07:00:00 PM   HR:MIN:SEC AM/PM
    */

    public static CurrentDateTimeUtils newInstance(){
        return new CurrentDateTimeUtils();
    }

    public CurrentDateTimeUtils(){

    }

    public String getCurrentDate(String format){
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(new Date());
    }

    public String getCurrentTime(String format){
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(new Date());
    }

}

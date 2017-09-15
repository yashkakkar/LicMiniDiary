package com.yashkakkar.licagentdiary.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Yash Kakkar on 13-06-2017.
 */

public class DateTimeUtils {

   public Date getDate(String date) throws ParseException {
      DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
      return sdf.parse(date);
   }

   public Calendar getCalenderFromDate(String date) throws ParseException {
      Calendar cal = Calendar.getInstance();
      cal.setTime(getDate(date));
      return cal;
   }

    public static Calendar getCalendar(Long dateTime) {
        Calendar cal = Calendar.getInstance();
        if (dateTime != null && dateTime != 0) {
            cal.setTimeInMillis(dateTime);
        }
        return cal;
    }

    // get date in string and return String date date,month year ex. 12 Aug, 2017
    public static String convertDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date1 = sdf.parse(date);
        //Calendar c = Calendar.getInstance();
        //c.setTime(sdf.parse(date));
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        return sdf1.format(date1);
    }

    public static String convertDateTime(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy K:mm a",Locale.ENGLISH);
        return date;
    }

}

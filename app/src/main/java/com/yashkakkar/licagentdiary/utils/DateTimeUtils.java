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

}

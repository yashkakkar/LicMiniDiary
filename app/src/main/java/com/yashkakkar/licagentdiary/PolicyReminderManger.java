package com.yashkakkar.licagentdiary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.yashkakkar.licagentdiary.receivers.OnAlarmReceiver;

import java.util.Calendar;

/**
 * Created by Yash Kakkar on 17-07-2017.
 */

public class PolicyReminderManger {

    private PolicyReminderManger(){

    }

    public static void setRemainder(Context context, String policyId, Calendar when){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, OnAlarmReceiver.class);
        intent.putExtra("policyId",policyId);
        intent.putExtra("when",when);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_ONE_SHOT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,when.getTimeInMillis(),pendingIntent);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pendingIntent);
        }
    }
}

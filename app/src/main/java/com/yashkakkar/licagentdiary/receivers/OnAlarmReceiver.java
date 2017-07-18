package com.yashkakkar.licagentdiary.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yashkakkar.licagentdiary.MemberProfileView;
import com.yashkakkar.licagentdiary.R;

import java.util.Calendar;

/**
 * Created by Yash Kakkar on 17-07-2017.
 */

public class OnAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context, MemberProfileView.class);
        String policyId = intent.getStringExtra("policyId");
        String when = intent.getStringExtra("when");

        intent1.putExtra("policyId",policyId);
        intent1.putExtra("when",when);

        PendingIntent pi = PendingIntent.getActivity(context,0, intent1,PendingIntent.FLAG_ONE_SHOT);
        // Build the notification object using a notification builder

        Notification policy = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.title_notifications))
                .setContentText("")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        // send the notification

        mgr.notify();

    }
}

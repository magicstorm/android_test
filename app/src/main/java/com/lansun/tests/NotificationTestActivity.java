package com.lansun.tests;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;


/**
 * Created by ly on 17/02/2017.
 */

public class NotificationTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("fuck");
        builder.setContentText("fuck u ass hole");
        builder.setSmallIcon(R.drawable.avatar);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.avatar));



        Notification notification = builder.build();

        /**
         * small icon is a must by default, if there is no large icon. small icon will show in the
         * place of large icon in OS version before lolipop. If OS version is higher, small icon won't
         * show in the place of large icon, and small icon is a must.
         */
        //remove small icon
        int smallIconId = getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
        notification.contentView.setViewVisibility(smallIconId, View.GONE);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}

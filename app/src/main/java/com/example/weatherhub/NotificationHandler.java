package com.example.weatherhub;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import androidx.annotation.RequiresApi;

public class NotificationHandler extends ContextWrapper {
    private NotificationManager manager;
    public static final String CHANNEL_HIGH_ID = "1";
    public static final String CHANNEL_LOW_ID = "2";
    private final String CHANNEL_HIGH_NAME = "HIGH CHANNEL";
    private final String CHANNEL_LOW_NAME = "LOW CHANNEL";

    public NotificationHandler(Context base) {
        super(base);
        createChannels();
    }

    public NotificationManager getManager() {
        if ((manager==null) && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O))
            manager = (NotificationManager) getSystemService(NotificationManager.class);
        return manager;
    }
    public void createChannels () {
        NotificationChannel highChannel = null;
        // Configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            highChannel = new NotificationChannel(CHANNEL_HIGH_ID, CHANNEL_HIGH_NAME, NotificationManager.IMPORTANCE_HIGH);
            highChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationChannel lowChanel = new NotificationChannel(CHANNEL_LOW_ID, CHANNEL_LOW_NAME, NotificationManager.IMPORTANCE_LOW);
            //Configuration
            lowChanel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(highChannel);
            getManager().createNotificationChannel(lowChanel);
        }
    }

    public Notification.Builder createNotification (String title, String msg, boolean priority) {
        if (Build.VERSION.SDK_INT >= 26) {
            if (priority) {
                return createNotificationChannels(title, msg, CHANNEL_HIGH_ID);
            }
            return createNotificationChannels(title, msg, CHANNEL_LOW_ID);
        }
        return createNotificationNoChannels(title, msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification.Builder createNotificationChannels (String title, String msg, String channel) {
        return new Notification.Builder(getApplicationContext(), channel)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.icon_weather_notify)
                .setAutoCancel(true);
    }

    private Notification.Builder createNotificationNoChannels (String title, String msg) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.icon_weather_notify)
                .setAutoCancel(true);
    }
}

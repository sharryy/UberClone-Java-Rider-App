package com.anonymous.uberedmtrider.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

import com.anonymous.uberedmtrider.R;

public class NotificationHelper extends ContextWrapper {

    private static final String ANONYMOUS_CHANNEL_ID = "com.anonymous.uberedmtrider.ANONYMOUS";
    private static final String ANONYMOUS_CHANNEL_NAME = "ANONYMOUS Channel";
    public NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        NotificationChannel anonymousChannel = new NotificationChannel(ANONYMOUS_CHANNEL_ID, ANONYMOUS_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        anonymousChannel.enableLights(true);
        anonymousChannel.enableVibration(true);
        anonymousChannel.setLightColor(Color.GREEN);
        anonymousChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(anonymousChannel);
    }

    public NotificationManager getManager() {
        if(manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    public Notification.Builder getANONYMOUSChannelNotification(String title, String body){

        return new Notification.Builder(getApplicationContext(), ANONYMOUS_CHANNEL_ID)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true);

    }

}


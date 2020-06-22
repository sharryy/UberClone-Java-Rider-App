package com.anonymous.uberedmtrider.Service;

import android.app.Notification;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.anonymous.uberedmtrider.Helper.NotificationHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Random;

import xdroid.toaster.Toaster;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    NotificationHelper helper;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification().getTitle().equals("Cancel")){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyFirebaseMessaging.this, ""+remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(remoteMessage.getNotification().getTitle().equals("Arrived")){

            showArrivedNotification(remoteMessage.getNotification().getBody());
            Toaster.toast("Hellooooooo");

        }
    }

    private void showArrivedNotification(String body) {
        helper = new NotificationHelper(this);
        Notification.Builder builder = helper.getANONYMOUSChannelNotification("Arrived",body);
        helper.getManager().notify(new Random().nextInt(), builder.build());
    }

}

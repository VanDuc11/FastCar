package com.example.fastcar;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseCloudMessageService extends FirebaseMessagingService {

    public String TAG = "zzzz";
    private static final String CHANNEL_ID = "FastCar";
    private static final String CHANNEL_NAME = "FastCar";

    public FireBaseCloudMessageService() {

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        RemoteMessage.Notification notification = message.getNotification();
        String title = notification.getTitle();
        String body = notification.getBody();
        String url = String.valueOf(notification.getImageUrl());

        createNotificationChannel(title, body, BitmapFactory.decodeFile(url));
    }

    @SuppressLint("MissingPermission")
    private void createNotificationChannel(String title, String message, Bitmap url) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelDescription = "FastCars";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_fast_car_30x30)
                .setContentTitle(title)
                .setContentText(message)
//                .setStyle( new NotificationCompat.BigPictureStyle()
//                        .bigPicture(url).bigLargeIcon(null))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}

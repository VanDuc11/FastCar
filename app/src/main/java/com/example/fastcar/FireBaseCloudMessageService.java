package com.example.fastcar;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fastcar.Activity.ChuXe.HoaDon_ChuSH_Activity;
import com.example.fastcar.Activity.KhachHang.HoaDon_Activity;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Model.HoaDon;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class FireBaseCloudMessageService extends FirebaseMessagingService {

    public String TAG = "zzzz";
    private static final String CHANNEL_ID = "FastCar";
    private static final String CHANNEL_NAME = "FastCar";

    public FireBaseCloudMessageService() {

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
//        RemoteMessage.Notification notification = message.getNotification();
//        String title = notification.getTitle();
//        String body = notification.getBody();
        String title = message.getData().get("title");
        String body = message.getData().get("body");
        String hoaDonKHStr = message.getData().get("hoadonKH");
        String hoaDonCXStr = message.getData().get("hoadonCX");
        HoaDon hoaDonKH = null;
        HoaDon hoaDonCX = null;
        PendingIntent pendingIntent = null;
        try {
            Gson gson = new Gson();
            if (hoaDonKHStr.length() != 0) {
                hoaDonKH = gson.fromJson(hoaDonKHStr, HoaDon.class);
                Intent intent = new Intent(this, HoaDon_Activity.class);
                intent.putExtra("hoadon", hoaDonKH);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            }
            if (hoaDonCXStr.length() != 0) {
                hoaDonCX = gson.fromJson(hoaDonCXStr, HoaDon.class);
                Intent intent = new Intent(this, HoaDon_ChuSH_Activity.class);
                intent.putExtra("hoadon", hoaDonCX);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            }

        } catch (Exception ignored) {
        }

        createNotificationChannel(title, body, pendingIntent);
    }

    @SuppressLint("MissingPermission")
    private void createNotificationChannel(String title, String message, PendingIntent pendingIntent) {

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
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}

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

import com.example.fastcar.Activity.ChuXe.ChiTietXeCuaToi_Activity;
import com.example.fastcar.Activity.ChuXe.HoaDon_ChuSH_Activity;
import com.example.fastcar.Activity.KhachHang.HoaDon_Activity;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Model.Car;
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
        String title = message.getData().get("title");
        String body = message.getData().get("body");

        if (message.getData().containsKey("hoadonKH")) {
            String hoaDonKHStr = message.getData().get("hoadonKH");
            PendingIntent pendingIntent = createPendingIntent(hoaDonKHStr, "", HoaDon_Activity.class);
            createNotificationChannel(title, body, pendingIntent);
        } else if (message.getData().containsKey("hoadonCX")) {
            String hoaDonCXStr = message.getData().get("hoadonCX");
            PendingIntent pendingIntent = createPendingIntent(hoaDonCXStr, "", HoaDon_ChuSH_Activity.class);
            createNotificationChannel(title, body, pendingIntent);
        } else if (message.getData().containsKey("xe")) {
            String XeStr = message.getData().get("xe");
            PendingIntent pendingIntent = createPendingIntent("", XeStr, ChiTietXeCuaToi_Activity.class);
            createNotificationChannel(title, body, pendingIntent);
        } else {
            createNotification_NoClicked(title, body);
        }

    }


    private PendingIntent createPendingIntent(String hoaDonStr, String carStr, Class<?> activityClass) {
        HoaDon hoaDon = null;
        Car car = null;
        if (hoaDonStr.length() != 0) {
            try {
                Gson gson = new Gson();
                hoaDon = gson.fromJson(hoaDonStr, HoaDon.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (carStr.length() != 0) {
            try {
                Gson gson = new Gson();
                car = gson.fromJson(carStr, Car.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (hoaDon != null) {
            Intent intent = new Intent(this, activityClass);
            intent.putExtra("hoadon", hoaDon);
            return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }

        if (car != null) {
            Intent intent = new Intent(this, activityClass);
            intent.putExtra("car", car);
            return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }
        return null;
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
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }


    @SuppressLint("MissingPermission")
    private void createNotification_NoClicked(String title, String message) {
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
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }


}

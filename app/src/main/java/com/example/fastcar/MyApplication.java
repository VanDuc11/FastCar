package com.example.fastcar;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fastcar.Socket.SocketManager;

public class MyApplication extends Application {
    private static SocketManager socketManager;
    private static boolean isAppInForeground;

    @Override
    public void onCreate() {
        super.onCreate();
        socketManager = SocketManager.getInstance();
        socketManager.connect();

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                isAppInForeground = true;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                isAppInForeground = true;
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                isAppInForeground = false;
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
            }
        });
    }

    public static SocketManager getSocketManager() {
        return socketManager;
    }

    public static boolean isAppInForeground() {
        return isAppInForeground;
    }
}

package com.example.fastcar;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fastcar.Socket.SocketManager;

public class MyApplication extends Application {
    private static SocketManager socketManager;

    @Override
    public void onCreate() {
        super.onCreate();
        socketManager = SocketManager.getInstance();
        socketManager.connect();
    }

    public static SocketManager getSocketManager() {
        return socketManager;
    }
}

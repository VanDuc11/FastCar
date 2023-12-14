package com.example.fastcar.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    private static boolean isScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            isScreenOn = true;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            isScreenOn = false;
        }
    }

    public static boolean isAppInForeground() {
        return isScreenOn;
    }
}

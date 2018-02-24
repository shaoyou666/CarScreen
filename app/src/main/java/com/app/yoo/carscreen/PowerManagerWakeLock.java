package com.app.yoo.carscreen;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * Created by csyoo on 2016/12/16.
 */

public class PowerManagerWakeLock {

    private static WakeLock wakeLock;
    /**开启 保持屏幕唤醒*/
    public static void acquire(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PowerManagerWakeLock");
        wakeLock.acquire();
    }

    /**关闭 保持屏幕唤醒*/
    public static void release() {
        wakeLock.release();
    }
}

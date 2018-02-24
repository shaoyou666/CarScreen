package com.app.yoo.carscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by csyoo on 2016/12/16.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    Intent i =new Intent();
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setClass(context, FullscreenActivity.class);
                    context.startActivity(i);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

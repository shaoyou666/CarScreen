package com.app.yoo.carscreen;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Created by csyoo on 2016/12/16.
 */

public class CarscreenService extends Service {

    //private static KeyguardManager.KeyguardLock lock;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //屏蔽系统的屏保
        //KeyguardManager manager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        //lock = manager.newKeyguardLock("KeyguardLock");
        //lock.disableKeyguard();

        // 注册一个监听屏幕开启和关闭的广播
        IntentFilter filter = new IntentFilter();
        //filter.addAction(Intent.ACTION_SCREEN_ON);
        //filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction("TimerClosed");
        registerReceiver(screenOffReceiver,filter);
    }
    //接收关屏广播
    BroadcastReceiver screenOffReceiver=new  BroadcastReceiver() {
        public void onReceive(final Context context, Intent intent){
            String action = intent.getAction();
            if(action.equals("TimerClosed") && FullscreenActivity.isKeepRun){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(30000);
                            if(!FullscreenActivity.isActive){
                                Intent i =new Intent();
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.setClass(context, FullscreenActivity.class);
                                context.startActivity(i);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            /*
            try{
                PowerManagerWakeLock.acquire(context);
                Intent i =new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setClass(context, FullscreenActivity.class);
                context.startActivity(i);
            }catch(Exception e){
                //Log.i("Output:", e.toString());
                e.printStackTrace();
            }
            */
        }
    };
    @Override
    public void onDestroy() {
        //PowerManagerWakeLock.release();
        unregisterReceiver(screenOffReceiver);
        //lock.reenableKeyguard();
    }

}

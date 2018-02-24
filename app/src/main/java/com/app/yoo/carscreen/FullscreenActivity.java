package com.app.yoo.carscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FullscreenActivity extends AppCompatActivity implements Runnable {

    private Handler handler;
    private TextView tv_time,tv_date;
    private CheckBox cb_keepRun;
    public  static  boolean isKeepRun;
    public static boolean isActive;
    private SharedPreferences settings;

    private View mContentView;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        settings = getSharedPreferences("settings",MODE_PRIVATE);
        isKeepRun = settings.getBoolean("isKeepRun",false);

        cb_keepRun = (CheckBox) findViewById(R.id.cb_keepRun);
        cb_keepRun.setChecked(isKeepRun);
        cb_keepRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isKeepRun = isChecked;
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("isKeepRun",isKeepRun);
                editor.commit();
            }
        });
        mContentView = findViewById(R.id.ll_content);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("TimerClosed");
                FullscreenActivity.this.sendBroadcast(intent);
                finish();
            }
        });

        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_date = (TextView)findViewById(R.id.tv_date);
        handler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what==100)
                    tv_time.setText((String)msg.obj);
                if(msg.what==200)
                    tv_date.setText((String)msg.obj);
            }
        };

        new Thread(this).start();

        startService(new Intent(this,CarscreenService.class));

    }

    @Override
    public void run() {
        try {
            while (true) {

                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                //SimpleDateFormat date = new SimpleDateFormat("yyyy年MM月dd日");
                String str_time = time.format(new Date());
                /*String str_date = date.format(new Date());
				handler.sendMessage(handler.obtainMessage(100,str_time));//100 for time
				handler.sendMessage(handler.obtainMessage(200,str_date));//200 for time
				*/
                Calendar cal = Calendar.getInstance();
                //获取年份
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;//获取月份
                int day = cal.get(Calendar.DATE);//获取日
                //int hour=cal.get(Calendar.HOUR);//小时
                //int minute=cal.get(Calendar.MINUTE);//分
                //int second=cal.get(Calendar.SECOND);//秒
                int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK) - 1;//一周的第几天
                //System.out.println("现在的时间是：公元"+year+"年"+month+"月"+day+"日      "+hour+"时"+minute+"分"+second+"秒       星期"+WeekOfYear);
                CalendarUtil c = new CalendarUtil();
                String ChineseDay = c.getChineseDay(year, month, day);
                String ChineseMonth = c.getChineseMonth(year, month, day);
                handler.sendMessage(handler.obtainMessage(100, str_time));//100 for time
                handler.sendMessage(handler.obtainMessage(200, year + "年" + month + "月" + day + "日    星期" + getWeek(WeekOfYear) + "   农历" + ChineseMonth + ChineseDay));//200 for time
                //getMusicInfo();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getWeek(int w){
        switch(w){
            case 1: return "一";
            case 2: return "二";
            case 3: return "三";
            case 4: return "四";
            case 5: return "五";
            case 6: return "六";
            case 7: return "天";
            default:return "";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive  = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.setAction("TimerClosed");
        FullscreenActivity.this.sendBroadcast(intent);
    }
}

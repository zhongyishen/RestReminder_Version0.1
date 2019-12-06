package com.szy.restreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity {
    Chronometer chronometer;
    Button start, end;
    //    boolean flag = true;
    AlertDialog alertDialog_end, alertDialog_rest;
    long record_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        //获取控件
        chronometer = (Chronometer) findViewById(R.id.timer);
        start = (Button) findViewById(R.id.start_work);
        end = (Button) findViewById(R.id.end_work);

        //添加逻辑
        /*
        点击开始工作，计时器开始计时
        计时到30分钟时，弹出对话框，提醒用户休息，对话框持续时间为1分钟
        点击休息，提醒用户闭上眼睛，播放音乐，同时重置计时器
        计时到3分钟时，音乐停止,计时器重置，继续计时
        点击结束工作，结束大循环，计时器重置。
         */

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (record_time != 0) {
                    chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - record_time);
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
                chronometer.start();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂停时钟，并弹出对话框警告
                chronometer.stop();
                record_time = SystemClock.elapsedRealtime();
                //创建对话框
                alertDialog_end = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("退出程序")
                        .setMessage("是否结束现阶段的工作")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create();
                alertDialog_end.show();
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) > 30 * 1000) {
                    chronometer.stop();
                    alertDialog_rest = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("您的助手来啦！")
                            .setMessage("闭上眼睛休息吧")
                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).create();
                    alertDialog_rest.show();
                }
            }
        });
    }
}

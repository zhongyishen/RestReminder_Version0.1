package com.szy.restreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.io.IOException;

/*
   点击开始工作，计时器开始计时
   计时到30分钟时，弹出对话框，提醒用户休息，对话框持续时间为1分钟
   点击休息，提醒用户闭上眼睛，播放音乐，同时重置计时器
   计时到3分钟时，音乐停止,计时器重置，继续计时
   点击结束工作，结束大循环，计时器重置。
*/

public class MainActivity extends AppCompatActivity {
    //计时器
    Chronometer chronometer;
    //按钮
    Button start, end;
    //弹框
    AlertDialog alertDialog_end, alertDialog_rest;
    //暂停时走过的时间
    long record_time = 0;
    //音乐线程
    Music music = new Music();
    Minder minder = new Minder();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        //获取控件
        chronometer = (Chronometer) findViewById(R.id.timer);
        start = (Button) findViewById(R.id.start_work);
        end = (Button) findViewById(R.id.end_work);

        //添加逻辑


        //开始工作
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

        //结束工作
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

        //弹窗1
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) > 3 * 1000) {
                    chronometer.stop();
                    alertDialog_rest = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("猫咪助手来啦(>^ω^<)！")
                            .setMessage("闭上眼睛休息吧")
                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    music.start();
                                    return;
                                }
                            }).create();
                    alertDialog_rest.show();
                    minder.start();
                }
            }
        });
    }

    class Minder extends Thread{
        @Override
        public void run(){
            super.run();
            AssetManager assetManager;
            MediaPlayer player = new MediaPlayer();
            assetManager = getResources().getAssets();
            try {
                AssetFileDescriptor fileDescriptor = assetManager.openFd("minder.mp3");
                player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Music extends Thread{
        @Override
        public void run(){
            super.run();
            AssetManager assetManager;
            MediaPlayer player = new MediaPlayer();
            assetManager = getResources().getAssets();
            try {
                AssetFileDescriptor fileDescriptor = assetManager.openFd("music.wav");
                player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




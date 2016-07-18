package com.example.administrator.musicplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.SeekBar;


public class MainActivity extends Activity {

    private Iservice iservice;//定义的Binder对象
    private MyConn conn;
    private static SeekBar sbar;

    public static Handler handler = new Handler(){
        //当接收到消息该方法执行
        @Override
        public void handleMessage(Message msg) {
            //获取msg携带的数据
            Bundle data = msg.getData();
            //获取当前的进度和总进度
            int duration = data.getInt("duration");
            int currentPosition = data.getInt("currentPosition");

            //设置seekBar的最大进度和当前进度
            sbar.setMax(duration);//设置最大进度
            sbar.setProgress(currentPosition);//设置当前进度
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sbar = (SeekBar) findViewById(R.id.seekBar);

        //先调用startService 方法开启服务 保证服务在后台运行
        Intent intent = new Intent(this,MusicService.class);
        startService(intent);
        //1.调用bindservice 获取定义的Binder类对象

        conn = new MyConn();

        //连接Music服务，获取Binder对象
        bindService(intent,conn,BIND_AUTO_CREATE);

        //给seekbar设置监听
        sbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //设置播放的位置
                iservice.callSeekToPosition(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //点击按钮播放
    public void click1(View v){
        iservice.callPlayMusic();
    }

    //点击按钮暂停
    public void click2(View v){
        iservice.callPauseMusic();
    }

    //点击按钮继续播放
    public void click3(View v){
        iservice.callRePlayMusic();
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

    //当连接成功时调用
    private class MyConn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取Binder类对象
            iservice = (Iservice) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}

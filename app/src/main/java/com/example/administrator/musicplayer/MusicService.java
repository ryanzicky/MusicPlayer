package com.example.administrator.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class MusicService extends Service {

    private MediaPlayer player;
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    //服务第一次开启的时候调用
    @Override
    public void onCreate() {
        player = new MediaPlayer();
        super.onCreate();
    }

    //当服务销毁的时候调用
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //设置播放音乐指定位置的方法
    public void seekToPosition(int position){
        player.seekTo(position);
    }

    //播放音乐
    public void playMusic(){
        System.out.println("播放音乐");

        try{
            player.reset();
            //设置要播放的资源
            player.setDataSource("/storage/sdcard/xpg.mp3");
            //准备播放
            player.prepare();
            //开始播放
            player.start();

            //更新进度条

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateSeekBar(){
        //获取歌曲总时长
        final int duration = player.getDuration();
        //获取当前歌曲进度(一秒钟获取一次)
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //获取当前歌曲进度
                int currentPosition = player.getCurrentPosition();

                //创建message对象
                Message msg = Message.obtain();
                //使用msg携带多个数据
                Bundle bundle = new Bundle();
                bundle.putInt("duration",duration);
                bundle.putInt("currentPosition",currentPosition);
                msg.setData(bundle);
                //发送消息
                MainActivity.handler.sendMessage(msg);
            }
        };

        timer.schedule(task,300,1000);
    }

    //暂停播放音乐
    public void pauseMusic(){
        System.out.println("暂停播放");

        player.pause();
    }

    //播放音乐
    public void rePlayMusic(){
        System.out.println("继续播放");

        player.start();
    }

    //定义一个中间人对象
    public class MyBinder extends Binder implements Iservice{

        //调用播放音乐的方法
        @Override
        public void callPlayMusic() {
            playMusic();
        }

        //调用暂停的方法
        @Override
        public void callPauseMusic() {
            pauseMusic();
        }

        //调用继续播放
        @Override
        public void callRePlayMusic() {
            rePlayMusic();
        }

        //调用
        @Override
        public void callSeekToPosition(int position) {
            seekToPosition(position);
        }
    }
}

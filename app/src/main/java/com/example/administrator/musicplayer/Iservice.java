package com.example.administrator.musicplayer;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public interface Iservice {
    public void callPlayMusic();
    public void callPauseMusic();
    public void callRePlayMusic();
    public void callSeekToPosition(int position);
}

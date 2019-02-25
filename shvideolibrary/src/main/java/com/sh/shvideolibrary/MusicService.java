package com.sh.shvideolibrary;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import static android.media.AudioManager.STREAM_MUSIC;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener{

    //绑定service时返回的bind对象
    private IBinder localBinder = new LocalBinder();
    //播放器
    private MediaPlayer mediaPlayer;
    //音乐是否准备好
    private boolean isPrepared = false;
    public void setMusicUrl(int index) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(Config.httpUrl +"/static/song" + index +".mp3");
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("yxs","create");
        //初始化播放器,这些操作建议放在onStartCommand方法中
//        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bie);
        mediaPlayer = new MediaPlayer();
        //设置音乐循环播放
        setMusicUrl(1);
        mediaPlayer.setLooping(true);
        ////监听音乐是否准备好,需要实现onPrepared方法
        mediaPlayer.setOnPreparedListener(MusicService.this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("yxs","onStartCommand");
        return START_STICKY;
    }

    /**
     * 播放音乐
     */
    public void playerStart(int index){
        //判断是否准备好
        if(isPrepared) {
            //判断播放器是否为null，和是否正在播放
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(index);
                mediaPlayer.start();
            }
        }
    }

    /**
     * 音乐暂停
     */
    public void playerStop(){
        //如果当前正在播放音乐,则暂停
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        //如果准备就绪,则isPrepared 赋值为真
        isPrepared = true;
        Log.i("yxs","是否准备好"+isPrepared);
    }

    //内部类,返回service实例
    public class LocalBinder  extends Binder {
        public MusicService getLocalService(){
            Log.i("yxs","构造");
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("yxs","onBind");
//返回Binder对象,以便在页面中通过binder获取service实例,操作音乐播放器
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("yxs","onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i("yxs","onDestroy");
        stopSelf();
        super.onDestroy();
    }
}
package com.thtfit.test;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * Created by tommy on 2017/06/08
 */
public class MusicService extends Service {

    public final IBinder binder = new MyBinder();
    Callback callback;
    public class MyBinder extends Binder{
        MusicService getService() {
            return MusicService.this;
        }
    }
    public static MediaPlayer mp = new MediaPlayer();
    @Override
    public void onCreate() {
    	super.onCreate();
    	initService();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	// TODO Auto-generated method stub
    	if (!mp.isPlaying()) {
			mp.start();
		}
    	
    	return super.onStartCommand(intent, flags, startId);
    }
    
    
    public void setCallback(Callback callback) {
		this.callback = callback;
	}
    
    public void initService() {
        try {
        	AssetFileDescriptor fileDescriptor = this.getAssets().openFd("music.mp3");
        	mp = new MediaPlayer();           
        	mp.setDataSource(fileDescriptor.getFileDescriptor(),                 
							fileDescriptor.getStartOffset(),fileDescriptor.getLength());
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer arg0) {
					callback.onFinish();
				}
			});
        } catch (Exception e) {
            Log.d("hint","can't get to the song");
            e.printStackTrace();
        }
    }
    public void playOrPause() {
        if(mp.isPlaying()){
            mp.pause();
        } else {
            mp.start();
        }
    }
    public void stop() {
        if(mp != null) {
            mp.stop();
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    
    interface Callback{
    	void onFinish();
    }
}

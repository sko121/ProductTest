package com.thtfit.test;

import java.io.File;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.MediaController;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.VideoView;

import com.thtfit.test.R;

/**
 * Created by tommy on 2017/06/08
 */

public class VideoActivity extends Activity {

	private VideoView mVideoView;
	MediaController mMediaController;
	private int  mPositionWhenPaused;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_video);
		mVideoView = (VideoView) findViewById(R.id.video1);
		mMediaController = new MediaController(this);
		String uri = "android.resource://" + getPackageName() + "/"
				+ R.raw.video;
		mVideoView.setVideoURI(Uri.parse(uri));
		mVideoView.setMediaController(mMediaController);
		mMediaController.setMediaPlayer(mVideoView);
		mVideoView.requestFocus();
		mVideoView.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {
				endtest();
//	            mVideoView.seekTo(0);  
//	            mVideoView.start();
			}
		});
		mVideoView.start();
	}
	
	public void onPause() {  
        mPositionWhenPaused = mVideoView.getCurrentPosition();  
        mVideoView.stopPlayback();  
        super.onPause();  
    }  
  
    public void onResume() {  
        if(mPositionWhenPaused >= 0) {  
            mVideoView.seekTo(mPositionWhenPaused);  
            mVideoView.start();
            mPositionWhenPaused = -1;  
        }  
        super.onResume();  
    }

	private void endtest() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				VideoActivity.this.setResult(Mainacitivity.VIDEO);
				VideoActivity.this.finish();
			}
		}, 2000);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
//		endtest();
	}
	

}

package com.thtfit.test;


import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;



public class VideoActivity extends Activity {
	private VideoView videoView ;
	private String uri;
	 
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_video); 	   
	    videoView = (VideoView)this.findViewById(R.id.videoView );	 
	    //设置视频控制器
	    videoView.setMediaController(new MediaController(this));    
	    uri = "android.resource://" + getPackageName() + "/" + R.raw.heart;
	    videoView.setVideoURI(Uri.parse(uri));
	    videoView.start();
	    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
	    	  
            @Override  
            public void onCompletion(MediaPlayer mp) {  
            	 videoView.setVideoURI(Uri.parse(uri));  
            	videoView.start();  

            }  
        });  
	  }
	 
	 
	  
	}



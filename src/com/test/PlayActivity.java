package com.test;   

import android.app.Activity;
import android.app.AlertDialog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FilenameFilter;   
import java.io.IOException;   
import java.util.ArrayList;   
import java.util.List;   
import android.app.ListActivity;   
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;   
import android.media.MediaPlayer.OnCompletionListener;   
import android.os.Bundle;   
import android.util.Log;
import android.view.KeyEvent;   
import android.view.View;   
import android.widget.TextView;
import android.widget.ArrayAdapter;   
import android.widget.Button;   
import android.widget.ListView;   


public class PlayActivity extends Activity  
{   
	private final static String MUSIC_SUCCESS = "音乐测试成功";
	private final static String MUSIC_FIALED = "音乐测试失败";
	private int defaultColor = Color.BLACK;
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
    private Button mStopButton, mStartButton, mPauseButton;   
	private Button confirmButton;
	private TextView mText;
	private final String MUSIC_FILE = "/mnt/sdcard/music.mp3";
    private MediaPlayer mMediaPlayer;   
	private ProductTest mProduct;
	private AlertDialog mDialog = null;
//	private AssetFileDescriptor mAssetFile;
//	private FileDescriptor mFile;
    /** Called when the activity is first created. */   
    @Override   
    public void onCreate(Bundle savedInstanceState)   
    {   
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.play);   
		mProduct = (ProductTest)getApplication();
		mText = (TextView)findViewById(R.id.message1);
        mStopButton = (Button) findViewById(R.id.StopButton);   
        mStartButton = (Button) findViewById(R.id.StartButton);    
        //mPauseButton = (Button) findViewById(R.id.PauseButton);   
		confirmButton = (Button)findViewById(R.id.button_confirm);

        mMediaPlayer = MediaPlayer.create(PlayActivity.this, R.raw.music);   
   		mMediaPlayer.setOnCompletionListener(new OnCompletionListener()    
		{   
			public void onCompletion(MediaPlayer arg0)   
			{   
				Log.d("playmusic", "play over");
			}   
    	});   

		confirmButton.setOnClickListener(new Button.OnClickListener()
		{
            @Override   
            public void onClick(View v){   
				showConfirmDialog();
			}
		});
        mStopButton.setOnClickListener(new Button.OnClickListener()    
        {   
            @Override   
            public void onClick(View v){   
                if (mMediaPlayer.isPlaying())   
                {   
        			try{  
                    	mMediaPlayer.stop();   
						mMediaPlayer.prepare();
					//mPauseButton.setText("继续");
        			}catch (Exception e){}   
                }   
            }   
        });    
        mStartButton.setOnClickListener(new Button.OnClickListener()    
        {   
            @Override   
            public void onClick(View v){   
				mMediaPlayer.seekTo(0);
            	mMediaPlayer.start();   
				//mPauseButton.setText("暂停");
            }   
        });     
	/*
        mPauseButton.setOnClickListener(new Button.OnClickListener()    
        {   
            public void onClick(View view)   
            {   
                if (mMediaPlayer.isPlaying()){   
                    mMediaPlayer.pause();   
					mPauseButton.setText("继续");
                }else{   
                    mMediaPlayer.start();   
					mPauseButton.setText("暂停");
                }   
            }   
        });   */
    }
	@Override
	public void onStop(){
		super.onStop();
		mMediaPlayer.stop();   
        mMediaPlayer.release();   
        this.finish();   
	}

	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle("音乐")
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mText.setText(MUSIC_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(11, MUSIC_SUCCESS);
				}
			})
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(MUSIC_FIALED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(11, MUSIC_FIALED);
				}
			})
			.create();
		mDialog.setMessage("播放音乐是否测试成功");
		mDialog.show();
	}
}   

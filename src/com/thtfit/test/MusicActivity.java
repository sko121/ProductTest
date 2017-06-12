package com.thtfit.test;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thtfit.test.MusicService.Callback;
import com.thtfit.test.R;

import java.text.SimpleDateFormat;

/**
 * Created by tommy on 2017/06/08
 */

public class MusicActivity extends Activity implements View.OnClickListener {

	private MusicService musicService;
	private SeekBar seekBar;
	private TextView musicStatus, musicTime;
	private Button btnPlayOrPause, btnStop, btnQuit;
	private SimpleDateFormat time = new SimpleDateFormat("m:ss");
	private ServiceConnection sc = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder iBinder) {
			musicService = ((MusicService.MyBinder) iBinder).getService();
			musicService.setCallback(new Callback() {
				@Override
				public void onFinish() {
					endtest();
				}
			});
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			musicService = null;
		}
	};

	private void bindServiceConnection() {
		Intent intent = new Intent(MusicActivity.this, MusicService.class);
		startService(intent);
		bindService(intent, sc, this.BIND_AUTO_CREATE);
	}

	public android.os.Handler handler = new android.os.Handler();
	public Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (musicService.mp.isPlaying()) {
				musicStatus.setText(getResources().getString(R.string.plays));
				btnPlayOrPause.setText(getResources().getString(R.string.pause)
						.toUpperCase());
			} else {
				musicStatus.setText(getResources().getString(R.string.pause));
				btnPlayOrPause.setText(getResources().getString(R.string.plays)
						.toUpperCase());
			}
			musicTime.setText(time.format(musicService.mp.getCurrentPosition())
					+ "/" + time.format(musicService.mp.getDuration()));
			seekBar.setMax(musicService.mp.getDuration());
			seekBar.setProgress(musicService.mp.getCurrentPosition());
			seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					if (fromUser) {
						musicService.mp.seekTo(seekBar.getProgress());
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {

				}
			});
			handler.postDelayed(runnable, 100);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("fallwater", "MusicActivity_onCreate");
		setContentView(R.layout.layout_music);
		bindServiceConnection();
		musicStatus = (TextView) this.findViewById(R.id.MusicStatus);
		musicTime = (TextView) this.findViewById(R.id.MusicTime);
		btnPlayOrPause = (Button) this.findViewById(R.id.BtnPlayorPause);
		seekBar = (SeekBar) this.findViewById(R.id.MusicSeekBar);

		musicService.mp.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer player) {
				// TODO Auto-generated method stub
				seekBar.setMax(player.getDuration());
				Log.i("fallwater", "onResume_musicService.mp.getDuration()"
						+ player.getDuration());
			}
		});
		handler.post(runnable);
	}

	@Override
	protected void onResume() {
		if (musicService.mp.isPlaying()) {
			musicStatus.setText(getResources().getString(R.string.playing));
		} else {
			musicStatus.setText(getResources().getString(R.string.pause));
		}
		super.onResume();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.BtnPlayorPause:
			musicService.playOrPause();
			break;
		case R.id.BtnStop:
//			musicService.stop();
//			seekBar.setProgress(0);
			break;
		case R.id.BtnQuit:
//			musicService.stop();
//			handler.removeCallbacks(runnable);
//			unbindService(sc);
//			endtest();
//			try {
//				System.exit(0);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		musicService.stop();
		seekBar.setProgress(0);
		unbindService(sc);
		super.onPause();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void endtest() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MusicActivity.this.setResult(Mainacitivity.MUSIC);
				MusicActivity.this.finish();
			}
		}, 2000);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		if (musicService!= null&&seekBar!=null) {
			musicService.stop();
			seekBar.setProgress(0);
			unbindService(sc);
			endtest();
		}
	}
}

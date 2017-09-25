package com.thtfit.test;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.thtfit.test.R;

/**
 * Created by tommy on 2017/06/08
 */

public class Mainacitivity extends Activity {
	public static final int RAM=0;
	public static final int NAND=1;
	public static final int BATTERY=2;
	public static final int NETWORK=3;
	public static final int BARCODE=4;
	public static final int MUSIC=5;
	public static final int VIDEO=6;
	public static final int SHOW=7;
	public static final String LOG_TAG = "fallwater";
	
	private TextView mShowTextView;
	private TextView mRam;
	private TextView mNand;
	private TextView mBattery;
	private TextView mBarcode;
	private TextView mNetWork;
	private TextView mMusic;
	private TextView mVideo;
	private Handler mHandler = new Handler();
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.layout_mainactivity);
			initView();
			requestAllPermissons();
			beginTest();
		}

		private void requestAllPermissons() {
			if (ContextCompat.checkSelfPermission(ProductTest.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
	        } else {
	            ActivityCompat.requestPermissions(this , new String[]{
	            		Manifest.permission.WRITE_EXTERNAL_STORAGE, 
	            		Manifest.permission.READ_EXTERNAL_STORAGE,
	            		}, 1);
	        }
		}
		
		@Override
		public void onRequestPermissionsResult(int requestCode,
				String[] permissions, int[] grantResults) {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			switch (requestCode) {
			case 1:
				if (grantResults.length > 0
		                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
		            } else {
		            }
				break;
			default:
				break;
			}
		}

		private void beginTest() {
				mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mShowTextView.setText("now begin RAM test:");
					startActivityForResult(new Intent(Mainacitivity.this, MemoryActivity.class),0);
				}
			}, 4000);
		}

		private void initView() {
			mShowTextView = (TextView)findViewById(R.id.showtextview);
			mRam = (TextView)findViewById(R.id.ram);
			mNand = (TextView)findViewById(R.id.nand);
			mBattery = (TextView)findViewById(R.id.battery);
			mNetWork = (TextView)findViewById(R.id.network);
			mBarcode = (TextView)findViewById(R.id.barcode);
			mMusic = (TextView)findViewById(R.id.music);
			mVideo = (TextView)findViewById(R.id.video);
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode==0) {
				switch (resultCode) {
				case RAM:	
					mShowTextView.setText("RAM test successfully ,now begin to test NAND");
					mRam.setTextColor(Color.GREEN);
					mHandler.removeCallbacks(mRunnable);
					mRunnable =new Runnable() {
						@Override
						public void run() {
							startActivityForResult(new Intent(Mainacitivity.this, NandflashActivity.class),0);
						}
					} ;
					mHandler.postDelayed(mRunnable, 4000);
					break;
				case NAND:	
					mShowTextView.setText("NAND test successfully ,now begin to test BATTERY");
					mNand.setTextColor(Color.GREEN);
					mHandler.removeCallbacks(mRunnable);
					mRunnable = new Runnable() {
						@Override
						public void run() {
							startActivityForResult(new Intent(Mainacitivity.this, BatteryActivity.class),0);
						}
					};
					mHandler.postDelayed(mRunnable, 4000);
					break;
				case BATTERY:
					mShowTextView.setText("BATTERY test successfully ,now begin to test reading QR code");
					mBattery.setTextColor(Color.GREEN);
					mHandler.removeCallbacks(mRunnable);
					mRunnable = new Runnable() {
						
						@Override
						public void run() {
							startActivityForResult(new Intent(Mainacitivity.this, MactobarcodeActivity.class),0);
						}
					};
					mHandler.postDelayed(mRunnable, 4000);
					
					break;
				case NETWORK:
					mShowTextView.setText("NETWORK test successfully ,now begin to test reading QR code");
					mBattery.setTextColor(Color.GREEN);
					mHandler.removeCallbacks(mRunnable);
					mRunnable =new Runnable() {
						@Override
						public void run() {
							startActivityForResult(new Intent(Mainacitivity.this, MactobarcodeActivity.class),0);
						}
					} ;
					mHandler.postDelayed(mRunnable, 4000);
					break;
				case BARCODE:
					mShowTextView.setText("BARCODE test successfully ,now begin to test play MUSIC");
					mBarcode.setTextColor(Color.GREEN);
					mHandler.removeCallbacks(mRunnable);
					mRunnable =new Runnable() {
						@Override
						public void run() {
							startActivityForResult(new Intent(Mainacitivity.this, MusicActivity.class),0);
						}
					} ;
					mHandler.postDelayed(mRunnable, 4000);
					break;
				case MUSIC:
					mShowTextView.setText("MUSIC test successfully ,now begin to test play VIDEO");
					mMusic.setTextColor(Color.GREEN);
					mHandler.removeCallbacks(mRunnable);
					mRunnable = new Runnable() {
						@Override
						public void run() {
							startActivityForResult(new Intent(Mainacitivity.this, VideoActivity.class),0);
						}
					};
					mHandler.postDelayed(mRunnable, 4000);
					break;
				case VIDEO:
					mShowTextView.setText("VIDEO test successfully");
					mVideo.setTextColor(Color.GREEN);
					testNextTime();
					break;
				default:
					break;
				}
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

		private void testNextTime() {
			mHandler.removeCallbacks(mRunnable);
			mRunnable = new Runnable(){
				@Override
				public void run() {
					mShowTextView.setText("all test items test successly ,now begin to test from begining ...");
					mRam.setTextColor(Color.WHITE);
					mNand.setTextColor(Color.WHITE);
					mBattery.setTextColor(Color.WHITE);
					mNetWork.setTextColor(Color.WHITE);
					mBarcode.setTextColor(Color.WHITE);
					mMusic.setTextColor(Color.WHITE);
					mVideo.setTextColor(Color.WHITE);
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							beginTest();
						}
					}, 2000);
				}
			};
			mHandler.postDelayed(mRunnable, 2000);
		}
		
		public void onClick(View view) {
			Log.i("fallwater", "onclick");
			switch (view.getId()) {
			case R.id.bt_music:
				mHandler.removeCallbacks(mRunnable);
				finish();
//				startActivity(new Intent(Mainacitivity.this, MusicActivity.class));
//				Log.i("fallwater", "onclick_music");
				break;
			case R.id.bt_video:
				startActivity(new Intent(Mainacitivity.this, VideoActivity.class));
//				Log.i("fallwater", "onclick_video");
				break;
			default:
				break;
			}
		}
		
		Runnable mRunnable ;
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
//			super.onBackPressed();
			mHandler.removeCallbacks(mRunnable);
			finish();
		}
}

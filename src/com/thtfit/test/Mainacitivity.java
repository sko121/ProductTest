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
import android.widget.TextView;

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
	
	
	
	private Handler mHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				
				switch (msg.what) {
				case RAM:	
					startActivityForResult(new Intent(Mainacitivity.this, MemoryActivity.class),0);
					Log.d(LOG_TAG, "memory");
					
					break;
				case NAND:					
					startActivity(new Intent(Mainacitivity.this, NandflashActivity.class));
					Log.d(LOG_TAG, "nand");
					break;
				case BATTERY:
					startActivity(new Intent(Mainacitivity.this, BatteryActivity.class));
					Log.d(LOG_TAG, "battery");
					break;
				case NETWORK:
					startActivity(new Intent(Mainacitivity.this,NetWorkActivity.class));
					Log.d(LOG_TAG,"networkButton");
					break;
				case BARCODE:
					startActivity(new Intent(Mainacitivity.this,MactobarcodeActivity.class));
					Log.d(LOG_TAG,"mactobarcode");
					break;
				case MUSIC:
					startActivity(new Intent(Mainacitivity.this, PlayActivity.class));
			        Log.d(LOG_TAG, "display");
					break;
				case VIDEO:
					
					break;
				case SHOW:
					
					break;

				default:
					break;
				}
				
				
			}
	};
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.layout_mainactivity);
			
			initView();
			requestAllPermissons();
			beginTest();
			
			
		
		}

		private void requestAllPermissons() {
			// TODO Auto-generated method stub
			//by Lu
			if (ContextCompat.checkSelfPermission(ProductTest.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				Log.d("luzhaojie", "NandflashActivity :: onCreate : have");
	        } else {
	        	 Log.d("luzhaojie", "NandflashActivity :: onCreate : no");
	            ActivityCompat.requestPermissions(this , new String[]{
	            		Manifest.permission.WRITE_EXTERNAL_STORAGE, 
	            		Manifest.permission.READ_EXTERNAL_STORAGE,
	            		}, 1);
	        }
		}
		
		//by Lu
		@Override
		public void onRequestPermissionsResult(int requestCode,
				String[] permissions, int[] grantResults) {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			switch (requestCode) {
			case 1:
				if (grantResults.length > 0
		                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
		                // permission was granted
					Log.d("luzhaojie", "NandflashActivity::onRequestPermissionsResult:permission was granted");
		            } else {
		                // permission denied,
		            }
				break;
			default:
				break;
			}
		}

		private void beginTest() {
			// TODO Auto-generated method stub
				mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mShowTextView.setText("now begin RAM test:");
					mHandler.sendEmptyMessageDelayed(RAM,2000);
					
				}
			}, 2000);
		}

		private void initView() {
			// TODO Auto-generated method stub
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
			
			Log.i(LOG_TAG,"onActivityResult_resultcode:" + resultCode);
			// TODO Auto-generated method stub
			if (requestCode==0) {
				switch (resultCode) {
				case RAM:	
					mShowTextView.setText("RAM test successfully ,now begin to test NAND");
					mRam.setTextColor(Color.GREEN);
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							startActivityForResult(new Intent(Mainacitivity.this, NandflashActivity.class),0);
						}
					}, 4000);
					break;
				case NAND:	
					mShowTextView.setText("NAND test successfully ,now begin to test BATTERY");
					mNand.setTextColor(Color.GREEN);
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							startActivityForResult(new Intent(Mainacitivity.this, BatteryActivity.class),0);
						}
					}, 4000);
					break;
				case BATTERY:
					mShowTextView.setText("BATTERY test successfully ,now begin to test reading QR code");
					mBattery.setTextColor(Color.GREEN);
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
//							startActivityForResult(new Intent(Mainacitivity.this, NetWorkActivity.class),0);
							startActivityForResult(new Intent(Mainacitivity.this, MactobarcodeActivity.class),0);
						}
					}, 4000);
					
					break;
				case NETWORK:
					mShowTextView.setText("NETWORK test successfully ,now begin to test reading QR code");
					mBattery.setTextColor(Color.GREEN);
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							startActivityForResult(new Intent(Mainacitivity.this, MactobarcodeActivity.class),0);
//							startActivityForResult(new Intent(Mainacitivity.this, VideoActivity.class),0);
						}
					}, 4000);
					break;
				case BARCODE:
					mShowTextView.setText("BARCODE test success :");
					mBarcode.setTextColor(Color.GREEN);
					testNextTime();
					break;
				case MUSIC:
					mShowTextView.setText("MUSIC test successfully ,now begin to test VIDEO");
					mBattery.setTextColor(Color.GREEN);
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							startActivityForResult(new Intent(Mainacitivity.this, PlayActivity.class),0);
//							startActivityForResult(new Intent(Mainacitivity.this, VideoActivity.class),0);
						}
					}, 4000);
					break;
				case VIDEO:
					mShowTextView.setText("VIDEO test success");
					mBattery.setTextColor(Color.GREEN);
					testNextTime();
					
					break;
				case SHOW:
					
					break;

				default:
					break;
				}
			}
			
			
			
			super.onActivityResult(requestCode, resultCode, data);
		}

		private void testNextTime() {
			// TODO Auto-generated method stub
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
//					startActivityForResult(new Intent(Mainacitivity.this, PlayActivity.class),0);
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
							// TODO Auto-generated method stub
							beginTest();
						}
					}, 2000);
				}
			}, 2000);
		}
	

}

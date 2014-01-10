package com.test;

import com.test.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EarplugActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Earplug";
	private final static String STRING_LAKALA_SUCCESS = "拉卡拉测试成功";
	private final static String STRING_LAKALA_FAILED = "拉卡拉测试失败";
	private int defaultColor = Color.BLACK;
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	private final String NO_PIN = "检测没有设备插入";
	private final String THREE_PIN = "检测三头设备插入";
	private final String FOUR_PIN = "检测四头设备插入";
	
	private HeadsetPlugReceiver headsetPlugReceiver;
	private Button startButton;
	private TextView mText;
	private ProductTest mProduct;
	private AlertDialog mDialog = null;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.earplug);
		mProduct = (ProductTest)getApplication();
		headsetPlugReceiver = new HeadsetPlugReceiver(); 
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetPlugReceiver, intentFilter);

		startButton = (Button)findViewById(R.id.button_start);
		//overButton = (Button)findViewById(R.id.button_over);
		startButton.setOnClickListener(this);
		//overButton.setOnClickListener(this);
		mText = (TextView)findViewById(R.id.message1);
		int Headset_statue = native_getEarplugHead();
		switch (Headset_statue) {
			case 0:
				mText.setText(NO_PIN);
				break;
			case 1:
				mText.setText(FOUR_PIN);
				mText.setTextColor(okColor);
				break;
			case 2:
				mText.setText(THREE_PIN);
				mText.setTextColor(okColor);
				break;
		}
	}
	@Override
	protected void onRestart(){
		super.onRestart();
		showConfirmDialog();
		Log.e(LOG_TAG, "lakala restart");
	}
    @Override
    public void onDestroy() {
    	unregisterReceiver(headsetPlugReceiver);
    	super.onDestroy();
    }     
	public class HeadsetPlugReceiver extends BroadcastReceiver {
		private static final String TAG = "HeadsetPlugReceiver";
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra("state")){
				Log.v(LOG_TAG, "state :"+intent.getIntExtra("state", 0));
				int Headset_statue = native_getEarplugHead();
				switch (Headset_statue) {
					case 0:
						mText.setText(NO_PIN);
						break;
					case 1:
						mText.setText(FOUR_PIN);
						mText.setTextColor(okColor);
						break;
					case 2:
						mText.setText(THREE_PIN);
						mText.setTextColor(okColor);
						break;
				}
				//if (intent.getIntExtra("state", 0) == 0){	
			   	//}else if (intent.getIntExtra("state", 0) == 1){
			   	//}
		  	}
		}
	}
	public void onClick(View v) {
		if(v.equals(startButton)) {
			Intent mIntent = new Intent(Intent.ACTION_MAIN);
			mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    	    mIntent.setFlags(0x10200000);
			ComponentName comp = new ComponentName("ban.card.payanywhere",
							"com.nabancard.ui.welcomescreen.WelcomeActivity");
	        mIntent.setComponent(comp);
			startActivity(mIntent);
		}
	}
	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle("拉卡拉")
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mText.setText(STRING_LAKALA_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(13, STRING_LAKALA_SUCCESS);
				}
			})
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_LAKALA_FAILED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(13, STRING_LAKALA_FAILED);
				}
			})
			.create();
		mDialog.setMessage("拉卡拉是否测试成功");
		mDialog.show();
	}
	public native int native_getEarplugHead();
	static {
		System.loadLibrary("audio_jni");
	}
}


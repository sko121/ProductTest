package com.thtfit.test;

import com.thtfit.test.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.res.Resources.NotFoundException;
import android.widget.Toast;

public class EarplugActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Earplug";
	private static String STRING_LAKALA_SUCCESS = "拉卡拉测试成功";
	private static String STRING_LAKALA_FAILED = "拉卡拉测试失败";
    private static String NO_PIN = "检测没有设备插入";
	private static String THREE_PIN = "检测三头设备插入";
	private static String FOUR_PIN = "检测四头设备插入";
	private static String EARPLUG = "Earplug";
	private static String EARPLUG_YES = "Yes";
	private static String EARPLUG_NO ="No";
	private static String EARPLUG_IS_SUCCESS ="Whether is the earplug successful";
	private int defaultColor = Color.BLACK;
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	
	
	private HeadsetPlugReceiver headsetPlugReceiver;
	private Button startButton, handsfreeButton;
	private TextView mText;
	private ProductTest mProduct;
	private AlertDialog mDialog = null;
 	private AudioManager audioManager;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.earplug);
		mProduct = (ProductTest)getApplication();
		headsetPlugReceiver = new HeadsetPlugReceiver(); 
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(headsetPlugReceiver, intentFilter);
	
		try{
            STRING_LAKALA_SUCCESS= this.getResources().getString(R.string.lakala_success);
            STRING_LAKALA_FAILED = this.getResources().getString(R.string.lakala_failed);
            EARPLUG = this.getResources().getString(R.string.earplug);
			EARPLUG_YES = this.getResources().getString(R.string.yes);
            EARPLUG_NO = this.getResources().getString(R.string.no);
			NO_PIN = this.getResources().getString(R.string.no_pin);
			THREE_PIN = this.getResources().getString(R.string.three_pin);
			FOUR_PIN = this.getResources().getString(R.string.four_pin);
            EARPLUG_IS_SUCCESS =  this.getResources().getString(R.string.earplug_is_success);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }

		audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
		if(audioManager == null)
			return;

		startButton = (Button)findViewById(R.id.button_start);
		handsfreeButton = (Button)findViewById(R.id.button_over);
		startButton.setOnClickListener(this);
		handsfreeButton.setOnClickListener(this);
		mText = (TextView)findViewById(R.id.message1);
		int Headset_statue = native_getEarplugHead();
		switch (Headset_statue) {
			case 0:
				mText.setText(NO_PIN);
				mText.setTextColor(defaultColor);
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
		audioManager.setSpeakerphoneOn(false);
		audioManager.setMode(AudioManager.MODE_NORMAL);
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
						mText.setTextColor(defaultColor);
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
			try{
				Intent mIntent = new Intent(Intent.ACTION_MAIN);
				mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			    mIntent.setFlags(0x10200000);
				ComponentName comp = new ComponentName("ban.card.payanywhere",
								"com.nabancard.ui.welcomescreen.WelcomeActivity");
			    mIntent.setComponent(comp);
				startActivity(mIntent);
			}catch(Exception e){

				Toast.makeText(this, "Don't found class", Toast.LENGTH_LONG).show();
			}
		}else if(v.equals(handsfreeButton)){
			doChangeSpeakerStatus();	
		}
	}
	private void doChangeSpeakerStatus() {
		try {
			if(!audioManager.isSpeakerphoneOn()){
				audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION); 
				audioManager.setSpeakerphoneOn(true);
			}else{	
				audioManager.setSpeakerphoneOn(false);
				audioManager.setMode(AudioManager.MODE_NORMAL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle(EARPLUG)
			.setPositiveButton(EARPLUG_YES, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mText.setText(STRING_LAKALA_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(13, STRING_LAKALA_SUCCESS);
				}
			})
			.setNegativeButton(EARPLUG_NO, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_LAKALA_FAILED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(13, STRING_LAKALA_FAILED);
				}
			})
			.create();
		mDialog.setMessage(EARPLUG_IS_SUCCESS);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
	public native int native_getEarplugHead();
	static {
		System.loadLibrary("thtfit_audio_jni");
	}
}


package com.test;
import com.test.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.lang.System;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
public class GeneralActivity extends Activity { //implements OnClickListener {
	private final static String LOG_TAG = "General";
	private final int okColor = Color.GREEN;
	private final int errColor = Color.RED;
	private final int defaultColor = Color.BLACK;
	private TextView mTextViewEthernet,mText1,mText2,mText3,mText4,mText5;
	private Button startButton;
	private Button stopButton;
	private Handler mHardler;
	private Context mContext;
	private Block mBlock;
	private EthernetManager mEthernet;
	private WifiController mWifi;
	private BluetoothController mBluetooth;
	private Handler mHandle;
	private Thread mThread;
	private volatile boolean existThread = false;
	private ProductTest mProduct;
	private TreeMap<Integer, String> mTreeMap;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general);	
		mProduct = (ProductTest)getApplication();
		mTreeMap = mProduct.mTreeMap;

		startButton = (Button)findViewById(R.id.button_start);
		stopButton = (Button)findViewById(R.id.button_stop);
		startButton.setOnClickListener(mBindListener);
		stopButton.setOnClickListener(mUnbindListener);
		mText1 = (TextView)findViewById(R.id.textgeneral1);
		mText2 = (TextView)findViewById(R.id.textgeneral2);
		mText3 = (TextView)findViewById(R.id.textgeneral3);
		mText4 = (TextView)findViewById(R.id.textgeneral4);

		mHandle = new Handler() {
			public void handleMessage(Message msg) {
				Log.d(LOG_TAG, "message: "+msg.toString());
				if(msg.what == Block.StopMessage){
					mText1.setText("内存测试结果");
					mText1.setTextColor(defaultColor);
					mText2.setText("flash测试结果");
					mText2.setTextColor(defaultColor);
					mText3.setText("SD卡测试结果");
					mText3.setTextColor(defaultColor);
					mText4.setText("U盘测试结果");
					mText4.setTextColor(defaultColor);
					existThread = false;
					Block.forceStop = false;
					return;
				}
				String MessageString = mTreeMap.get(msg.what); 
				int temp = msg.what / 100;
				int color;
				if((msg.what % 10) == 0)
					color = okColor;
				else
					color = errColor;
				switch (temp) {
					case 1:
						mText1.setText(MessageString);
						mText1.setTextColor(color);
						break;
					case 2:
						mText2.setText(MessageString);
						mText2.setTextColor(color);
						break;
					case 3:
						mText3.setText(MessageString);
						mText3.setTextColor(color);
						break;
					case 4:
						mText4.setText(MessageString);
						mText4.setTextColor(color);
						break;
				}
				mProduct.addKeyEvent(msg.what);
			}
		};

		mContext = getApplicationContext();
		mBlock = new Block(mHandle);
	}
	protected void onStop(){
		super.onStop();
		Block.forceStop = false;
	}
/*
	public void handleMemoryMessage(int message, TextView text)
	{
		switch (message) {
			case Block.MEMORY_SUCCESS:
				text.setText(MEMORY_SUCCESS_STRING+totalMem+" M");
				text.setTextColor(okColor);
				break;
			case Block.MEMORY_NOMEM:
				text.setText(MEMORY_NOMEM_STRING);
				text.setTextColor(errColor);
				break;
			case Block.MEMORY_COPY_ERROR:
				text.setText(MEMORY_COPY_ERROR_STRING);
				text.setTextColor(errColor);
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
		}
	}	

	public void handleSdcardMessage(int message, TextView text)
	{
		switch (message) {
			case Block.EXTSD_SUCCESS:
				text.setText(EXTSD_SUCCESS_STRING);
				text.setTextColor(okColor);
				break;
			case Block.EXTSD_NOT_EXIST:
				text.setText(EXTSD_NOT_EXIST_STRING);
				text.setTextColor(errColor);
				break;
			case Block.EXTSD_NOT_ENOUGH_SPACE:
				text.setText(EXTSD_NOT_ENOUGH_SPACE_STRING);
				text.setTextColor(errColor);
				break;
			case Block.EXTSD_COPY_ERROR:
				text.setText(EXTSD_COPY_ERROR_STRING);
				text.setTextColor(errColor);
				break;
			case Block.EXTSD_FIALED:
				text.setText(EXTSD_FIALED_STRING);
				text.setTextColor(errColor);
				break;
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
				break;
		}
	}
	public void handleFlashMessage(int message, TextView text)
	{
		switch (message){
			case Block.NANDFLASH_SUCCESS:
				text.setText(NANDFLASH_SUCCESS_STRING+totalNand+" M");
				text.setTextColor(okColor);
				break;
			case Block.NANDFLASH_NOSPC:
				text.setText(NANDFLASH_NOSPC_STRING);
				text.setTextColor(errColor);
				break;
			case Block.NANDFLASH_NOMEM:
				text.setText(NANDFLASH_NOMEM_STRING);
				text.setTextColor(errColor);
				break;
			case Block.NANDFLASH_EACCESS:
				text.setText(NANDFLASH_EACCESS_STRING);
				text.setTextColor(errColor);
				break;
			case Block.NANDFLASH_WRITE_ERROR:
				text.setText(NANDFLASH_WRITE_ERROR_STRING);
				text.setTextColor(errColor);
				break;
			case Block.NANDFLASH_COPY_ERROR:
				text.setText(NANDFLASH_COPY_ERROR_STRING);
				text.setTextColor(errColor);
				break;
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
		}
	}
	public void handleUsbhostMessage(int message, TextView text)
	{
		switch (message) {
			case Block.USBHOST_SUCCESS:
				text.setText(USBHOST_SUCCESS_STRING);
				text.setTextColor(okColor);
				break;
			case Block.USBHOST_NOT_EXIST:
				text.setText(USBHOST_NOT_EXIST_STRING);
				text.setTextColor(errColor);
				break;
			case Block.USBHOST_NOT_ENOUGH_SPACE:
				text.setText(USBHOST_NOT_ENOUGH_SPACE_STRING);
				text.setTextColor(errColor);
				break;
			case Block.USBHOST_COPY_ERROR:
				text.setText(USBHOST_COPY_ERROR_STRING);
				text.setTextColor(errColor);
				break;
			case Block.USBHOST_FIALED:
				text.setText(USBHOST_FIALED_STRING);
				text.setTextColor(errColor);
				break;
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
				break;
		}
	}*/
	private OnClickListener mBindListener = new OnClickListener(){
		public void onClick(View v){
			if(existThread){
				Log.v(LOG_TAG, "existTread");	
				return;
			}
			mThread = new Thread(){
				public void run(){
					int ret;
					existThread = true;
					ret = mBlock.memoryInspection();
					if(ret < 0){
						return;
					}
					ret = mBlock.nandflashInspection();
					if(ret < 0){
						return;
					}
					ret = mBlock.sdcardInspection();
					if(ret < 0){
						return;
					}
					ret = mBlock.usbhostInspection();
					if(ret < 0){
						return;
					}
					existThread= false;
				}
			};
			mThread.start();
		}
	};
	private OnClickListener mUnbindListener = new OnClickListener(){
		public void onClick(View v){
			if(!Block.forceStop){
				Block.forceStop = true;
				//mBlock.terminateInspection();
				if(!existThread){
					Message msg = Message.obtain();
					msg.what = Block.StopMessage;
					mHandle.sendMessage(msg);
				}
			}
		}
	};
}

package com.test;

import com.test.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BluetoothActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Adversiting_Bluetooth";	
	private final static String STRING_BLUETOOTH_SUCCESS = "蓝牙测试成功";
	private final static String STRING_BLUETOOTH_FAILED = "蓝牙测试失败";
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	private TextView mText;
	private Button startButton, stopButton;
	private ProductTest mProduct;
	private AlertDialog mDialog = null;

	public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);
		mProduct = (ProductTest)getApplication();
		startButton = (Button)findViewById(R.id.button_start);
		startButton.setOnClickListener(this);
		stopButton = (Button)findViewById(R.id.button_stop);
		stopButton.setOnClickListener(this);
		mText = (TextView)findViewById(R.id.textResult);
		//mBluetooth = new BluetoothController(this, mHandle);
/*		ConnectivityManager Manager = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo EthernetInfo = Manager.
				getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
		Log.v(LOG_TAG, "state:"+EthernetInfo.toString());
		if(EthernetInfo.isConnected()){
			mText.setText("已经连接到蓝牙网络");
			mText.setTextColor(okColor);
		}*/
	}
	@Override
	protected void onRestart(){
		super.onRestart();
		showConfirmDialog();
		Log.e(LOG_TAG, "bluetooth restart");
	}
	public void onClick(View v) {
		if(v.equals(startButton)){
			Intent mIntent = new Intent();
        	ComponentName comp = new ComponentName("com.android.settings",
							"com.android.settings.Settings");
    	    mIntent.setComponent(comp);
	        mIntent.setAction("android.intent.action.MAIN");
        	startActivity(mIntent);
		}else if(v.equals(stopButton)){
		}
	}
	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle("蓝牙")
			.setPositiveButton("是", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_BLUETOOTH_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(7, STRING_BLUETOOTH_SUCCESS);
				}
			})
			.setNegativeButton("否", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_BLUETOOTH_FAILED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(7, STRING_BLUETOOTH_FAILED);
				}
			})
			.create();
		mDialog.setMessage("蓝牙是否测试成功");
		mDialog.show();
	}
}

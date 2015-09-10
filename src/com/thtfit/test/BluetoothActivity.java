package com.thtfit.test;

import com.thtfit.test.R;
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
import android.bluetooth.BluetoothAdapter;
import android.content.res.Resources.NotFoundException;

public class BluetoothActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Adversiting_Bluetooth";	
	private static String STRING_BLUETOOTH_SUCCESS = "蓝牙测试成功";
	private static String STRING_BLUETOOTH_FAILED = "蓝牙测试失败";
    private static String BLUTOOTH = "Blutooth";
    private static String BLUTOOTH_YES ="Yes";
	private static String BLUTOOTH_NO ="No";
	private static String BLUTOOTH_IS_SUCCESS ="Whether is the blutooth successful";
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

		try{
            STRING_BLUETOOTH_SUCCESS= this.getResources().getString(R.string.bluetooth_success);
            STRING_BLUETOOTH_FAILED = this.getResources().getString(R.string.bluetooth_failed);
            BLUTOOTH = this.getResources().getString(R.string.bluetooth);
			BLUTOOTH_YES = this.getResources().getString(R.string.yes);
            BLUTOOTH_NO = this.getResources().getString(R.string.no);
            BLUTOOTH_IS_SUCCESS =  this.getResources().getString(R.string.bluetooth_is_success);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }
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
            /*
			Intent mIntent = new Intent();
        	ComponentName comp = new ComponentName("com.android.settings",
							"com.android.settings.Settings");
    	    mIntent.setComponent(comp);
	        mIntent.setAction("android.intent.action.MAIN");
        	startActivity(mIntent);*/
            gotoSetBluetooth();
		}else if(v.equals(stopButton)){
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter.isEnabled()){
              gotoSetBluetooth();
            }
		}
	}
    private void gotoSetBluetooth(){
     		Intent mIntent = new Intent();
        	ComponentName comp = new ComponentName("com.android.settings",
							"com.android.settings.Settings");
    	    mIntent.setComponent(comp);
	        mIntent.setAction("android.intent.action.MAIN");
        	startActivity(mIntent);
    
    }
	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle(BLUTOOTH)
			.setPositiveButton(BLUTOOTH_YES, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_BLUETOOTH_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(7, STRING_BLUETOOTH_SUCCESS);
				}
			})
			.setNegativeButton(BLUTOOTH_NO, new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_BLUETOOTH_FAILED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(7, STRING_BLUETOOTH_FAILED);
				}
			})
			.create();
		mDialog.setMessage(BLUTOOTH_IS_SUCCESS);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
}

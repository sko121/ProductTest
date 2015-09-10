package com.thtfit.test;

import com.thtfit.test.WifiController;
import com.thtfit.test.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WifiActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Adversiting_Wifi";	
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;

	private TextView mText;
	private Button WifiConnectButton, WifiTestButton;
	private NetworkInfo EthernetInfo;
	private ProductTest mProduct;
	//private WifiController mWifi;
	@Override
	public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi);
		mProduct = (ProductTest)getApplication();
		mText = (TextView)findViewById(R.id.textResult);
		ConnectivityManager Manager = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo EthernetInfo = Manager.
				getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Log.v(LOG_TAG, "state:"+EthernetInfo.toString());
		if(EthernetInfo.isConnected()){
			String str="已经连接到Wifi网络 :"+EthernetInfo.getExtraInfo();
			mText.setText(str);
			mText.setTextColor(okColor);
			mProduct.addKeyEvent(6, str);
		}else{
			gotoSetWifi();
		}
		WifiConnectButton = (Button)findViewById(R.id.button_WifiConnect);
		WifiTestButton = (Button)findViewById(R.id.button_WifiTest);
		WifiConnectButton.setOnClickListener(this);
		WifiTestButton.setOnClickListener(this);
	}
	@Override
	public void onRestart(){
		super.onRestart();
		ConnectivityManager Manager = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo EthernetInfo = Manager.
				getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(EthernetInfo.isConnected()){
			String str="已经连接到Wifi网络 :"+EthernetInfo.getExtraInfo();
			mText.setText(str);
			mText.setTextColor(okColor);
			mProduct.addKeyEvent(6, str);
		}else{
			String str="没有连接到Wifi网络";
			mText.setText(str);
			mText.setTextColor(errColor);
			mProduct.addKeyEvent(6, str);
		}	
	}
	public void onClick(View v) {
		if(v.equals(WifiConnectButton)){
			gotoSetWifi();
		}else if(v.equals(WifiTestButton)){
		WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
		if(mWifiManager != null)
            gotoSetWifi();
			Log.v(LOG_TAG, "connect ssid:"+mWifiInfo.getSSID());
		}
	}
	private void gotoSetWifi(){
			Intent mIntent = new Intent();
        	ComponentName comp = new ComponentName("com.android.settings",
							"com.android.settings.Settings");
        	mIntent.setComponent(comp);
        	mIntent.setAction("android.intent.action.MAIN");
        	startActivity(mIntent);
	}
}

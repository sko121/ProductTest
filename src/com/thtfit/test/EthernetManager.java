package com.thtfit.test;

import android.net.ConnectivityManager;
import android.content.Intent;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.lang.System;
import java.lang.Runtime;
import java.lang.Exception;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EthernetManager {
	private final static String LOG_TAG = "Adversiting-Ethernet";
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	public final int ETHERNET_CHECK_SUCCESS = 400;
	public final int ETHERNET_NO_DEVICE_EXIST = 410;
	public final int ETHERNET_UNCONNECT_LOCAL_OBJ = 420;
	public final int ETHERNET_UNCONNECT_EXTRA_OBJ = 430;
	public final String ETHERNET_CHECK_SUCCESS_STRING = "以太网测试成功";
	public final String ETHERNET_NO_DEVICE_EXIST_STRING= "不存在以太网设备";
	public final String ETHERNET_UNCONNECT_LOCAL_OBJ_STRING = "不能连接到本地局域网";
	public final String ETHERNET_UNCONNECT_EXTRA_OBJ_STRING= "不能连接到远程局域网";
	private ComponentName component = null;
	private ConnectivityManager Manager = null;
	private NetworkInfo EthernetInfo = null;
	private String Interface;
	private Context mContext;
	private Handler mHandle = null;
	
	public EthernetManager(Context context, Handler handle){
		mContext = context;
		mHandle = handle;
		Manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		EthernetInfo = Manager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
		Interface = EthernetInfo.getTypeName();
	}
	public void handleEthernetMessage(int message, TextView text)
	{
		switch (message) {
			case ETHERNET_CHECK_SUCCESS:
				text.setText(ETHERNET_CHECK_SUCCESS_STRING);
				text.setTextColor(okColor);
				break;
			case ETHERNET_NO_DEVICE_EXIST:
				text.setText(ETHERNET_NO_DEVICE_EXIST_STRING);
				text.setTextColor(errColor);
				break;
			case ETHERNET_UNCONNECT_LOCAL_OBJ:
				text.setText(ETHERNET_UNCONNECT_LOCAL_OBJ_STRING);
				text.setTextColor(errColor);
				break;
			case ETHERNET_UNCONNECT_EXTRA_OBJ:
				text.setText(ETHERNET_UNCONNECT_EXTRA_OBJ_STRING);
				text.setTextColor(errColor);
				break;
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
		}
	}	
	private void sendLocalMessage(int message){
		Message msg = Message.obtain();
		msg.what = message;
		mHandle.sendMessage(msg);
	}

	public void EthernetTest(){
		Log.d(LOG_TAG, "EthernetTest start");
		//测试以太网首先要关闭wifi
		WifiManager mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        if(mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }
		Log.d(LOG_TAG, "EthernetTest mWificlose");
		if(mHandle == null){
			Log.d(LOG_TAG, "EthernetTest mHandle is null");
		}
		if(!EthernetInfo.isAvailable()){
			if(mHandle != null)
				sendLocalMessage(ETHERNET_NO_DEVICE_EXIST);
			return;
		}
		Log.d(LOG_TAG, "EthernetTest setWifiEnabled");
		if(!EthernetInfo.isConnected()){
			if(mHandle != null)
				sendLocalMessage(ETHERNET_UNCONNECT_LOCAL_OBJ);
			return;
		}
		if(!isPingTarget()){
			if(mHandle != null)
				sendLocalMessage(ETHERNET_UNCONNECT_EXTRA_OBJ);
			return;
		}
		if(mHandle != null)
			sendLocalMessage(ETHERNET_CHECK_SUCCESS);
	}

	protected boolean isPingTarget()
	{
		Process process = null;
		try {
			//process = Runtime.getRuntime().exec("cmd /c start ping 115.239.210.26");
			process = Runtime.getRuntime().exec("ping -c 2 115.239.210.26");
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String buf;
			while ((buf = br.readLine()) != null) {
				System.out.println(buf);
			}
			br.close();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(process != null)
			Log.d(LOG_TAG,"result: "+process.exitValue());
		boolean result = process.exitValue() == 0 ? true : false;

		return result;
	}
}

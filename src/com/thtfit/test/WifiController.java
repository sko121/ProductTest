package com.thtfit.test;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView; 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.lang.Runtime;
import java.lang.Thread;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class WifiController {
	private final static String LOG_TAG = "Adversiting_Wifi";	
	//private final String DEFUALT_NETWORK_SSID = "CMCC-WiNS";
	private final String DEFUALT_NETWORK_SSID = "DvT-3";
	private final String DEFUALT_SHARED_KEY = "123456781234";
	private static final int WIFI_RESCAN_INTERVAL_MS = 10 * 1000;
	private ConnectivityManager Manager;
	private NetworkInfo EthernetInfo;
	private WifiManager mWifiManager;
	private WifiManager.ActionListener mConnectListener;
	private String Interface;
	private Handler mHandle;
	boolean State = false;
	WifiConfiguration mConfig = null;
	ScanResult mResult = null;
    static final int SECURITY_NONE = 0;
    static final int SECURITY_WEP = 1;
    static final int SECURITY_PSK = 2;
    static final int SECURITY_EAP = 3;
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	public static final String ACTION_WIFI_INSPECTION_STATUS = "android.test.wifi";
    public static final int WIFI_CHECK_SUCCESS = 500;
    public static final int WIFI_NO_DEVICE_EXIST = 510;
	public static final int WIFI_SCANNER_FAILED = 520;
    public static final int WIFI_UNFIND_OBJECT_ROUTE = 530;
	public static final int WIFI_UNBIND_OBJECT_ROUTE = 540;
    public static final int WIFI_UNCONNECT_EXTRA_OBJ = 550;
	public static final String WIFI_CHECK_SUCCESS_STRING="wifi测试成功";
	public static final String WIFI_NO_DEVICE_EXIST_STRING="没有发现wifi设备";
	public static final String WIFI_SCANNER_FAILED_STRING="没有发现wifi路由";
	public static final String WIFI_UNFIND_OBJECT_ROUTE_STRING="没有发现wifi路由";
	public static final String WIFI_UNBIND_OBJECT_ROUTE_STRING="不能连接目标wifi路由";
	public static final String WIFI_UNCONNECT_EXTRA_OBJ_STRING="不能连接远程目标";
	
	public WifiController(Context context, Handler handle) {
		mHandle = handle;
		mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		Manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		EthernetInfo = Manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Interface = EthernetInfo.getTypeName();
	}
	
	public void handleWifiMessage(int message, TextView text)
	{
		switch (message) {
		case WIFI_CHECK_SUCCESS:
			text.setText(WIFI_CHECK_SUCCESS_STRING);
			text.setTextColor(okColor);
			break;
		case WIFI_UNFIND_OBJECT_ROUTE:
			text.setText(WIFI_UNFIND_OBJECT_ROUTE_STRING);
			text.setTextColor(errColor);
			break;
		case WIFI_UNBIND_OBJECT_ROUTE:
			text.setText(WIFI_UNBIND_OBJECT_ROUTE_STRING);
			text.setTextColor(errColor);
			break;
		case WIFI_UNCONNECT_EXTRA_OBJ:
			text.setText(WIFI_UNCONNECT_EXTRA_OBJ_STRING);
			text.setTextColor(errColor);
			break;
		default:
			Log.d(LOG_TAG, "unknown message: "+message);
		}	
	}
	public void WifiTest(){
		if(!mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(true);
		}
		//mWifiManager.startScan();
		if(!Scanner()){
			Message msg = Message.obtain();
			msg.what = WIFI_SCANNER_FAILED;
			mHandle.sendMessage(msg);
			return;
		}
		if(!findObjectScanResults()){
			Message msg = Message.obtain();
			msg.what = WIFI_UNFIND_OBJECT_ROUTE;
			mHandle.sendMessage(msg);
			return;
		}
		connectObject();
	}
/*
	private boolean findObjectWifiRoute(){
		if(findObjectConfiguedNetworks() 
			|| findObjectScanResults()){
			return true;	
		}
		return false;
	}
	private boolean findObjectConfiguedNetworks(){
		final List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
        if (configs != null) {
            for (WifiConfiguration config : configs) {
				Log.d(LOG_TAG,"config SSID: "+config.SSID);
				if(config.SSID.equals(DEFUALT_NETWORK_SSID)){
					Log.d(LOG_TAG,"find object config SSID: "+config.SSID);
					mConfig = config;
					return true;
				}
            }
        }
		return false;
	}*/
	private boolean findObjectScanResults(){
        final List<ScanResult> results = mWifiManager.getScanResults();
        if (results != null) {
            for (ScanResult result : results) {
                // Ignore hidden and ad-hoc networks.
                if (result.SSID == null || result.SSID.length() == 0 ||
                        result.capabilities.contains("[IBSS]")) {
                    continue;
                }
				Log.d(LOG_TAG,"result SSID: "+result.SSID);
				if(result.SSID.equals(DEFUALT_NETWORK_SSID)){
					Log.d(LOG_TAG,"find object result SSID: "+result.SSID);
					mResult = result;
					return true;
				}
            }
        }
		return false;
	}
	private boolean connectObject(){
        mConnectListener = new WifiManager.ActionListener() {
		   public void onSuccess() {
				new Thread(new Runnable(){
					public void run(){
					if(!isPingTarget()){
						Message msg = Message.obtain();
						msg.what = WIFI_UNCONNECT_EXTRA_OBJ;
						mHandle.sendMessage(msg);
					}else {
						Message msg = Message.obtain();
						msg.what = WIFI_CHECK_SUCCESS;
						mHandle.sendMessage(msg);
					}
				}}).start();	
				Log.d(LOG_TAG,"wifi connect success");
			}
			public void onFailure(int reason) {
				Message msg = Message.obtain();
				msg.what = WIFI_UNBIND_OBJECT_ROUTE;
				mHandle.sendMessage(msg);
				Log.d(LOG_TAG,"wifi connect failed");
			}
        };
		mWifiManager.disconnect();
		if(mConfig != null){
			mWifiManager.connect(mConfig.networkId,
							mConnectListener);
			return true;
		}
		if(mResult != null){
			WifiConfiguration config = new WifiConfiguration();
			config.SSID = convertToQuotedString(DEFUALT_NETWORK_SSID);
			//config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.preSharedKey = convertToQuotedString(DEFUALT_SHARED_KEY); 
			mWifiManager.connect(config, mConnectListener);
			return true;
		}
		return false;
	}
    static String convertToQuotedString(String string) {
        return "\"" + string + "\"";
    }
	boolean isPingTarget()
	{
		Process process = null;
		try {
			Thread.sleep(5000);
			//process = Runtime.getRuntime().exec("cmd /c start ping 115.239.210.26");
			process = Runtime.getRuntime().exec("ping -c 5 115.239.210.26");
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
    private boolean Scanner() {
        int mRetry = 0;
		return false;
		/*while(true){
            if (mWifiManager.startScanActive()) {
				try{
					Thread.sleep(3000);
				}catch (Exception e){
					e.printStackTrace();
				}
				return true;
            } else if (++mRetry >= 3) {
				Log.d(LOG_TAG, "mRetry : %d"+mRetry);
	            return false;
            }
		}*/
    }	
}

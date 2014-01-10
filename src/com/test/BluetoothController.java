package com.test;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BluetoothController {
	private static final String LOG_TAG = "BluetoothManage";
	private String DEFUALT_BLUETOOTH_DEVICE = "GT-S7572";    
	private String mPairingKey = "123456";
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	public final String ACTION_BLUETOOTH_INSPECTION_STATUS = "android.test.bluetooth";
    public final int BLUETOOTH_CHECK_SUCCESS = 600;
    public final int BLUETOOTH_NO_DEVICE_EXIST = 610;
	public final int BLUETOOTH_SCANNER_FAILED = 620;
    public final int BLUETOOTH_UNFIND_OBJECT_ROUTE = 630;
	public final int BLUETOOTH_UNBIND_OBJECT_ROUTE = 640;
    public final int BLUETOOTH_UNCONNECT_EXTRA_OBJ = 650;
	public final String BLUETOOTH_CHECK_SUCCESS_STRING="蓝牙测试成功";
	public final String BLUETOOTH_NO_DEVICE_EXIST_STRING="没有发现本地蓝牙设备";
	public final String BLUETOOTH_SCANNER_FAILED_STRING="没有发现远程蓝牙设备";
	public final String BLUETOOTH_UNFIND_OBJECT_ROUTE_STRING="不能发现远程蓝牙设备";
	public final String BLUETOOTH_UNBIND_OBJECT_ROUTE_STRING="不能绑定远程蓝牙设备";
	public final String BLUETOOTH_UNCONNECT_EXTRA_OBJ_STRING="与远程蓝牙设备传输数据错误";

    public static final int PAIRING_VARIANT_PASSKEY = 1;
    public static final int PAIRING_VARIANT_OOB_CONSENT = 6;
	private BluetoothAdapter mLocalAdapter; 		
	private final IntentFilter mLocalAdapterIntentFilter;
    private Context mContext;
	private Handler mHandle;
	private boolean mReceiverRegistered = false;

	public BluetoothController(Context context, Handler handle)
	{
		mHandle = handle;
		mContext = context;
		mLocalAdapter = BluetoothAdapter.getDefaultAdapter();
		mLocalAdapterIntentFilter = new IntentFilter();
		mLocalAdapterIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		mLocalAdapterIntentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
		mContext.registerReceiver(mBroadcastReceiver, mLocalAdapterIntentFilter);
		mReceiverRegistered = true;
	}
	public void dBluetoothController()
	{
		if(mReceiverRegistered)
			mContext.unregisterReceiver(mBroadcastReceiver);
	}

	public void handleBluetoothMessage(int message, TextView text)
	{
		switch (message) {
		case BLUETOOTH_CHECK_SUCCESS:
			text.setText(BLUETOOTH_CHECK_SUCCESS_STRING);
			text.setTextColor(okColor);
			break;
		case BLUETOOTH_NO_DEVICE_EXIST:
			text.setText(BLUETOOTH_NO_DEVICE_EXIST_STRING);
			text.setTextColor(errColor);
			break;
		case BLUETOOTH_UNBIND_OBJECT_ROUTE:
			text.setText(BLUETOOTH_UNBIND_OBJECT_ROUTE_STRING);
			text.setTextColor(errColor);
			break;
		case BLUETOOTH_UNCONNECT_EXTRA_OBJ:
			text.setText(BLUETOOTH_UNCONNECT_EXTRA_OBJ_STRING);
			text.setTextColor(errColor);
			break;
		default:
			Log.d(LOG_TAG, "unknown message: "+message);
		}
	}	
	public void BluetoothTest(){
		if(!mLocalAdapter.isEnabled())
			mLocalAdapter.enable();	

		findDevice();
	}
	public void findDevice()
	{
		Set<BluetoothDevice> devices = mLocalAdapter.getBondedDevices();
		if(devices.size()>0){
			for(Iterator<BluetoothDevice> iterator = 
						devices.iterator();iterator.hasNext();){
				BluetoothDevice device = (BluetoothDevice) iterator.next();
				System.out.println("matched :"+device.getName());
			}
		}
		mLocalAdapter.startDiscovery();  
	}

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if(action.equals(BluetoothDevice.ACTION_FOUND))
				DeviceFoundHandler(context, intent, device);		
			if(action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST))
				DevicePairingHandler(context, intent, device);
        }	
    };
	private void DeviceFoundHandler(Context context, Intent intent,
                BluetoothDevice device) {
      	short rssi = intent.getShortExtra(
				BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
      	BluetoothClass btClass = intent.getParcelableExtra(
									BluetoothDevice.EXTRA_CLASS);
      	String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
		if(name.equals(DEFUALT_BLUETOOTH_DEVICE)){
			byte[] pinBytes = BluetoothDevice.convertPinToBytes(mPairingKey);
		    device.setPin(pinBytes);
			Log.d(LOG_TAG, "DeviceFoundHandler =======>>>>>"+device.getName());	
			Log.d(LOG_TAG, "DeviceFoundHandler :"+device.createBond());
		}
    }
	private void DevicePairingHandler(Context context, Intent intent,
                BluetoothDevice device) {
			int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT,
                    BluetoothDevice.ERROR);
//			onPair(device, type, "Paring");
			Log.d(LOG_TAG, "DevicePairingHandler =======>>>>>"+device.getName());	
	}

}
/*
	private void onPair(BluetoothDevice device, int type, String value) {
        switch (type) {
            case BluetoothDevice.PAIRING_VARIANT_PIN:
                byte[] pinBytes = BluetoothDevice.convertPinToBytes(value);
                if (pinBytes == null) {
                    return;
                }
                device.setPin(pinBytes);
                break;
            case PAIRING_VARIANT_PASSKEY:
                int passkey = Integer.parseInt(value);
                device.setPasskey(passkey);
                break;
            case BluetoothDevice.PAIRING_VARIANT_PASSKEY_CONFIRMATION:
            case BluetoothDevice.PAIRING_VARIANT_CONSENT:
                device.setPairingConfirmation(true);
                break;
            case BluetoothDevice.PAIRING_VARIANT_DISPLAY_PASSKEY:
            case BluetoothDevice.PAIRING_VARIANT_DISPLAY_PIN:
                // Do nothing.
                break;
            case PAIRING_VARIANT_OOB_CONSENT:
                device.setRemoteOutOfBandData();
                break;
            default:
                Log.e(LOG_TAG, "Incorrect pairing type received");
        }
    }
   */

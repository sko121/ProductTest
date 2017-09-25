package com.thtfit.test;
import com.thtfit.test.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UsbDevicesActivity extends Activity {
	
	private String TAG = "UsbDevicesActivity";
	private boolean mIsSmallScreen = true;
	private UsbManager mUsbManAndroid;
	private ListView mListUsbAndroid;
	private TextView mTvDeviceCountAndroid;
	private HashMap<String, UsbDevice> mAndroidUsbDeviceList;
	
	private boolean isSmallScreen(){
		Boolean res;
		if(findViewById(R.id.fragment_container) == null){
			res = true;
		} else {
			res = false;
		}
		Log.d(TAG, "^ Is this device a small screen? " + res);
		return res;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usb_device_main);
		mIsSmallScreen = isSmallScreen();
		mUsbManAndroid = (UsbManager) getSystemService(Context.USB_SERVICE);
		mTvDeviceCountAndroid = (TextView) findViewById(R.id.lbl_devices_api);
		mListUsbAndroid = (ListView) findViewById(R.id.usb_list_api);
		mListUsbAndroid.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListUsbAndroid.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mListUsbAndroid.setItemChecked(position, true);
				displayAndroidUsbDeviceInfo(((TextView) view).getText().toString());
			}
			
		});
		
		View emptyView = getListViewEmptyView(getString(R.string.label_empty_list));
		((ViewGroup) mListUsbAndroid.getParent()).addView(emptyView);
		mListUsbAndroid.setEmptyView(emptyView);
		refreshUsbDevices();
	}
	
	private void refreshUsbDevices(){

		// Getting devices from API
	    mAndroidUsbDeviceList = mUsbManAndroid.getDeviceList();
		String[] array = mAndroidUsbDeviceList.keySet().toArray(new String[mAndroidUsbDeviceList.keySet().size()]);

		Arrays.sort(array);
			
		ArrayAdapter<String> adaptor = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, array);
		mListUsbAndroid.setAdapter(adaptor);
		mTvDeviceCountAndroid.setText("Device List (" + mAndroidUsbDeviceList.size()+ "):");
		Fragment f = new UsbDeviceInfoAndroidFragment(this,"");

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, f);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

		ft.commit();
	}
	
	private View getListViewEmptyView(String text){
		TextView emptyView = new TextView(getApplicationContext());
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setText(text);
		emptyView.setTextSize(20f);
		emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		return emptyView;
	}
	
	private void displayAndroidUsbDeviceInfo(String device){
		
            Fragment f = new UsbDeviceInfoAndroidFragment(this,device);

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.fragment_container, f);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

			ft.commit();
            
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			refreshUsbDevices();
			return true;
		}

		return false;
	}
	
	
	
	
}

package com.thtfit.test;
import com.thtfit.test.R;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android_serialport_api.SerialPortFinder;

public class SerialActivity extends Activity {

	private ListView listView;	
	private SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	private SerialAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        List<String> list = new ArrayList<String>();
        if(entries.length == entryValues.length){
        	for(int i = 0; i < entries.length; i++){
        		list.add("SerialPort: "+entries[i]+"   position: "+entryValues[i]);
        	}
        }else{
        	this.finish();
        }
		setContentView(R.layout.serial_devices);
		listView = (ListView)findViewById(R.id.serialListView);
		adapter =  new SerialAdapter(list,this);
		if(list.size() > 0){
			listView.setAdapter(adapter);
		}
	}
}

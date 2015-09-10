package com.thtfit.test;
import com.thtfit.test.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SerialAdapter extends BaseAdapter {

	private List<String> list = new ArrayList<String>();
	private Context context;
	private LayoutInflater inflater =null;
	
	public SerialAdapter(List<String> list,Context context){
		this.list = list;
		this.context = context;
		inflater=LayoutInflater.from(context);
	}
	
	
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		v = inflater.inflate(R.layout.serial_adapter, null);
		TextView tv = (TextView)v.findViewById(R.id.text_serial);
		tv.setText(list.get(position));
		return v;
	}

}

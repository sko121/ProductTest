package com.thtfit.test;
import com.thtfit.test.R;

/*******************************************************************************
 * Copyright 2011 Alexandros Schillings
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ValidFragment" })
public class UsbDeviceInfoAndroidFragment extends Fragment {
	private final String TAG =  this.getClass().getName();

	private final static String BUNDLE_USB_KEY = "BUNDLE_USBKEY";

	public final static int TYPE_ANDROID_INFO = 0;
	public final static int TYPE_LINUX_INFO = 1;	

	public final static String DEFAULT_STRING = "???";
	private String usbKey = DEFAULT_STRING;
	private TableLayout tblUsbInfoHeader;
	private TableLayout tblUsbInfoTop;
	private TableLayout tblUsbInfoBottom;
	private TextView tvVID;
	private TextView tvPID;
	private TextView tvVendorDb;
	private TextView tvProductDb;		
	private TextView tvDevicePath;
	private TextView tvDeviceClass;
	private ImageButton btnLogo;
	private UsbManager usbMan;


	private Context context;
	public UsbDeviceInfoAndroidFragment(Context mContext,String usbKey) {
		this.context = mContext;
		this.usbKey = usbKey;
	}

	private void addDataRow(LayoutInflater inflater, TableLayout tlb, String cell1Text, String cell2Text){
		TableRow row = (TableRow)inflater.inflate(R.layout.usb_table_row_data, null);
		TextView tv1 = (TextView) row.findViewById(R.id.usb_tablerow_cell1);
		TextView tv2 = (TextView) row.findViewById(R.id.usb_tablerow_cell2);
		tv1.setText(cell1Text);
		tv2.setText(cell2Text);
		tlb.addView(row);
	}



	/**
	 * If we are being created with saved state, restore our state
	 */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		if (null != saved) {
			usbKey = saved.getString(BUNDLE_USB_KEY);
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {   	
		View v = new LinearLayout(getActivity().getApplicationContext());
		//context = getActivity().getApplicationContext();
		if(context == null){
			return null;
		}
		usbMan = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		System.out.println("------UsbDeviceInfoAndroidFragment---onCreateView-->");
		if (usbMan == null || usbMan.getDeviceList().get(usbKey) == null) {
			return v;
		} else {
			v = inflater.inflate(R.layout.usb_info_android, container, false);
		}

		tblUsbInfoHeader = (TableLayout) v.findViewById(R.id.tblUsbInfo_title);
		tblUsbInfoTop = (TableLayout) v.findViewById(R.id.tblUsbInfo_top);
		tblUsbInfoBottom = (TableLayout) v.findViewById(R.id.tblUsbInfo_bottom);
		tvVID = ((TextView) v.findViewById(R.id.tvVID));
		tvPID = ((TextView) v.findViewById(R.id.tvPID));
		tvProductDb = ((TextView) v.findViewById(R.id.tvProductDb));
		tvVendorDb = ((TextView) v.findViewById(R.id.tvVendorDb));
		tvDevicePath = ((TextView) v.findViewById(R.id.tvDevicePath));
		tvDeviceClass = ((TextView) v.findViewById(R.id.tvDeviceClass));

		populateAndroidTable(inflater);

		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle toSave) {
		toSave.putString(BUNDLE_USB_KEY, usbKey);
	}

	private String padLeft(String string, String padding, int size){
		String pad = "";
		while((pad+string).length() < size){
			pad += padding + pad;
		}
		return pad+string;
	}

	private void populateAndroidTable(LayoutInflater inflater){
		UsbDevice device = usbMan.getDeviceList().get(usbKey);
		tvDevicePath.setText(usbKey);

		if(device != null){  		
			tvVID.setText(padLeft(Integer.toHexString(device.getVendorId()),"0",4));
			tvPID.setText(padLeft(Integer.toHexString(device.getDeviceId()),"0",4));
			tvDeviceClass.setText(UsbConstants.resolveUsbClass(device.getDeviceClass()));

			UsbInterface iface;
			for(int i = 0 ; i < device.getInterfaceCount() ; i++){
				iface = device.getInterface(i);
				if(iface != null){

					addDataRow(inflater, tblUsbInfoBottom, getActivity().getString(R.string.interface_) + i, "");
					addDataRow(inflater, tblUsbInfoBottom, getActivity().getString(R.string.class_), UsbConstants.resolveUsbClass((iface.getInterfaceClass())));

					String endpointText = getActivity().getString(R.string.none);
					if(iface.getEndpointCount() > 0){
						UsbEndpoint endpoint;
						for(int j=0; j < iface.getEndpointCount(); j++){
							endpoint = iface.getEndpoint(j);
							endpointText = "#" + j + "\n";
							endpointText += getActivity().getString(R.string.address_) + endpoint.getAddress() + " (" + padLeft(Integer.toBinaryString(endpoint.getAddress()), "0", 8) + ")\n";
							endpointText += getActivity().getString(R.string.number_) + endpoint.getEndpointNumber() + "\n";
							endpointText += getActivity().getString(R.string.direction_) + UsbConstants.resolveUsbEndpointDirection(endpoint.getDirection()) + "\n";        					
							endpointText += getActivity().getString(R.string.type_) + UsbConstants.resolveUsbEndpointType(endpoint.getType()) + "\n";
							endpointText += getActivity().getString(R.string.poll_interval_) + endpoint.getInterval() + "\n";
							endpointText += getActivity().getString(R.string.max_packet_size_) + endpoint.getMaxPacketSize() + "\n";
							endpointText += getActivity().getString(R.string.attributes_) + padLeft(Integer.toBinaryString(endpoint.getAttributes()), "0", 8);
							addDataRow(inflater, tblUsbInfoBottom, "\t" + getActivity().getString(R.string.endpoint_), endpointText);
						}
					} else {
						addDataRow(inflater, tblUsbInfoBottom, "\tEndpoints:", "none");
					}
				}
			}
		}
	}

}

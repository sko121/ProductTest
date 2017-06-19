package com.thtfit.test;

import com.thtfit.test.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.telephony.TelephonyManager;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.Manifest;

public class IccSerialNumberActivity extends Activity  {

    private TextView minfo,mimei,mtel,miccid,mimsi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_iccserialnumber);
		mimei = (TextView)findViewById(R.id.tv_imei);
		mimei.setTextColor(Color.GREEN);
		mtel = (TextView)findViewById(R.id.tv_tel);
		mtel.setTextColor(Color.GREEN);
		miccid = (TextView)findViewById(R.id.tv_iccID);
		miccid.setTextColor(Color.GREEN);
		mimsi = (TextView)findViewById(R.id.imsi);
		mimsi.setTextColor(Color.GREEN);
		minfo = (TextView)findViewById(R.id.tv_info);

		requestAllPermissons();
		showInfo();
	}

	private void showInfo(){

		StringBuffer sb = new StringBuffer();
        sb.append("IMEI:       international mobile Equipment identity手机唯一标识码" + "\n");
        sb.append("MSISDN:mobile subscriber ISDN用户号码，这个是我们说的139，136那个号码,很可能为空,"+ "\n");
        sb.append("ICCID:     ICC identity集成电路卡标识，这个是唯一标识一张卡片物理号码的"+ "\n");
        sb.append("IMSI:        international mobiles subscriber identity国际移动用户号码标识"+ "\n");
        minfo.setText(sb.toString());
	}

	private void writeInfo(){
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();       //取出IMEI,international mobile Equipment identity手机唯一标识码
        mimei.setText(imei);
        String tel = tm.getLine1Number();     //取出MSISDN，很可能为空,mobile subscriber ISDN用户号码，这个是我们说的139，136那个号码；
        mtel.setText(tel);
        String iccID =tm.getSimSerialNumber();  //取出ICCID,ICC identity集成电路卡标识，这个是唯一标识一张卡片物理号码的；
        miccid.setText(iccID);
        String imsi =tm.getSubscriberId();     //取出IMSI,international mobiles subscriber identity国际移动用户号码标识，
        mimei.setText(imsi);
	}

	private void requestAllPermissons() {
			if (ContextCompat.checkSelfPermission(ProductTest.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
				writeInfo();
	        } else {
	            ActivityCompat.requestPermissions(this , new String[]{
	            		Manifest.permission.READ_PHONE_STATE,
	            		}, 1);
	        }
		}

		@Override
		public void onRequestPermissionsResult(int requestCode,
				String[] permissions, int[] grantResults) {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			switch (requestCode) {
			case 1:
				if (grantResults.length > 0
		                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
							writeInfo();
		            } else {
						finish();
		            }
				break;
			default:
				break;
			}
		}
}

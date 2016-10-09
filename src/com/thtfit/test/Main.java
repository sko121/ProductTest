package com.thtfit.test;
import com.thtfit.test.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
public class Main extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Advertising_Player";
	private Button sdramButton, flashButton, sdcardButton, usbotgButton;
	private Button touchkeyButton, gravityButton, wifiButton, bluetoothButton;
	private Button mouseButton, cameraButton, displayButton;
	private Button playButton, recorderButton, earplugButton;
	private Button generalButton, reportButton;
    private Button serialport,batteryButton;
    private Button mactobarcodeButton;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//by Lu
		Log.d("luzhaojie", "MainActivity::onCreate:============");
		
		setContentView(R.layout.main);
		flashButton = (Button)findViewById(R.id.button_flash);
		sdcardButton = (Button)findViewById(R.id.button_sdcard);
		sdramButton = (Button)findViewById(R.id.button_sdram);
		wifiButton = (Button)findViewById(R.id.button_wifi);
		bluetoothButton = (Button)findViewById(R.id.button_bluetooth);
		gravityButton = (Button)findViewById(R.id.button_gravity);
		touchkeyButton = (Button)findViewById(R.id.button_touchkey);
		mouseButton = (Button)findViewById(R.id.button_mouse);
		displayButton = (Button)findViewById(R.id.button_display);
		recorderButton = (Button)findViewById(R.id.button_recorder);
		playButton = (Button)findViewById(R.id.button_play);
		cameraButton = (Button)findViewById(R.id.button_camera);
		usbotgButton = (Button)findViewById(R.id.button_usbhost);
		generalButton = (Button)findViewById(R.id.button_general);
		earplugButton = (Button)findViewById(R.id.button_earplug);
		reportButton = (Button)findViewById(R.id.button_report);
        serialport = (Button)findViewById(R.id.button_serialport);
        batteryButton = (Button)findViewById(R.id.button_battery);
        mactobarcodeButton = (Button)findViewById(R.id.button_mactobarcode);

		flashButton.setOnClickListener(this);
		sdcardButton.setOnClickListener(this);
		sdramButton.setOnClickListener(this);
		wifiButton.setOnClickListener(this);
		bluetoothButton.setOnClickListener(this);
		displayButton.setOnClickListener(this);
		mouseButton.setOnClickListener(this);
		recorderButton.setOnClickListener(this);
		playButton.setOnClickListener(this);
		usbotgButton.setOnClickListener(this);
		generalButton.setOnClickListener(this);
		touchkeyButton.setOnClickListener(this);
		gravityButton.setOnClickListener(this);
		earplugButton.setOnClickListener(this);
		cameraButton.setOnClickListener(this);
		reportButton.setOnClickListener(this);

        serialport.setOnClickListener(this);
		batteryButton.setOnClickListener(this);
		
		mactobarcodeButton.setOnClickListener(this);

	}
    @Override
    public void onClick(View v) {
   	if(v.equals(flashButton)) {
		startActivity(new Intent(Main.this, NandflashActivity.class));
		Log.d(LOG_TAG, "flash");
   	}else if(v.equals(sdcardButton)) {
		startActivity(new Intent(Main.this, SdcardActivity.class));
		Log.d(LOG_TAG, "sdcard");
   	}else if(v.equals(sdramButton)) {	
		startActivity(new Intent(Main.this, MemoryActivity.class));
		Log.d(LOG_TAG, "memory");
    }else if(v.equals(gravityButton)) {
		startActivity(new Intent(Main.this, GravityActivity.class));
		Log.d(LOG_TAG, "gravity");
	}else if(v.equals(wifiButton)) {
		startActivity(new Intent(Main.this, WifiActivity.class));
		Log.d(LOG_TAG, "wifi");
	}else if(v.equals(bluetoothButton)) {
		/*Intent mIntent = new Intent();
       	ComponentName comp = new ComponentName("com.android.settings",
						"com.android.settings.Settings");
   	    mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.MAIN");
       	startActivity(mIntent);*/
		startActivity(new Intent(Main.this, BluetoothActivity.class));
		Log.d(LOG_TAG, "bluetooth");
	}else if(v.equals(touchkeyButton)) {
		startActivity(new Intent(Main.this, TouchkeyActivity.class));
		Log.d(LOG_TAG, "TouchkeyActivity");
	}else if(v.equals(mouseButton)) {
		startActivity(new Intent(Main.this, MouseActivity.class));
		Log.d(LOG_TAG, "mouse");
	}else if(v.equals(displayButton)) {
        startActivity(new Intent(Main.this, DisplayActivity.class));
        Log.d(LOG_TAG, "display");
	}else if(v.equals(playButton)) {
        startActivity(new Intent(Main.this, PlayActivity.class));
	}else if(v.equals(recorderButton)) {
        startActivity(new Intent(Main.this, RecorderActivity.class));
		Log.d(LOG_TAG, "recorder");
	}else if(v.equals(cameraButton)) {
   		startActivity(new Intent(Main.this, CameraActivity.class)); 
		Log.d(LOG_TAG, "camera");
	}else if(v.equals(earplugButton)){
		startActivity(new Intent(Main.this, EarplugActivity.class));
		Log.d(LOG_TAG, "earplug");
	}else if(v.equals(usbotgButton)) {
        startActivity(new Intent(Main.this, UsbhostActivity.class));
		Log.d(LOG_TAG, "usbotg");
    }else if(v.equals(generalButton)){
        startActivity(new Intent(Main.this, GeneralActivity.class));
        Log.d(LOG_TAG, "general");
    }else if(v.equals(reportButton)){
        startActivity(new Intent(Main.this, ReportActivity.class));
        Log.d(LOG_TAG, "report");
	}else if(v.equals(serialport)){
		try{
			Intent mIntent = new Intent();
			ComponentName comp = new ComponentName("android_serialport_api.sample",
					"android_serialport_api.sample.MainActivity");
			mIntent.setComponent(comp);		
			startActivity(mIntent);
		    Log.d(LOG_TAG, "serialport");

			}catch(Exception e){

				Toast.makeText(this, "Don't found class", Toast.LENGTH_LONG).show();
			}
    }else if(v.equals(batteryButton)){
    	startActivity(new Intent(Main.this, BatteryActivity.class));
		Log.d(LOG_TAG, "battery");

	}else if(v.equals(mactobarcodeButton)){
		startActivity(new Intent(Main.this,MactobarcodeActivity.class));
		Log.d(LOG_TAG,"mactobarcode");
	}
	}
}

package com.thtfit.test;

import com.thtfit.test.R;
import android.app.Activity;
import android.widget.TextView;
import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;  
import android.content.IntentFilter;  
import android.os.BatteryManager;  
import android.os.Bundle;  
import android.util.Log;
import android.os.Handler;
import android.os.Message;

public class BatteryActivity extends Activity {

	public TextView mChargerAcOnlineText;
    public TextView mChargerUsbOnlineText;
    public TextView mChargerWirelessOnlineText;
	public TextView mBatteryStatusText;
    public TextView mBatteryHealthText;
	public TextView mBatteryPresentText;
	public TextView mBatteryLevelText;
	public TextView mBatteryVoltageText;
	public TextView mBatteryCurrentNowText;
	public TextView mBatteryChargeCounterText;
	public TextView mBatteryTemperatureText;
	public TextView mBatteryTechnologyText;
	
	public boolean chargerAcOnline = false;
    public boolean chargerUsbOnline = false;
    public boolean chargerWirelessOnline = false;
    public int batteryStatus;
    public int batteryHealth;
    public boolean batteryPresent = false;
    public int batteryLevel;
    public int batteryVoltage;
    public int batteryCurrentNow;
    public int batteryChargeCounter;
    public int batteryTemperature;
    public int batteryPlugged;
    public String batteryTechnology;
    public String mBatteryStatus;
	public String mBatteryHealth;
    public String mBatteryYes,mBatteryNo;
    
   	public void onCreate(Bundle savedInstanceState){
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery);

		mChargerAcOnlineText = (TextView)findViewById(R.id.chargerAcOnlineText);
		mChargerUsbOnlineText = (TextView)findViewById(R.id.chargerUsbOnlineText);
        mChargerWirelessOnlineText = (TextView)findViewById(R.id.chargerWirelessOnlineText);
		mBatteryStatusText = (TextView)findViewById(R.id.batteryStatusText);
		mBatteryHealthText = (TextView)findViewById(R.id.batteryHealthText);
		mBatteryPresentText = (TextView)findViewById(R.id.batteryPresentText);
		mBatteryLevelText = (TextView)findViewById(R.id.batteryLevelText);
		mBatteryVoltageText = (TextView)findViewById(R.id.batteryVoltageText);
		mBatteryCurrentNowText = (TextView)findViewById(R.id.batteryCurrentNowText);
		mBatteryChargeCounterText = (TextView)findViewById(R.id.batteryChargeCounterText);
		mBatteryTemperatureText = (TextView)findViewById(R.id.batteryTemperatureText);
		mBatteryTechnologyText = (TextView)findViewById(R.id.batteryTechnologyText);
        mBatteryYes = this.getResources().getString(R.string.charger_yes);
		mBatteryNo = this.getResources().getString(R.string.charger_no);
   		
    }

    
   	@Override  
	public void onResume() {  
	super.onResume();  
	  
	IntentFilter filter = new IntentFilter();  
	  
	filter.addAction(Intent.ACTION_BATTERY_CHANGED);  
	registerReceiver(mBroadcastReceiver, filter);  
	} 

 
    
	public void updateBatteryInfo(){

		if (chargerAcOnline){
			mChargerAcOnlineText.setText(mBatteryYes);
        }else{
			mChargerAcOnlineText.setText(mBatteryNo);
        }
        if (chargerUsbOnline){
         	mChargerUsbOnlineText.setText(mBatteryYes);
		}else{
    		mChargerUsbOnlineText.setText(mBatteryNo);
        }
		if	(chargerWirelessOnline){
 			mChargerWirelessOnlineText.setText(mBatteryYes);
		}else{
			mChargerWirelessOnlineText.setText(mBatteryNo);
		}
		mBatteryStatusText.setText(mBatteryStatus);
		mBatteryHealthText.setText(mBatteryHealth);
		mBatteryPresentText.setText(String.valueOf(batteryPresent));
		mBatteryLevelText.setText(String.valueOf(batteryLevel));
		mBatteryVoltageText.setText(String.valueOf(batteryVoltage));
		mBatteryCurrentNowText.setText(String.valueOf(batteryCurrentNow));
		mBatteryChargeCounterText.setText(String.valueOf(batteryChargeCounter));
		mBatteryTemperatureText.setText(String.valueOf(batteryTemperature));
		mBatteryTechnologyText.setText(batteryTechnology);
    }
	@Override  
	public void onPause() {  
		super.onPause();  
		  
		unregisterReceiver(mBroadcastReceiver);  
	} 
 		
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		@Override  
		public void onReceive(Context context, Intent intent) {

		    chargerAcOnline = false;
            chargerUsbOnline = false;
            chargerWirelessOnline = false;

			String action = intent.getAction();
  			if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
   				batteryStatus = intent.getIntExtra("status", 0); 
				batteryHealth = intent.getIntExtra("health", 0);
				batteryPresent = intent.getBooleanExtra("present", false);
				batteryLevel = intent.getIntExtra("level", 0);
				batteryVoltage = intent.getIntExtra("voltage", 0);
				batteryCurrentNow = intent.getIntExtra("current_now",0);
				batteryChargeCounter = intent.getIntExtra("charge_counter",0);
				batteryTemperature =  intent.getIntExtra("temperature", 0);
				batteryTechnology = intent.getStringExtra("technology");
                batteryPlugged = intent.getIntExtra("plugged",0);
                
				switch (batteryStatus) {
		            case BatteryManager.BATTERY_STATUS_UNKNOWN:
		                mBatteryStatus = "unknown";
		                break;
		            case BatteryManager.BATTERY_STATUS_CHARGING:
		                mBatteryStatus = "charging";
		                break;
		            case BatteryManager.BATTERY_STATUS_DISCHARGING:
		                mBatteryStatus = "discharging";
		                break;
		            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
		                mBatteryStatus = "not charging";
		                break;
		            case BatteryManager.BATTERY_STATUS_FULL:
		                mBatteryStatus = "full";
		                break;
		            }

                switch (batteryHealth) {
		            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
		                mBatteryHealth = "unknown";
		                break;
		            case BatteryManager.BATTERY_HEALTH_GOOD:
		                mBatteryHealth = "good";
		                break;
		            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
		                mBatteryHealth = "overheat";
		                break;
		            case BatteryManager.BATTERY_HEALTH_DEAD:
		                mBatteryHealth = "dead";
		                break;
		            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
		                mBatteryHealth = "voltage";
		                break;
		            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
		                mBatteryHealth = "unspecified failure";
		                break;
                }
                switch (batteryPlugged) {
		            case BatteryManager.BATTERY_PLUGGED_AC:
		                 chargerAcOnline = true;
		                 break;
		            case BatteryManager.BATTERY_PLUGGED_USB:
		                 chargerUsbOnline = true;
		                 break;
		            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
						 chargerWirelessOnline = true;
		                 break;
		            default:
					 	break;
                }
                updateBatteryInfo();
			}
		 
		} 
	};
   	
}

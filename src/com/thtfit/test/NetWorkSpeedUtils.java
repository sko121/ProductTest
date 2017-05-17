package com.thtfit.test;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class NetWorkSpeedUtils {
	private Context context;  
    private Handler mHandler;  
  
    private long lastTotalRxBytes = 0;  
    private long lastTimeStamp = 0;  
    int i = 0;
    int v1=0;
    float v2=0;
    private static NetWorkSpeedUtils netWorkSpeedUtils;
    public static NetWorkSpeedUtils getInstance(){
    	if(netWorkSpeedUtils==null){
    		netWorkSpeedUtils=new NetWorkSpeedUtils();
    	}
		return netWorkSpeedUtils;
    	
    }
   
	public Timer getTimer() {
		return timer;
	}


	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	private  Timer timer=new Timer();
	
    
	
    public NetWorkSpeedUtils(Context context, Handler mHandler){  
        this.context = context;  
        this.mHandler = mHandler;  
    }  
  
    /**
	 * 
	 */
	public NetWorkSpeedUtils() {
		// TODO Auto-generated constructor stub
	}

	TimerTask task = new TimerTask() {  
        @Override  
        public void run() {  
            showNetSpeed();  
        }  
    };  
   
   
  
    public void startShowNetSpeed(){  
        lastTotalRxBytes = getTotalRxBytes();  
        lastTimeStamp = System.currentTimeMillis();  
        timer.schedule(task, 1000, 1000); // 1s后启动任务 
   
  
    }  
   
  
    private long getTotalRxBytes() {  
        return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);//转为KB  
    }  
  
    private void showNetSpeed() {  
        long nowTotalRxBytes = getTotalRxBytes();  
        long nowTimeStamp = System.currentTimeMillis();  
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换  
        long speed2 = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 % (nowTimeStamp - lastTimeStamp));//毫秒转换  
  
        lastTimeStamp = nowTimeStamp;  
        lastTotalRxBytes = nowTotalRxBytes;  
  
        Message msg = mHandler.obtainMessage();  
        msg.what = 100;  
        msg.obj = String.valueOf(speed) + "." + String.valueOf(speed2) + " KB/s";   
       
       String sp2="0."+speed2;
       float spp2=Float.valueOf(sp2);
       
        i++;         
        v1=(int) (v1+speed);
        v2=(float) (v2+spp2);
        
        msg.arg1=(int) v1;
        
        
        Bundle bundle=new Bundle();
        bundle.putFloat("v2", v2);
        bundle.putInt("i", i);
        msg.setData(bundle);
        
       
        mHandler.sendMessage(msg);//更新界面 

        
       
    }  
  

}

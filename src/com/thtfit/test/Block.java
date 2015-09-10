package com.thtfit.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.DataInputStream;

public class Block {
	public final static String LOG_TAG = "Adversiting-Block";
	private Handler mHandle = null;
	private	final String srcfile="/mnt/sdcard/SourceFile";
	public static volatile boolean forceStop = false;
	public final static int StopMessage = 10086;
	int status = -1;
	
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;

	public static final int MEMORY_SUCCESS = 100;
	public static final int MEMORY_NOMEM = 112;
	public static final int MEMORY_COPY_ERROR = 101;

	public static final int NANDFLASH_SUCCESS = 200;
	public static final int NANDFLASH_NOSPC = 228;
	public static final int NANDFLASH_NOMEM = 212;
	public static final int NANDFLASH_EACCESS = 213;
	public static final int NANDFLASH_COPY_ERROR = 201;

	public static final int EXTSD_SUCCESS = 300;	 
	public static final int EXTSD_NOT_EXIST = 306;	//ENOXIO	6
	public static final int EXTSD_NOT_ENOUGH_SPACE = 328;	//ENOSPC	28
	public static final int EXTSD_COPY_ERROR = 332;		//EPIPE		32
	public static final int EXTSD_FIALED = 312;			//ENOMEM ??? 12

	public static final int USBHOST_SUCCESS = 400; 
	public static final int USBHOST_NOT_EXIST = 406;
	public static final int USBHOST_NOT_ENOUGH_SPACE = 428;
	public static final int USBHOST_COPY_ERROR = 432;
	public static final int USBHOST_FIALED = 412;

	public Block(Handler handle){
		blockInit();
		mHandle = handle;
	}
	
    private String RootCmd(String cmd){
    	Process process = null;
        DataOutputStream dos = null;
		DataInputStream dis = null; 
		String line = null;
		String result = "";
		String ROOT_KEY = "root_1nbgetaqz37#@159";
		int ret;
        try{/*
			if(Build.FIRMWARE.equals("v3.2")){
				process = Runtime.getRuntime().exec("su");	
			} else {*/
           	process = Runtime.getRuntime().exec("su "+ROOT_KEY);
            Log.d(LOG_TAG, "cmd: "+cmd);
            dos = new DataOutputStream(process.getOutputStream());
			dis = new DataInputStream(process.getInputStream());
            dos.writeBytes(cmd+ "\n");
            dos.writeBytes("exit\n");
            dos.flush();
            while ((line = dis.readLine()) != null) {
                Log.d("result", line);
                result += line;
            }
            process.waitFor();
			status = process.exitValue();
        } catch (Exception e) {
        } finally {
            try {
                if(dos != null)
                    dos.close();
				if(dis != null)
					dis.close();
                process.destroy();
            } catch (Exception e) {
            }
        }
        return result;
    }
	
	public int blockInit(){
		return native_initBlock();
	}
	public synchronized int copyMemBlock(){
		return native_copyMemBlock(0);
	}
	public synchronized int copyNandBlock(){
		return native_copyNandBlock(0);
	}
	private void sendLocalMessage(int message){
		Message msg = Message.obtain();
		msg.what = message;
		mHandle.sendMessage(msg);
		Log.d(LOG_TAG, "send message: "+message);
	}
	public synchronized int memoryInspection(){
		int ret = native_copyMemBlock(0);	
		if(forceStop){
			sendLocalMessage(StopMessage);
			return -1;
		}else{
			ret = 100 - ret;
			sendLocalMessage(ret);
			return 0;
		}
	}
	public synchronized int nandflashInspection(){
		int ret = native_copyNandBlock(0);
		if(forceStop){
			sendLocalMessage(StopMessage);
			return -1;
		}
		else{
			ret = 200 - ret;
			sendLocalMessage(ret);
			return 0;
		}
	}
	public synchronized int sdcardInspection(){
		String disk = "extsd";
		int ret = externInspection(disk);
		if(forceStop){
			sendLocalMessage(StopMessage);
			return -1;
		}else{
			ret = 300 - ret;
			sendLocalMessage(ret);
			return 0;
		}
	}
	public synchronized int usbhostInspection(){
		String disk = "usbhost1";
		int ret = externInspection(disk);
		if(forceStop){
			sendLocalMessage(StopMessage);
			return -1;
		}else{
			ret = 400 - ret;
			sendLocalMessage(ret);
			return 0;
		}
	}
	private synchronized int cmdexec_isDiskExist(String disk)
	{
		String cmd = "mount | grep "+disk;
        RootCmd(cmd);
		if(status != 0){
			Log.e(LOG_TAG, disk+" not find");
			return -6;//ENOXIO	6
		}	
		return 0;
	}
	private synchronized int cmdexec_isEnoughSpace(String disk){
		String cmd = "busybox df -m | grep \""+disk+"\" | busybox awk '{print $3}'";
		String result = RootCmd(cmd);
		if(status != 0){
			Log.e(LOG_TAG, disk+" not find");
			return -6;//ENOXIO  6	
		}
		Integer var = Integer.parseInt(result);
		if(var < 50){
			Log.e(LOG_TAG, disk+" not enough space");
			return -28;//ENOSPC    28
		}
		return 0;
	}
	private synchronized int cmdexec_creatSource(){
		RootCmd("dd if=/dev/urandom of="+srcfile+" bs=1048576 count=5");
		if(status != 0){
			Log.e(LOG_TAG, "creatsource fialed");
			return -28;//ENOSPC	28
		}
		return 0;
	}
	private synchronized int cmdexec_cpToDestination(String dstfile){
		RootCmd("cp -f "+srcfile+" "+dstfile);
		if(status != 0){
			Log.e(LOG_TAG, "cp Destination err");
			return -32;//EPIPE	32
		}
		return 0;
	}
	private synchronized int cmdexec_compareFile(String dstfile){
		String cmd = "md5 "+srcfile+" | busybox awk '{print $1}'";
		String srcmd5 = RootCmd(cmd);
		if(srcmd5 == ""){
			Log.e(LOG_TAG, "srcmd5 not find");
			return -32;	//EPIPE		32
		}
		cmd = "md5 "+dstfile+" | busybox awk '{print $1}'";
		String dstmd5 = RootCmd(cmd);
		if(srcmd5 == ""){
			Log.e(LOG_TAG, "dstmd5 not find");
			return -32; //EPIPE		32
		}
		if(!dstmd5.equals(srcmd5)){
			Log.e(LOG_TAG, "dstmd5 and srcmd5 not same");
			return -32;//EPIPE		32
		}
		return 0;
	}
	private synchronized void cmdexec_cleanAll(String dstfile){
		String cmd = "rm -f "+srcfile;
		RootCmd(cmd);
		cmd = "rm -f "+dstfile;
		RootCmd(cmd);
	}
	public synchronized int externInspection(String disk){
		int ret;
		String dstfile="/mnt/"+disk+"/DestinationFile";
		Log.d(LOG_TAG, "exec "+disk+"Inspection");

		ret = cmdexec_isDiskExist(disk);
		if(forceStop || ret < 0){
			return ret;
		}
		ret = cmdexec_isEnoughSpace(disk);
		if(forceStop || ret < 0){
			return ret;
		}
		ret = cmdexec_creatSource();	
		if(forceStop || ret < 0){
			return ret;
		}
		ret = cmdexec_cpToDestination(dstfile);
		if(forceStop || ret < 0){
			return ret;
		}
		ret = cmdexec_compareFile(dstfile);
		if(forceStop || ret < 0){
			return ret;
		}
		cmdexec_cleanAll(dstfile);	
		return 0;//success
	}
	public static int getMemorySize(){
		return native_getMemoryInfo();
	}
	public static int getFlashSize(){
		int value = native_getNandflashInfo();
		Log.v(LOG_TAG, "value :"+value+" M");
		return value;
	}
	public native int native_initBlock();
	public native int native_copyMemBlock(int arg);
	public native int native_copyNandBlock(int arg);
	public native int native_copySdCard(int arg);
	public static native int native_getMemoryInfo();
	public static native int native_getNandflashInfo();

	static {
        System.loadLibrary("block_jni");
    }
}

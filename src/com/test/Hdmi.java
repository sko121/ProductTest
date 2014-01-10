package com.test;

import android.util.Log;
import java.util.*;

public class Hdmi {
	public int mResolution;

	public Hdmi(){
		hdmiInit();
	}
/*
	public ~Hdmi(){
		hdmiStop();
	}*/
	static {
                System.loadLibrary("disp_jni");
        }
	public native int native_initDisp();
        public native int native_startDisp();
        public native int native_closeDisp();
	public native int native_setHdmiResolution(int resolution);
	
	public int hdmiInit()
	{
		return native_initDisp();
	}
	public int hdmiOn()
	{
		return native_startDisp();
	}
	public int hdmiOff()
	{
		return native_closeDisp();
	}
	public void hdmiSetResolution(int resolution)
	{
		hdmiOff();
		native_setHdmiResolution(resolution);
		hdmiOn();
	}
}

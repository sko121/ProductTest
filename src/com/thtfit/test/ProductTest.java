package com.thtfit.test;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ProductTest extends Application{
	private final static String LOG_TAG = "Report";
	private final String report = "/mnt/sdcard/report";
	public TreeMap<Integer, String> mTreeMap;
	List<Map<String, Object>> reportList;
	
	//by Lu 
	private static Context pt;
	@Override
	public void onCreate() {
		super.onCreate();
		if(pt == null){
			pt = getApplicationContext();
			Log.d("luzhaojie", "ProductTest::Application onCreate:pt == " + pt);
			try {
				ProductTest1();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	public static Context getContext() {
		return pt;
	}

	//create mTreeMap for build (errno) and (error message) Mapper tree
	public void ProductTest1() throws NameNotFoundException{
		mTreeMap = new TreeMap<Integer, String>();
		reportList = new ArrayList<Map<String,Object>>();
		reportList.clear();
		MemoryActivity.createHashTable(mTreeMap);
		MemoryActivity.createTitle(reportList, 0);

		NandflashActivity.createHashTable(mTreeMap);
		NandflashActivity.createTitle(reportList, 1);

		SdcardActivity.createHashTable(mTreeMap);
		Map<String,Object> map3 =new HashMap<String,Object>();
		map3.put("title", "SD卡测试");
		reportList.add(2, map3);

		UsbhostActivity.createHashTable(mTreeMap);
		Map<String,Object> map4 =new HashMap<String,Object>();
		map4.put("title", "OTG测试");
		reportList.add(3, map4);

		Map<String,Object> map5 =new HashMap<String,Object>();
		map5.put("title", "按键测试");
		reportList.add(4, map5);

		Map<String,Object> map6 =new HashMap<String,Object>();
		map6.put("title", "重力测试");
		reportList.add(5, map6);

		Map<String,Object> map7 =new HashMap<String,Object>();
		map7.put("title", "WIFI测试");
		reportList.add(6, map7);

		Map<String,Object> map8 =new HashMap<String,Object>();
		map8.put("title", "蓝牙测试");
		reportList.add(7, map8);

		MouseActivity.createHashTable(mTreeMap);
		Map<String,Object> map9 =new HashMap<String,Object>();
		map9.put("title", "触摸屏测试");
		reportList.add(8, map9);

		CameraActivity.createTitle1(reportList, 9); // by Lu
//		getPermissionToCreateTitle( reportList, 9); // by Lu

		DisplayActivity.createHashTable(mTreeMap);
		Map<String,Object> map11 =new HashMap<String,Object>();
		map11.put("title", "显示屏测试");
		reportList.add(10, map11);
//		reportList.add(9, map11);//by Lu

		Map<String,Object> map12 =new HashMap<String,Object>();
		map12.put("title", "音乐测试");
		reportList.add(11, map12);
//		reportList.add(10, map12);//by Lu

		Map<String,Object> map13 =new HashMap<String,Object>();
		map13.put("title", "录音测试");
		reportList.add(12, map13);
//		reportList.add(11, map13);//by Lu

		Map<String,Object> map14 =new HashMap<String,Object>();
		map14.put("title", "耳塞测试");
		reportList.add(13, map14);
//		reportList.add(12, map14);//by Lu
	}
	public String getTitle(int position)
	{
		String str = "";
		String key = "title";
		Map<String,Object> map = reportList.get(position);
		if(map.containsKey(key)){
			str = map.get(key).toString();
		}
		return str;
	}
	public void addKeyEvent(int position, String key){
		Map<String,Object> map = reportList.get(position);
		if(map.containsKey(key)){
			int val = Integer.parseInt(map.get(key).toString());
			val++;
			map.put(key, val);
			Log.v(LOG_TAG, "key:"+key+"val:"+val);
		}else{
			map.put(key, 1);
			Log.v(LOG_TAG, "key:"+key+"val:1");
		}
	}
	public void addKeyEvent(int index){
		int position = index / 100;
		position = position - 1;
		String key = mTreeMap.get(index);
		if(position > 13 || position < 0){
			Log.e(LOG_TAG, "position err");
			return;
		}
		Map<String,Object> map = reportList.get(position);
		if(map.containsKey(key)){
			int val = Integer.parseInt(map.get(key).toString());
			val++;
			map.put(key, val);
			Log.v(LOG_TAG, "key:"+key+"val:"+val);
		}else{
			map.put(key, 1);
			Log.v(LOG_TAG, "key:"+key+"val:1");
		}
	}
}

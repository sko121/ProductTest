package com.thtfit.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View; 
import android.view.View.OnClickListener; 
import android.view.WindowManager;
import android.widget.Button; 
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import android.content.res.Resources.NotFoundException;

public class DisplayActivity extends Activity 
{
	private final static String LOG_TAG = "Display";
	private static String STRING_DISPLAY_SUCCESS = "显示屏测试成功";
	private static String STRING_DISPLAY_FAILED = "显示屏测试失败";
    private static String DISPLAY = "Display";
	private static String DISPLAY_YES ="Yes";
	private static String DISPLAY_NO = "No";
	private static String DISPLAY_TEST = "Screen test (resolution:";
	private static String CURRENT_RESOLUTION = "Current resolution:";
	private static String DISPLAY_IS_SUCCESS = "Whether is the display successful";
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	private TextView mText;
	private Button curResButton, screenCheckButton;
	private ProductTest mProduct;
	private AlertDialog mDialog;

	protected void onCreate(Bundle savedInstanceState){
   	    super.onCreate(savedInstanceState);
       	setContentView(R.layout.display);

		mText = (TextView) findViewById(R.id.message1);	
		curResButton = (Button)findViewById(R.id.button_currentResolution);
		screenCheckButton = (Button)findViewById(R.id.button_screenCheck);
		curResButton.setOnClickListener(viewlisten);
		screenCheckButton.setOnClickListener(viewlisten);
		mProduct = (ProductTest)getApplication();

		try{
            STRING_DISPLAY_SUCCESS = this.getResources().getString(R.string.display_success);
            STRING_DISPLAY_FAILED = this.getResources().getString(R.string.display_failed);
            DISPLAY = this.getResources().getString(R.string.display);
			DISPLAY_TEST = this.getResources().getString(R.string.display_test);
			CURRENT_RESOLUTION = this.getResources().getString(R.string.current_resolution);
			DISPLAY_YES = this.getResources().getString(R.string.yes);
            DISPLAY_NO = this.getResources().getString(R.string.no);
            DISPLAY_IS_SUCCESS =  this.getResources().getString(R.string.display_is_success);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }
	}
	@Override
	protected void onRestart(){
		super.onRestart();
		showConfirmDialog();
		Log.e(LOG_TAG, "display restart");
	}
	public static void createHashTable(TreeMap<Integer, String> treemap)
	{
		treemap.put(1100, STRING_DISPLAY_SUCCESS);
		treemap.put(1105, STRING_DISPLAY_FAILED);
	}
	public void createTitle(List list, int position)
	{
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("title", DISPLAY_TEST + metrics.widthPixels+"*"+metrics.heightPixels+")");
		list.add(position, map);
	}
	OnClickListener viewlisten = new OnClickListener(){
		public void onClick(View v) {
			if(v.equals(curResButton)){
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
				mText.setTextColor(Color.GREEN);
				mText.setText(CURRENT_RESOLUTION + metrics.widthPixels+"*"+metrics.heightPixels);
				Log.d(LOG_TAG, "start"+metrics.toString());
			}else if(v.equals(screenCheckButton)){
				screenTest();
				//fullScreenChange();	
				Log.d(LOG_TAG, "screenCheckButton");
			}
		}
	};
	private void screenTest(){
		startActivity(new Intent(this, ScreenActivity.class));
	}
	public void fullScreenChange() {
		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean fullScreen = mPreferences.getBoolean("fullScreen", false);
		WindowManager.LayoutParams attrs = getWindow().getAttributes(); 
		Log.d(LOG_TAG, "fullScreen的值:" + fullScreen);
		if (fullScreen) {
			attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN); 
			getWindow().setAttributes(attrs); 
			//取消全屏设置
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			mPreferences.edit().putBoolean("fullScreen", false).commit() ;
		} else {
			attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN; 
			getWindow().setAttributes(attrs); 
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); 
			mPreferences.edit().putBoolean("fullScreen", true).commit();
		}
	}
	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle(DISPLAY)
			.setPositiveButton(DISPLAY_YES, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_DISPLAY_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(1100);
				}
			})
			.setNegativeButton(DISPLAY_NO, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_DISPLAY_FAILED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(1105);
				}
			})
			.create();
		mDialog.setMessage(DISPLAY_IS_SUCCESS);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
}

package com.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.System;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.TreeMap;
public class MouseActivity extends Activity implements OnLongClickListener{
	private final static String LOG_TAG = "Mouse";
	private String TIP_TOUCHSCREEN = "请尽可能触摸屏幕的所有位置以检测是否存在死角";
	private final static String STRING_LEFT_KEYDOWN = "单击此键成功";
	private final static String STRING_LONG_KEYDOWN = "长按此键成功";
	private final static String STRING_TOUCHSCREEN_SUCCESS = "触摸屏测试成功";
	private final static String STRING_TOUCHSCREEN_FAILED = "触摸屏测试失败";
	private TextView mText, mTip;
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	//private int textSize = 200;
	private Button LeftButton, RightButton, DragButton;
	private ContentResolver resolver;
	private ProductTest mProduct;
	private AlertDialog mDialog = null;

	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mouse);
		resolver = this.getContentResolver();
        mText = (TextView) findViewById(R.id.textResult); 
		mTip = (TextView) findViewById(R.id.textTip);
		LeftButton = (Button)findViewById(R.id.button_left);
		RightButton = (Button)findViewById(R.id.button_right);
		DragButton = (Button)findViewById(R.id.button_drag);
		LeftButton.setOnClickListener(viewlisten);
		RightButton.setOnLongClickListener(this);
		DragButton.setOnClickListener(viewlisten);
        //mText.setTextSize(textSize);  
		mProduct = (ProductTest)getApplication();
    }
	protected void onStop(){
		super.onStop();
		Settings.System.putInt(resolver, Settings.System.POINTER_LOCATION, 0);	
	}
	public static void createHashTable(TreeMap<Integer, String> treemap)
	{
		treemap.put(900, STRING_TOUCHSCREEN_SUCCESS);
		treemap.put(905, STRING_TOUCHSCREEN_FAILED);
		treemap.put(910, STRING_LEFT_KEYDOWN);
		treemap.put(920, STRING_LONG_KEYDOWN);
	}
	OnClickListener viewlisten = new OnClickListener(){
	public void onClick(View v) {
		if(v.equals(LeftButton)){
			mText.setTextColor(okColor);
			mText.setText(STRING_LEFT_KEYDOWN);
			mProduct.addKeyEvent(910);
		}else if(v.equals(DragButton)){
			mTip.setText(TIP_TOUCHSCREEN);
			int value = Settings.System.getInt(resolver,
                        	Settings.System.POINTER_LOCATION, 0);	
			if(value == 0){
				Settings.System.putInt(resolver,
                        	Settings.System.POINTER_LOCATION, 1);
			}else{
				showConfirmDialog();
				Settings.System.putInt(resolver,
                        	Settings.System.POINTER_LOCATION, 0);
			}
			Log.d(LOG_TAG, "pointer_location: "+value);	
		}
	}};	
	public boolean onLongClick(View v) {
		if(v.equals(RightButton)){
			mText.setTextColor(okColor);
			mText.setText(STRING_LONG_KEYDOWN);
			mProduct.addKeyEvent(920);
		}
		return true;
	}
	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle("触摸屏")
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_TOUCHSCREEN_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(900);
				}
			})
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_TOUCHSCREEN_FAILED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(905);
				}
			})
			.create();
		mDialog.setMessage("触摸屏是否测试成功");
		mDialog.show();
	}
}

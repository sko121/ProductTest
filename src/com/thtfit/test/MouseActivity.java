package com.thtfit.test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.System;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.content.res.Resources.NotFoundException;

import java.util.HashMap;
import java.util.TreeMap;

import com.ckl.PointerLocation.PointerLocation;
public class MouseActivity extends Activity implements OnLongClickListener{
	private final static String LOG_TAG = "Mouse";
	private String TIP_TOUCHSCREEN = "请尽可能触摸屏幕的所有位置以检测是否存在死角";
	private static String STRING_LEFT_KEYDOWN = "单击此键成功";
	private static String STRING_LONG_KEYDOWN = "长按此键成功";
	private static String STRING_TOUCHSCREEN_SUCCESS = "触摸屏测试成功";
	private static String STRING_TOUCHSCREEN_FAILED = "触摸屏测试失败";
	private static String TOUCHSCREEN = "Touchscreen";
	private static String TOUCHSCREEN_YES = "Yes";
	private static String TOUCHSCREEN_NO = "No";
	private static String TOUCHSCREEN_IS_SUCCESS ="Whether is the touchscreen successful";
	private TextView mText, mTip;
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	//private int textSize = 200;
	private Button LeftButton, RightButton, DragButton;
	private Button confirmButton;
	private ContentResolver resolver;
	private ProductTest mProduct;
	private AlertDialog mDialog = null;
	private static final int REQUEST_CODE = 101;

	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mouse);
        
        //by Lu
        grantedPermission2();
        
		resolver = this.getContentResolver();
        mText = (TextView) findViewById(R.id.textResult); 
		mTip = (TextView) findViewById(R.id.textTip);
		LeftButton = (Button)findViewById(R.id.button_left);
		RightButton = (Button)findViewById(R.id.button_right);
		DragButton = (Button)findViewById(R.id.button_drag);
		confirmButton = (Button)findViewById(R.id.button_confirm);
		LeftButton.setOnClickListener(viewlisten);
		RightButton.setOnLongClickListener(this);
		DragButton.setOnClickListener(viewlisten);
		confirmButton.setOnClickListener(viewlisten);
        //mText.setTextSize(textSize);  
		mProduct = (ProductTest)getApplication();

		try{
			TIP_TOUCHSCREEN = this.getResources().getString(R.string.tip_touchscreen);
            STRING_TOUCHSCREEN_SUCCESS = this.getResources().getString(R.string.touchscreen_success);
            STRING_TOUCHSCREEN_FAILED = this.getResources().getString(R.string.touchscreen_failed);
			STRING_LEFT_KEYDOWN = this.getResources().getString(R.string.left_keydown);
            STRING_LONG_KEYDOWN = this.getResources().getString(R.string.long_keydown);
            TOUCHSCREEN = this.getResources().getString(R.string.bluetooth);
			TOUCHSCREEN_YES = this.getResources().getString(R.string.yes);
            TOUCHSCREEN_NO = this.getResources().getString(R.string.no);
            TOUCHSCREEN_IS_SUCCESS =  this.getResources().getString(R.string.touchscreen_is_success);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }
    }
	//by Lu
	public void grantedPermission() {
		if (ContextCompat.checkSelfPermission(ProductTest.getContext(), 
        		Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED &&
        		ContextCompat.checkSelfPermission(ProductTest.getContext(), 
                		Manifest.permission.WRITE_SYNC_SETTINGS) == PackageManager.PERMISSION_GRANTED &&
                		ContextCompat.checkSelfPermission(ProductTest.getContext(), 
                        		Manifest.permission.READ_SYNC_SETTINGS) == PackageManager.PERMISSION_GRANTED &&
                        		ContextCompat.checkSelfPermission(ProductTest.getContext(), 
                                		Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED &&
                                		ContextCompat.checkSelfPermission(ProductTest.getContext(), 
                                        		Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) == PackageManager.PERMISSION_GRANTED &&
                                        		ContextCompat.checkSelfPermission(ProductTest.getContext(), 
                                                		Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED 
        		) {
        	Log.d("luzhaojie", "have WRITE_SETTINGS permission");
        } else {
        	Log.d("luzhaojie", "no WRITE_SETTINGS permission");
            ActivityCompat.requestPermissions(this, new String[]{
            		Manifest.permission.WRITE_SETTINGS,
            		Manifest.permission.WRITE_SYNC_SETTINGS,
            		Manifest.permission.READ_SYNC_SETTINGS,
            		Manifest.permission.MODIFY_AUDIO_SETTINGS,
            		Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            		Manifest.permission.WRITE_EXTERNAL_STORAGE
            		}, 1);
        }
	}
	//by Lu
	public void grantedPermission2() {
		 if(!Settings.System.canWrite(this)){
	        	Log.d("luzhaojie", "MouseActivity :: onCreate : ifSystemCanWrite ===========");
	            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
	                      Uri.parse("package:" + getPackageName()));
	            startActivityForResult(intent, REQUEST_CODE);
	       } 
	}
	//by Lu
	  @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  Log.d("luzhaojie", "MouseActivity :: onActivityResult ============");
          if (requestCode == REQUEST_CODE) {
              if (Settings.System.canWrite(this)) {
                  //检查返回结果
                  Toast.makeText(MouseActivity.this, "WRITE_SETTINGS permission granted", Toast.LENGTH_SHORT).show();
              } else {
                  Toast.makeText(MouseActivity.this, "WRITE_SETTINGS permission not granted", Toast.LENGTH_SHORT).show();
              }
          }
      }
	protected void onStop(){
		super.onStop();
//		Settings.System.putInt(resolver, Settings.System.POINTER_LOCATION, 0);	 //by Lu
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
//			onClickDragButton();
			startPointerLocation();
		}else if(v.equals(confirmButton)){
			showConfirmDialog();
		}
	}};	
	//by Lu
	public void startPointerLocation() {
		startActivity(new Intent(this, PointerLocation.class));
	}
	//by Lu
	public void onClickDragButton() {
//		try { //by Lu
			int value = Settings.System.getInt(resolver, Settings.System.POINTER_LOCATION, 0);	//SHOW_TOUCHES
			boolean canWrite = Settings.System.canWrite(ProductTest.getContext());
			Log.d("luzhaojie", "MouseActivity::onClick:Settings.System.canWrite == " + canWrite); //true
			if(value == 0){
					Settings.System.putInt(resolver, Settings.System.POINTER_LOCATION, 1);
			}else{
					Settings.System.putInt(resolver, Settings.System.POINTER_LOCATION, 0);
			}
			Log.d(LOG_TAG, "pointer_location: "+value);	
//		} catch (Exception e) {
//			Log.d("luzhaojie", e.toString());
//		}
//		Log.d("luzhaojie", "after e.toString......");
	}
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
			.setTitle(TOUCHSCREEN)
			.setPositiveButton(TOUCHSCREEN_YES, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_TOUCHSCREEN_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(900);
				}
			})
			.setNegativeButton(TOUCHSCREEN_NO, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(STRING_TOUCHSCREEN_FAILED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(905);
				}
			})
			.create();
		mDialog.setMessage(TOUCHSCREEN_IS_SUCCESS);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
}

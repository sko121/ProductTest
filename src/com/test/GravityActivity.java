package com.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.WindowManagerGlobal;
import android.view.IWindowManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.view.RotationPolicy;

public class GravityActivity extends Activity
{
	private final static String LOG_TAG = "Recorder";
	private final static String GRAVITY_SUCCESS = "重力感应测试成功";
	private final static String GRAVITY_FIALED = "重力感应测试失败";
	private int defaultColor = Color.BLACK;
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;

	private Button confirmButton;
	private TextView mText;
	private ProductTest mProduct;
	private AlertDialog mDialog = null;
	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gravity);
		mProduct = (ProductTest)getApplication();
		confirmButton = (Button)findViewById(R.id.button_confirm);
		confirmButton.setOnClickListener(viewlisten);
		mText = (TextView)findViewById(R.id.message1);
		/*try {
			IWindowManager wm = WindowManagerGlobal.getWindowManagerService();
			wm.thawRotation();
		} catch (RemoteException exc) {
			Log.d("Gravity", "Unable to save auto-rotate setting");
		}*/
		//if(RotationPolicy.isRotationLocked(this))
		//	Log.d("Activity", "isRotation");
			//RotationPolicy.setRotationLock(this, false);
	}
	OnClickListener viewlisten = new OnClickListener(){
		public void onClick(View v) {
			if(v.equals(confirmButton)){
				showConfirmDialog();
			}
		}
	};
	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle("重力感应")
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mText.setText(GRAVITY_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(5, GRAVITY_SUCCESS);
				}
			})
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(GRAVITY_FIALED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(5, GRAVITY_FIALED);
				}
			})
			.create();
		mDialog.setMessage("录音是否测试成功");
		mDialog.show();
	}
}

package com.thtfit.test;

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
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import com.android.internal.view.RotationPolicy;

public class GravityActivity extends Activity
{
	private final static String LOG_TAG = "Recorder";
	private static String GRAVITY_SUCCESS = "重力感应测试成功";
	private static String GRAVITY_FIALED = "重力感应测试失败";
	private static String GRAVITY ="Gravity";
	private static String GRAVITY_YES ="Yes";
	private static String GRAVITY_NO ="No";
  	private static String GRAVITY_IS_SUCCESS ="Whether is the gravity successful";
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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

		try{
            GRAVITY_SUCCESS= this.getResources().getString(R.string.gravity_success);
            GRAVITY_FIALED = this.getResources().getString(R.string.gravity_failed);
            GRAVITY = this.getResources().getString(R.string.gravity);
			GRAVITY_YES = this.getResources().getString(R.string.yes);
            GRAVITY_NO = this.getResources().getString(R.string.no);
            GRAVITY_IS_SUCCESS =  this.getResources().getString(R.string.gravity_is_success);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }
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
			.setTitle(GRAVITY)
			.setPositiveButton(GRAVITY_YES, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mText.setText(GRAVITY_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(5, GRAVITY_SUCCESS);
				}
			})
			.setNegativeButton(GRAVITY_NO, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(GRAVITY_FIALED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(5, GRAVITY_FIALED);
				}
			})
			.create();
		mDialog.setMessage(GRAVITY_IS_SUCCESS);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
}

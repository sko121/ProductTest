package com.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraActivity extends Activity 
{
	private final static String LOG_TAG = "Camera";
	private final static String NO_CAMERA = "没有摄像头";
	private final static String CAMERA_SUCCESS = "摄像头测试成功";
	private final static String CAMERA_FAILED = "摄像头测试失败";
	final private int okColor = Color.GREEN;
	final private int errColor = Color.RED;
	
	private static boolean isExistCamera;
	private TextView mText, titleText;
	private Button startButton;
	private ProductTest mProduct;
	private AlertDialog mDialog = null;

	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
		mProduct = (ProductTest)getApplication();

		titleText = (TextView)findViewById(R.id.textCamera);
		titleText.setText(mProduct.getTitle(9));
		mText = (TextView)findViewById(R.id.message1);
		startButton = (Button)findViewById(R.id.button_start);
		startButton.setOnClickListener(viewlisten);
	}
	@Override
	protected void onRestart(){
		super.onRestart();
		showConfirmDialog();
		Log.e(LOG_TAG, "display restart");
	}
	private void cameraTest(){
		Intent mIntent = new Intent();
		ComponentName comp = new ComponentName("com.android.gallery3d",
				"com.android.camera.CameraLauncher");
		mIntent.setComponent(comp);
		//mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		//mIntent.setFlags(0x10200000);
		//mIntent.setAction("android.intent.action.MAIN");
		startActivity(mIntent);
	}
	public static void createTitle(List list, int position)
	{
		String info = "";
		try{
			Camera mCamera = Camera.open();
			Parameters parameters = mCamera.getParameters(); 
			int newPixels = 0; 
			List<Size> psizelist = parameters.getSupportedPictureSizes(); 
			if (null != psizelist && 0 < psizelist.size()) { 
				int heights[] = new int[psizelist.size()]; 
				int widths[] = new int[psizelist.size()]; 
				for (int i = 0; i < psizelist.size(); i++) { 
					Size size = (Size) psizelist.get(i); 
					int sizehieght = size.height; 
					int sizewidth = size.width; 
					heights[i] = sizehieght; 
					widths[i] =sizewidth; 
					Log.d(LOG_TAG, "id :"+i+"width :"+sizewidth+"height :"+sizehieght);
				} 
				int Height_Pixels = heights[0]; 
				int width_Pixels = widths[0]; 
				newPixels = Height_Pixels*width_Pixels; 
				newPixels = newPixels / 10000;
				System.out.println(newPixels); 
				info = "("+newPixels+"万像素)";
				isExistCamera = true;
			} 
			mCamera.release();
		}catch(Exception e){
			info = "("+NO_CAMERA+")";
			isExistCamera = false;
			e.printStackTrace();
			return;
		}finally{
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("title", "摄像头测试"+info);
			list.add(position, map);
		}
	}
	OnClickListener viewlisten = new OnClickListener(){
	public void onClick(View v) {
		if(v.equals(startButton)){
			if(!isExistCamera){
				mText.setText(NO_CAMERA);
				mText.setTextColor(errColor);
				return;
			}
			try{
				cameraTest();	
			}catch(Exception e){
				mText.setText(NO_CAMERA);
				mText.setTextColor(errColor);
				e.printStackTrace();
			}
		}
	}};
	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle("摄像头")
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					mText.setText(CAMERA_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(9, CAMERA_SUCCESS);
				}
			})
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(CAMERA_FAILED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(9, CAMERA_FAILED);
				}
			})
			.create();
		mDialog.setMessage("摄像头是否测试成功");
		mDialog.show();
	}
}

package com.thtfit.test;

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
import android.content.res.Resources.NotFoundException;

public class CameraActivity extends Activity 
{
	private final static String LOG_TAG = "Camera";
	private static String NO_CAMERA = "没有摄像头";
	private static String CAMERA_SUCCESS = "摄像头测试成功";
	private static String CAMERA_FAILED = "摄像头测试失败";
    private static String CAMERA = "Camera";
    private static String CAMERA_YES ="Yes";
	private static String CAMERA_NO ="No";
	private static String CAMERA_TEST ="摄像头测试";
	private static String CAMERA_IS_SUCCESS ="Whether is the camera successful";
    private static String CAMERA_PIXELS = "万像素";

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
		
		mText = (TextView)findViewById(R.id.message1);
		startButton = (Button)findViewById(R.id.button_start);
		startButton.setOnClickListener(viewlisten);
		
		try{
			NO_CAMERA = this.getResources().getString(R.string.no_camera);
            CAMERA_SUCCESS = this.getResources().getString(R.string.camera_success);
            CAMERA_FAILED = this.getResources().getString(R.string.camera_failed);
            CAMERA = this.getResources().getString(R.string.camera);
			CAMERA_YES = this.getResources().getString(R.string.yes);
            CAMERA_NO = this.getResources().getString(R.string.no);
            CAMERA_TEST = this.getResources().getString(R.string.camera_test);
            CAMERA_PIXELS = this.getResources().getString(R.string.million_pixels);
            CAMERA_IS_SUCCESS  =  this.getResources().getString(R.string.camera_is_success);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }
			titleText.setText(mProduct.getTitle(9));
	}
	@Override
	protected void onRestart(){
		super.onRestart();
		showConfirmDialog();
		Log.e(LOG_TAG, "display restart");
	}
	private void cameraTest(){
		Intent mIntent = new Intent();
 		ComponentName comp = new ComponentName("com.android.awgallery",
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
				info = "("+newPixels + CAMERA_PIXELS +")";
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
			map.put("title", CAMERA_TEST+info);
			list.add(position, map);
		}
	}
	OnClickListener viewlisten = new OnClickListener(){
	public void onClick(View v) {
		if(v.equals(startButton)){
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
			.setTitle(CAMERA)
			.setPositiveButton(CAMERA_YES, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					mText.setText(CAMERA_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(9, CAMERA_SUCCESS);
				}
			})
			.setNegativeButton(CAMERA_NO, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(CAMERA_FAILED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(9, CAMERA_FAILED);
				}
			})
			.create();
		mDialog.setMessage(CAMERA_IS_SUCCESS);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
}

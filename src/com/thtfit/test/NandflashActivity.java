package com.thtfit.test;

import com.thtfit.test.R;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;

import android.content.pm.PackageManager;
import android.content.res.Resources.NotFoundException;

public class NandflashActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Nandflash";
	private static String NANDFLASH_NOSPC_STRING = "没有足够的空间";
	private static String NANDFLASH_SUCCESS_STRING = "内部存储卡测试成功";
	private static String NANDFLASH_NOMEM_STRING = "没有足够的内存";
	private static String NANDFLASH_EACCESS_STRING = "不能获取内部存储卡权限";
	private static String NANDFLASH_COPY_ERROR_STRING = "拷贝数据到内部存储卡错误";
	private static String NANDFLASH_TEST = "flash测试(flash总量:";
	private static String TEST_RESULT = "测试结果";
    private static String BEING_TEST = "正在测试中...";
	private final int okColor = Color.GREEN;
	private final int errColor = Color.RED;
	private final int defaultColor = Color.BLACK;

	private int ret;
	private FileManager mFileMag;
	private Handler mHandle;
	private Thread mThread;

	private Button startButton, overButton;
	private TextView firstMessage, titleText;
	private Block mBlock;
	private ProductTest mProduct;
	private volatile boolean existThread = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nandflash);
		mProduct = (ProductTest)getApplication();
		
		//by Lu
		if (ContextCompat.checkSelfPermission(ProductTest.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			Log.d("luzhaojie", "NandflashActivity :: onCreate : have");
        } else {
        	 Log.d("luzhaojie", "NandflashActivity :: onCreate : no");
            ActivityCompat.requestPermissions(this , new String[]{
            		Manifest.permission.WRITE_EXTERNAL_STORAGE, 
            		Manifest.permission.READ_EXTERNAL_STORAGE,
            		}, 1);
        }

		startButton = (Button)findViewById(R.id.button_start);
		overButton = (Button)findViewById(R.id.button_over);
		startButton.setOnClickListener(this);
		overButton.setOnClickListener(this);
		titleText = (TextView)findViewById(R.id.textNandflash);
		titleText.setText(mProduct.getTitle(1));
		firstMessage = (TextView)findViewById(R.id.message1);

		 try{
            NANDFLASH_SUCCESS_STRING = this.getResources().getString(R.string.nandflash_success_string);
            NANDFLASH_NOSPC_STRING = this.getResources().getString(R.string.nandflash_not_enopy_space_string);
			NANDFLASH_COPY_ERROR_STRING = this.getResources().getString(R.string.nandflash_copy_error_string);
			NANDFLASH_NOMEM_STRING = this.getResources().getString(R.string.nandflash_no_memory_string);
			NANDFLASH_EACCESS_STRING = this.getResources().getString(R.string.nandflash_no_access_string);
			NANDFLASH_TEST = this.getResources().getString(R.string.flash_test);
            TEST_RESULT = this.getResources().getString(R.string.textresult);
            BEING_TEST = this.getResources().getString(R.string.being_test);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }
		mHandle = new Handler() {
			public void handleMessage(Message msg) {
			 	Log.d(LOG_TAG, "message: "+msg.toString());
				handleFlashMessage(msg.what, firstMessage);
				mProduct.addKeyEvent(msg.what);
				Block.forceStop = false;
			}
		};
		mBlock = new Block(mHandle);
		Log.i(LOG_TAG, "Nandflash Test Activity Created");
		
		startTest();
	}
	//by Lu
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
		case 1:
			if (grantResults.length > 0
	                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	                // permission was granted
				Log.d("luzhaojie", "NandflashActivity::onRequestPermissionsResult:permission was granted");
	            } else {
	                // permission denied,
	            }
			break;
		default:
			break;
		}
	}
	public void handleFlashMessage(int message, TextView text)
	{
		switch (message){
			case Block.NANDFLASH_SUCCESS:
				text.setText(NANDFLASH_SUCCESS_STRING);
				text.setTextColor(okColor);
				endTest();
				break;
			case Block.NANDFLASH_NOSPC:
				text.setText(NANDFLASH_NOSPC_STRING);
				text.setTextColor(errColor);
				endTest();
				break;
			case Block.NANDFLASH_NOMEM:
				text.setText(NANDFLASH_NOMEM_STRING);
				text.setTextColor(errColor);
				endTest();
				break;
			case Block.NANDFLASH_EACCESS:
				text.setText(NANDFLASH_EACCESS_STRING);
				text.setTextColor(errColor);
				endTest();
				break;
			case Block.NANDFLASH_COPY_ERROR:
				text.setText(NANDFLASH_COPY_ERROR_STRING);
				text.setTextColor(errColor);
				endTest();
				break;
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
				endTest();
		}
	}
	public static void createTitle(List list, int position)
	{
		int size = Block.getFlashSize();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("title", NANDFLASH_TEST+size+" M)");
		list.add(position, map);
	}
	public static void createHashTable(TreeMap<Integer, String> treemap)
	{
		treemap.put(Block.NANDFLASH_SUCCESS, NANDFLASH_SUCCESS_STRING);
		treemap.put(Block.NANDFLASH_NOSPC, NANDFLASH_NOSPC_STRING);
		treemap.put(Block.NANDFLASH_NOMEM, NANDFLASH_NOMEM_STRING);
		treemap.put(Block.NANDFLASH_EACCESS, NANDFLASH_EACCESS_STRING);
		treemap.put(Block.NANDFLASH_COPY_ERROR, NANDFLASH_COPY_ERROR_STRING);
	}
	protected void onStart(){
		super.onStart();
		Block.forceStop = false;
	}
	public void onClick(View v) {
		if(v.equals(startButton)) {
			firstMessage.setTextColor(okColor);
			firstMessage.setText(BEING_TEST);
			if(existThread){
				return;
			}
            mThread = new Thread(){
                public void run(){
					existThread = true;
					Block.forceStop = false;
					mBlock.nandflashInspection();	
					existThread = false;
			}};
			mThread.start();
			Log.d(LOG_TAG, "start"); 
		}else if(v.equals(overButton)) {
			if(!Block.forceStop){
				Block.forceStop = true;
				if(null != mThread){
				 	mThread.interrupt();
				}
				existThread = false;
				firstMessage.setText(TEST_RESULT);
				firstMessage.setTextColor(defaultColor);
				
			}else{
            	firstMessage.setText(TEST_RESULT);
				firstMessage.setTextColor(defaultColor);
			}
		}
	}
	
	private void startTest(){
		firstMessage.setTextColor(okColor);
		firstMessage.setText(BEING_TEST);
		if(existThread){
			return;
		}
        mThread = new Thread(){
            public void run(){
				existThread = true;
				Block.forceStop = false;
				mBlock.nandflashInspection();	
				existThread = false;
		}};
		mThread.start();
		Log.d(LOG_TAG, "start"); 
	}
	
	private void endTest(){
			new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				NandflashActivity.this.setResult(Mainacitivity.NAND);			
				NandflashActivity.this.finish();
			}
		}, 2000);
//		setResult(Mainacitivity.RAM);
//		finish();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
	}
	
	
}


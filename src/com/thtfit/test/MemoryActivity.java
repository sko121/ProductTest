package com.thtfit.test;

import com.thtfit.test.R;
import android.app.Activity;
import android.app.ActivityManager;  
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.lang.Process;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import android.content.res.Resources.NotFoundException;

public class MemoryActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Memory";
	private static String MEMORY_SUCCESS_STRING = "内存测试成功";
	private static String MEMORY_NOMEM_STRING = "没有足够的内存";
	private static String MEMORY_COPY_ERROR_STRING = "内存拷贝数据错误";
	private static String MEMORY_TEST = "内存测试(内存总量:";
	private static String TEST_RESULT ="测试结果";
    private static String BEING_TEST ="正在测试中...";
	private final int okColor = Color.GREEN;
	private final int errColor = Color.RED;
	private final int defaultColor = Color.BLACK;
	
	private Block mBlock;
	private Handler mHandle;
	private Thread mThread;
	private Button startButton, overButton;
	private TextView firstMessage, titleText;
	private ProductTest mProduct;
	private volatile boolean existThread = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memory);
		mProduct = (ProductTest)getApplication();

		startButton = (Button)findViewById(R.id.button_start);
		overButton = (Button)findViewById(R.id.button_over);
		startButton.setOnClickListener(this);
		overButton.setOnClickListener(this);
		firstMessage = (TextView)findViewById(R.id.message1);
		titleText = (TextView)findViewById(R.id.textMemory);
		titleText.setText(mProduct.getTitle(0));

		 try{
            MEMORY_SUCCESS_STRING = this.getResources().getString(R.string.memory_success_string);
            MEMORY_NOMEM_STRING = this.getResources().getString(R.string.memory_not_enopy_space_string);
			MEMORY_COPY_ERROR_STRING = this.getResources().getString(R.string.memory_copy_error_string);
			MEMORY_TEST = this.getResources().getString(R.string.memory_test);
            TEST_RESULT = this.getResources().getString(R.string.textresult);
            BEING_TEST = this.getResources().getString(R.string.being_test);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }

		mHandle = new Handler() {
			public void handleMessage(Message msg) {
			 	Log.d(LOG_TAG, "message: "+msg.toString());
				handleMemoryMessage(msg.what, firstMessage);
				mProduct.addKeyEvent(msg.what);
				Block.forceStop = false;
			}
		};
		mBlock = new Block(mHandle);
		Log.i(LOG_TAG, "Memory Test Activity Created");
		
		startTest();
	}
	public void handleMemoryMessage(int message, TextView text)
	{
		switch (message) {
			case Block.MEMORY_SUCCESS:
				text.setText(MEMORY_SUCCESS_STRING);
				text.setTextColor(okColor);
				endtest();
				
				break;
			case Block.MEMORY_NOMEM:
				text.setText(MEMORY_NOMEM_STRING);
				text.setTextColor(errColor);
				endtest();
				break;
			case Block.MEMORY_COPY_ERROR:
				text.setText(MEMORY_COPY_ERROR_STRING);
				text.setTextColor(errColor);
				endtest();
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
				endtest();
		}
	}	
	public static void createTitle(List list, int position)
	{
		int size = Block.getMemorySize();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("title", MEMORY_TEST + size+" M)");
		list.add(position, map);
	}
	public static void createHashTable(TreeMap<Integer, String> treemap)
	{
		treemap.put(Block.MEMORY_SUCCESS, MEMORY_SUCCESS_STRING);
		treemap.put(Block.MEMORY_NOMEM, MEMORY_NOMEM_STRING);
		treemap.put(Block.MEMORY_COPY_ERROR, MEMORY_COPY_ERROR_STRING);
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
					mBlock.memoryInspection();	
					existThread = false;
			}};
			mThread.start();
			Log.d(LOG_TAG, "mem test failed");	
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
			
			/*
			ActivityManager mActivityManager = null ;
	        mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
    	    MemoryInfo memoryInfo = new MemoryInfo() ;
        	mActivityManager.getMemoryInfo(memoryInfo) ;  
	        long memSize = memoryInfo.availMem ;  
        	long totalSize = memoryInfo.totalMem;
    	    Log.e(LOG_TAG, "total Size:"+totalSize/1024/1024+"avail size:"+memSize);   
			Log.d(LOG_TAG, "over"); */
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
				mBlock.memoryInspection();	
				existThread = false;
		}};
		mThread.start();
//		Log.d(LOG_TAG, "mem test failed");	
	}
	
	private void endtest(){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MemoryActivity.this.setResult(Mainacitivity.RAM);			
				MemoryActivity.this.finish();
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

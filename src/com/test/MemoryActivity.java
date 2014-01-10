package com.test;

import com.test.R;
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

public class MemoryActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Memory";
	private final static String MEMORY_SUCCESS_STRING = "内存测试成功";
	private final static String MEMORY_NOMEM_STRING = "没有足够的内存";
	private final static String MEMORY_COPY_ERROR_STRING = "内存拷贝数据错误";
	private final int okColor = Color.GREEN;
	private final int errColor = Color.RED;
	private final int defaultColor = Color.BLACK;
	
	private Block mBlock;
	private Handler mHandle;
	private Button startButton, overButton;
	private TextView firstMessage, titleText;
	private ProductTest mProduct;

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
		mHandle = new Handler() {
			public void handleMessage(Message msg) {
			 	Log.d(LOG_TAG, "message: "+msg.toString());
				handleMemoryMessage(msg.what, firstMessage);
				mProduct.addKeyEvent(msg.what);
			}
		};
		mBlock = new Block(mHandle);
		Log.i(LOG_TAG, "Memory Test Activity Created");
	}
	public void handleMemoryMessage(int message, TextView text)
	{
		switch (message) {
			case Block.MEMORY_SUCCESS:
				text.setText(MEMORY_SUCCESS_STRING);
				text.setTextColor(okColor);
				break;
			case Block.MEMORY_NOMEM:
				text.setText(MEMORY_NOMEM_STRING);
				text.setTextColor(errColor);
				break;
			case Block.MEMORY_COPY_ERROR:
				text.setText(MEMORY_COPY_ERROR_STRING);
				text.setTextColor(errColor);
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
		}
	}	
	public static void createTitle(List list, int position)
	{
		int size = Block.getMemorySize();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("title", "内存测试(内存总量:"+size+" M)");
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
			mBlock.memoryInspection();	
			Log.d(LOG_TAG, "mem test failed");	
		}else if(v.equals(overButton)) {
			firstMessage.setText("测试结果");
			firstMessage.setTextColor(defaultColor);

			ActivityManager mActivityManager = null ;
	        mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
    	    MemoryInfo memoryInfo = new MemoryInfo() ;
        	mActivityManager.getMemoryInfo(memoryInfo) ;  
	        long memSize = memoryInfo.availMem ;  
        	long totalSize = memoryInfo.totalMem;
    	    Log.e(LOG_TAG, "total Size:"+totalSize/1024/1024+"avail size:"+memSize);   

			Log.d(LOG_TAG, "over"); 
		}
	}
}

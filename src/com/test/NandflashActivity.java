package com.test;

import com.test.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;

public class NandflashActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Nandflash";
	private final static String NANDFLASH_NOSPC_STRING = "没有足够的空间";
	private final static String NANDFLASH_SUCCESS_STRING = "内部存储卡测试成功";
	private final static String NANDFLASH_NOMEM_STRING = "没有足够的内存";
	private final static String NANDFLASH_EACCESS_STRING = "不能获取内部存储卡权限";
	private final static String NANDFLASH_WRITE_ERROR_STRING = "写数据到内部存储卡错误";
	private final static String NANDFLASH_COPY_ERROR_STRING = "拷贝数据到内部存储卡错误";
	private final int okColor = Color.GREEN;
	private final int errColor = Color.RED;
	private final int defaultColor = Color.BLACK;

	private int ret;
	private FileManager mFileMag;
	private Handler mHandle;

	private Button startButton, overButton;
	private TextView firstMessage, titleText;
	private Block mBlock;
	private ProductTest mProduct;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nandflash);
		mProduct = (ProductTest)getApplication();

		startButton = (Button)findViewById(R.id.button_start);
		overButton = (Button)findViewById(R.id.button_over);
		startButton.setOnClickListener(this);
		overButton.setOnClickListener(this);
		titleText = (TextView)findViewById(R.id.textNandflash);
		titleText.setText(mProduct.getTitle(1));
		firstMessage = (TextView)findViewById(R.id.message1);
		mHandle = new Handler() {
			public void handleMessage(Message msg) {
			 	Log.d(LOG_TAG, "message: "+msg.toString());
				handleFlashMessage(msg.what, firstMessage);
				mProduct.addKeyEvent(msg.what);
			}
		};
		mBlock = new Block(mHandle);
		Log.i(LOG_TAG, "Nandflash Test Activity Created");
	}
	public void handleFlashMessage(int message, TextView text)
	{
		switch (message){
			case Block.NANDFLASH_SUCCESS:
				text.setText(NANDFLASH_SUCCESS_STRING);
				text.setTextColor(okColor);
				break;
			case Block.NANDFLASH_NOSPC:
				text.setText(NANDFLASH_NOSPC_STRING);
				text.setTextColor(errColor);
				break;
			case Block.NANDFLASH_NOMEM:
				text.setText(NANDFLASH_NOMEM_STRING);
				text.setTextColor(errColor);
				break;
			case Block.NANDFLASH_EACCESS:
				text.setText(NANDFLASH_EACCESS_STRING);
				text.setTextColor(errColor);
				break;
			case Block.NANDFLASH_WRITE_ERROR:
				text.setText(NANDFLASH_WRITE_ERROR_STRING);
				text.setTextColor(errColor);
				break;
			case Block.NANDFLASH_COPY_ERROR:
				text.setText(NANDFLASH_COPY_ERROR_STRING);
				text.setTextColor(errColor);
				break;
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
		}
	}
	public static void createTitle(List list, int position)
	{
		int size = Block.getFlashSize();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("title", "flash测试(flash总量:"+size+" M)");
		list.add(position, map);
	}
	public static void createHashTable(TreeMap<Integer, String> treemap)
	{
		treemap.put(Block.NANDFLASH_SUCCESS, NANDFLASH_SUCCESS_STRING);
		treemap.put(Block.NANDFLASH_NOSPC, NANDFLASH_NOSPC_STRING);
		treemap.put(Block.NANDFLASH_NOMEM, NANDFLASH_NOMEM_STRING);
		treemap.put(Block.NANDFLASH_EACCESS, NANDFLASH_EACCESS_STRING);
		treemap.put(Block.NANDFLASH_WRITE_ERROR, NANDFLASH_WRITE_ERROR_STRING);
		treemap.put(Block.NANDFLASH_COPY_ERROR, NANDFLASH_COPY_ERROR_STRING);
	}
	public void onClick(View v) {
		if(v.equals(startButton)) {
			mBlock.nandflashInspection();	
			Log.d(LOG_TAG, "start"); 
		}else if(v.equals(overButton)) {
			firstMessage.setText("测试结果");
			firstMessage.setTextColor(defaultColor);
			Log.d(LOG_TAG, "over"); 
		}
	}
}


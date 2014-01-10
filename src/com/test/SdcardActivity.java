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
import java.util.TreeMap;

public class SdcardActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Sdcard";
	private final static String EXTSD_SUCCESS_STRING="外部存储卡测试成功";
	private final static String EXTSD_FIALED_STRING="外部存储卡测试失败";
	private final static String EXTSD_NOT_EXIST_STRING="没有检测到外部存储卡";
	private final static String EXTSD_NOT_ENOUGH_SPACE_STRING="外部存储卡没有足够空间";
	private final static String EXTSD_COPY_ERROR_STRING="外部存储卡拷贝数据错误";
	private final int okColor = Color.GREEN;
	private final int errColor = Color.RED;
	private final int defaultColor = Color.BLACK;

	private int ret;
	private Block mBlock;
	private Handler mHandle;
	private FileManager mFileMag;
	private Button sdStartButton;
	private Button sdOverButton;
	private TextView firstMessage;
	private ProductTest mProduct;
	private volatile boolean existThread = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdcard);
		mProduct = (ProductTest)getApplication();

		mFileMag = new FileManager(this);
		sdStartButton = (Button)findViewById(R.id.button_sdStart);
		sdOverButton = (Button)findViewById(R.id.button_sdOver);
		sdStartButton.setOnClickListener(this);
		sdOverButton.setOnClickListener(this);
		firstMessage = (TextView)findViewById(R.id.sdText1);
		mHandle = new Handler() {
			public void handleMessage(Message msg) {
			 	Log.d(LOG_TAG, "message: "+msg.toString());
				handleSdcardMessage(msg.what, firstMessage);
				mProduct.addKeyEvent(msg.what);
			}
		};
		mBlock = new Block(mHandle);
	}
	public void handleSdcardMessage(int message, TextView text)
	{
		switch (message) {
			case Block.EXTSD_SUCCESS:
				text.setText(EXTSD_SUCCESS_STRING);
				text.setTextColor(okColor);
				break;
			case Block.EXTSD_NOT_EXIST:
				text.setText(EXTSD_NOT_EXIST_STRING);
				text.setTextColor(errColor);
				break;
			case Block.EXTSD_NOT_ENOUGH_SPACE:
				text.setText(EXTSD_NOT_ENOUGH_SPACE_STRING);
				text.setTextColor(errColor);
				break;
			case Block.EXTSD_COPY_ERROR:
				text.setText(EXTSD_COPY_ERROR_STRING);
				text.setTextColor(errColor);
				break;
			case Block.EXTSD_FIALED:
				text.setText(EXTSD_FIALED_STRING);
				text.setTextColor(errColor);
				break;
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
				break;
		}
	}
	public static void createHashTable(TreeMap<Integer, String> treemap){
		treemap.put(Block.EXTSD_SUCCESS, EXTSD_SUCCESS_STRING);
		treemap.put(Block.EXTSD_NOT_EXIST, EXTSD_NOT_EXIST_STRING);
		treemap.put(Block.EXTSD_NOT_ENOUGH_SPACE, EXTSD_NOT_ENOUGH_SPACE_STRING);
		treemap.put(Block.EXTSD_FIALED, EXTSD_FIALED_STRING);
		treemap.put(Block.EXTSD_COPY_ERROR, EXTSD_COPY_ERROR_STRING);
	}
	public void onClick(View v) {
		if(v.equals(sdStartButton)) {
			if(existThread)
				return;
			existThread = true;
			mBlock.sdcardInspection();
			existThread = false;
		}
		else if(v.equals(sdOverButton)) {
			if(!existThread){
				firstMessage.setText("测试结果");
				firstMessage.setTextColor(defaultColor);
			}
		}
	}
}


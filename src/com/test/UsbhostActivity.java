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

public class UsbhostActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Usbhost";
	private final static String USBHOST_SUCCESS_STRING="OTG测试成功";
	private final static String USBHOST_FIALED_STRING="OTG测试失败";
	private final static String USBHOST_NOT_EXIST_STRING="没有检测到U盘";
	private final static String USBHOST_NOT_ENOUGH_SPACE_STRING="U盘没有足够空间";
	private final static String USBHOST_COPY_ERROR_STRING="U盘拷贝数据错误";
	private final int okColor = Color.GREEN;
	private final int errColor = Color.RED;
	private final int defaultColor = Color.BLACK;

	private int ret;
	private Block mBlock;
	private Handler mHandle;
	private FileManager mFileMag;
	private Button StartButton;
	private Button OverButton;
	private TextView resultMessage;
	private volatile boolean existThread = false;
	private ProductTest mProduct;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usbhost);
		mProduct = (ProductTest)getApplication();

		StartButton = (Button)findViewById(R.id.button_Start);
		OverButton = (Button)findViewById(R.id.button_Over);
		StartButton.setOnClickListener(this);
		OverButton.setOnClickListener(this);
		resultMessage = (TextView)findViewById(R.id.textResult);
		mHandle = new Handler() {
			public void handleMessage(Message msg) {
			 	Log.d(LOG_TAG, "message: "+msg.toString());
				handleUsbhostMessage(msg.what, resultMessage);
				mProduct.addKeyEvent(msg.what);
			}
		};
		mBlock = new Block(mHandle);
	}
	public void handleUsbhostMessage(int message, TextView text)
	{
		switch (message) {
			case Block.USBHOST_SUCCESS:
				text.setText(USBHOST_SUCCESS_STRING);
				text.setTextColor(okColor);
				break;
			case Block.USBHOST_NOT_EXIST:
				text.setText(USBHOST_NOT_EXIST_STRING);
				text.setTextColor(errColor);
				break;
			case Block.USBHOST_NOT_ENOUGH_SPACE:
				text.setText(USBHOST_NOT_ENOUGH_SPACE_STRING);
				text.setTextColor(errColor);
				break;
			case Block.USBHOST_COPY_ERROR:
				text.setText(USBHOST_COPY_ERROR_STRING);
				text.setTextColor(errColor);
				break;
			case Block.USBHOST_FIALED:
				text.setText(USBHOST_FIALED_STRING);
				text.setTextColor(errColor);
				break;
			default:
				Log.d(LOG_TAG, "unknown message: "+message);
				break;
		}
	}
	public static void createHashTable(TreeMap<Integer, String> treemap)
	{
		treemap.put(Block.USBHOST_SUCCESS, USBHOST_SUCCESS_STRING);
		treemap.put(Block.USBHOST_NOT_EXIST, USBHOST_NOT_EXIST_STRING);
		treemap.put(Block.USBHOST_NOT_ENOUGH_SPACE, USBHOST_NOT_ENOUGH_SPACE_STRING);
		treemap.put(Block.USBHOST_COPY_ERROR, USBHOST_COPY_ERROR_STRING);
		treemap.put(Block.USBHOST_FIALED, USBHOST_FIALED_STRING);
	}	
	protected void onStop(){
		super.onStop();
		Block.forceStop = false;
	}
	public void onClick(View v) {
		if(v.equals(StartButton)) {
			if(existThread)
				return;
			existThread = true;
			mBlock.usbhostInspection();
			existThread = false;
		}
		else if(v.equals(OverButton)) {
			if(!Block.forceStop){
				Block.forceStop = true;
				if(!existThread){
					resultMessage.setText("测试结果");
					resultMessage.setTextColor(defaultColor);
				}
			}
		}
	}
}


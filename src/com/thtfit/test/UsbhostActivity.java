package com.thtfit.test;

import com.thtfit.test.R;
import com.thtfit.test.GetSdcardMemory;
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
import android.content.res.Resources.NotFoundException;

public class UsbhostActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Usbhost";
    private final static String PATH = "/mnt/usbhost1";
	private static String USBHOST_SUCCESS_STRING="OTG测试成功";
	private static String USBHOST_FIALED_STRING="OTG测试失败";
	private static String USBHOST_NOT_EXIST_STRING="没有检测到U盘";
	private static String USBHOST_NOT_ENOUGH_SPACE_STRING="U盘没有足够空间";
	private static String USBHOST_COPY_ERROR_STRING="U盘拷贝数据错误";
    private static String USBHOST_AVAILABLE_SPACE ="USB卡可用空间:";
    private static String USBHOST_TOTAL_CAPACITY ="USB卡总容量:";
	private static String TEST_RESULT ="测试结果";
    private static String BEING_TEST ="正在测试中...";
	private final int okColor = Color.GREEN;
	private final int errColor = Color.RED;
	private final int defaultColor = Color.BLACK;

	private int ret;
	private Block mBlock;
	private Handler mHandle;
	private Thread mThread;
	private FileManager mFileMag;
	private Button StartButton;
	private Button OverButton;
	private TextView resultMessage;
    private TextView usbData;
	private volatile boolean existThread = false;
    private volatile boolean stop =false;

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
        usbData = (TextView)findViewById(R.id.textResult1);
		
		try{
            USBHOST_SUCCESS_STRING = this.getResources().getString(R.string.usbhost_success_string);
            USBHOST_FIALED_STRING = this.getResources().getString(R.string.usbhost_failure_string);
            USBHOST_NOT_EXIST_STRING = this.getResources().getString(R.string.usbhost_not_exist_string);
            USBHOST_NOT_ENOUGH_SPACE_STRING = this.getResources().getString(R.string.usbhost_not_enopy_space_string);
			USBHOST_COPY_ERROR_STRING = this.getResources().getString(R.string.usbhost_copy_error_string);
            USBHOST_AVAILABLE_SPACE = this.getResources().getString(R.string.usbhost_available_space);
            USBHOST_TOTAL_CAPACITY =  this.getResources().getString(R.string.usbhost_total_capacity);
            TEST_RESULT = this.getResources().getString(R.string.textresult);
            BEING_TEST = this.getResources().getString(R.string.being_test);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }
		
		mHandle = new Handler() {
			public void handleMessage(Message msg) {
			 	Log.d(LOG_TAG, "message: "+msg.toString());
				handleUsbhostMessage(msg.what, resultMessage);
				mProduct.addKeyEvent(msg.what);
				Block.forceStop = false;
			}
		};
		mBlock = new Block(mHandle);
	}
	public void handleUsbhostMessage(int message, TextView text)
	{
		switch (message) {
			case Block.USBHOST_SUCCESS:
                Log.i(LOG_TAG,"forceStop" +Block.forceStop );
				if(!stop){
					text.setText(USBHOST_SUCCESS_STRING);
					text.setTextColor(okColor);
					text.setTextColor(okColor);
		            GetSdcardMemory mGetSdcardMemory = new GetSdcardMemory (this,PATH);
		            usbData.setText(USBHOST_AVAILABLE_SPACE + mGetSdcardMemory.getSDAvailableSize() + "M" + " \n" +USBHOST_TOTAL_CAPACITY + mGetSdcardMemory.getSDTotalSize() +"M"  );
				 	usbData.setTextColor(okColor);
				}
				break;
			case Block.USBHOST_NOT_EXIST:
				if(!Block.forceStop){
					text.setText(USBHOST_NOT_EXIST_STRING);
					text.setTextColor(errColor);
				}
				break;
			case Block.USBHOST_NOT_ENOUGH_SPACE:
				if(!Block.forceStop){
					text.setText(USBHOST_NOT_ENOUGH_SPACE_STRING);
					text.setTextColor(errColor);
				}
				break;
			case Block.USBHOST_COPY_ERROR:
				if(!Block.forceStop){
					text.setText(USBHOST_COPY_ERROR_STRING);
					text.setTextColor(errColor);
				}
				break;
			case Block.USBHOST_FIALED:
				if(!Block.forceStop){
					text.setText(USBHOST_FIALED_STRING);
					text.setTextColor(errColor);
				}
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
	protected void onStart(){
		super.onStart();
		Block.forceStop = false;
	}
	public void onClick(View v) {
		if(v.equals(StartButton)) {
			stop = false;
			resultMessage.setTextColor(okColor);
			resultMessage.setText(BEING_TEST);
			if(existThread){
				return;
			}
			mThread = new Thread(){
                public void run(){
					existThread = true;
					Block.forceStop = false;
					mBlock.usbhostInspection();	
					existThread = false;
			}};
			mThread.start();
			
		}else if(v.equals(OverButton)) {
			stop = true;
			if(!Block.forceStop){
				Block.forceStop = true;
				if(null != mThread){
				 	mThread.interrupt();
				}
				existThread = false;
				resultMessage.setText(TEST_RESULT);
				resultMessage.setTextColor(defaultColor);
				usbData.setText("");

				
			}else{
            	resultMessage.setText(TEST_RESULT);
				resultMessage.setTextColor(defaultColor);
				usbData.setText("");

			}
		}
	}
}


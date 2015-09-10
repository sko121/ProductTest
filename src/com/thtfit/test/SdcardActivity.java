package com.thtfit.test;

import com.thtfit.test.R;
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

public class SdcardActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Sdcard";
    private final static String PATH = "/mnt/extsd";
	private  static String EXTSD_SUCCESS_STRING="外部存储卡测试成功";
	private  static String EXTSD_FIALED_STRING="外部存储卡测试失败";
	private  static String EXTSD_NOT_EXIST_STRING="没有检测到外部存储卡";
	private  static String EXTSD_NOT_ENOUGH_SPACE_STRING="外部存储卡没有足够空间";
	private  static String EXTSD_COPY_ERROR_STRING="外部存储卡拷贝数据错误";
    private  static String SDCARD_AVAILABLE_SPACE ="SD卡可用空间:";
    private  static String SDCARD_TOTAL_CAPACITY ="SD卡总容量:";
    private  static String TEST_RESULT ="测试结果";
    private  static String BEING_TEST ="正在测试中...";
	private final int okColor = Color.GREEN;
	private final int errColor = Color.RED;
	private final int defaultColor = Color.BLACK;

	private int ret;
	private Block mBlock;
	private Handler mHandle;
	private Thread mThread;
	private FileManager mFileMag;
	private Button sdStartButton;
	private Button sdOverButton;
	private TextView firstMessage;
    private TextView sdcardData;
	private ProductTest mProduct;
	private volatile boolean existThread = false;
	private volatile boolean stop =false;

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
        sdcardData = (TextView)findViewById(R.id.sdText2);

        try{
            EXTSD_SUCCESS_STRING = this.getResources().getString(R.string.extsd_success_string);
            EXTSD_FIALED_STRING = this.getResources().getString(R.string.extsd_failure_string);
            EXTSD_NOT_EXIST_STRING = this.getResources().getString(R.string.extsd_not_exist_string);
            EXTSD_NOT_ENOUGH_SPACE_STRING = this.getResources().getString(R.string.extsd_not_enopy_space_string);
			EXTSD_COPY_ERROR_STRING = this.getResources().getString(R.string.extsd_copy_error_string);
            SDCARD_AVAILABLE_SPACE = this.getResources().getString(R.string.sdcard_available_space);
            SDCARD_TOTAL_CAPACITY =  this.getResources().getString(R.string.sdcard_total_capacity);
            TEST_RESULT = this.getResources().getString(R.string.textresult);
            BEING_TEST = this.getResources().getString(R.string.being_test);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }
		mHandle = new Handler() {
			public void handleMessage(Message msg) {
			 	Log.d(LOG_TAG, "message: "+msg.toString());
				handleSdcardMessage(msg.what, firstMessage);
				mProduct.addKeyEvent(msg.what);
				Block.forceStop = false;
			}
		};
		mBlock = new Block(mHandle);
	}
	public void handleSdcardMessage(int message, TextView text)
	{
		switch (message) {
			case Block.EXTSD_SUCCESS:
				if(!stop){
					text.setText(EXTSD_SUCCESS_STRING);
					text.setTextColor(okColor);
		            GetSdcardMemory mGetSdcardMemory = new GetSdcardMemory (this,PATH);
		            sdcardData.setText( SDCARD_AVAILABLE_SPACE + mGetSdcardMemory.getSDAvailableSize() + "M" + " \n" +SDCARD_TOTAL_CAPACITY + mGetSdcardMemory.getSDTotalSize() +"M"  );
		            sdcardData.setTextColor(okColor);
				}
				break;
			case Block.EXTSD_NOT_EXIST:
				if(!stop){
					text.setText(EXTSD_NOT_EXIST_STRING);
					text.setTextColor(errColor);
				}
				break;
			case Block.EXTSD_NOT_ENOUGH_SPACE:
				if(!stop){
					text.setText(EXTSD_NOT_ENOUGH_SPACE_STRING);
					text.setTextColor(errColor);
				}
				break;
			case Block.EXTSD_COPY_ERROR:
				if(!stop){
					text.setText(EXTSD_COPY_ERROR_STRING);
					text.setTextColor(errColor);
				}
				break;
			case Block.EXTSD_FIALED:
				if(!stop){
					text.setText(EXTSD_FIALED_STRING);
					text.setTextColor(errColor);
				}
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
	protected void onStart(){
		super.onStart();
		Block.forceStop = false;
	}
	public void onClick(View v) {
		if(v.equals(sdStartButton)) {
			stop = false;
			firstMessage.setTextColor(okColor);
			firstMessage.setText(BEING_TEST);
			if(existThread){
				return;
			}
			mThread = new Thread(){
                public void run(){
					existThread = true;
					Block.forceStop = false;
					mBlock.sdcardInspection();	
					existThread = false;
			}};
			mThread.start();

            
		}
		else if(v.equals(sdOverButton)) {
			stop = true;
			if(!Block.forceStop){
				Block.forceStop = true;
				if(null != mThread){
				 	mThread.interrupt();
				}
				existThread = false;
				firstMessage.setText(TEST_RESULT);
				firstMessage.setTextColor(defaultColor);
				sdcardData.setText("");

			}else{
            	firstMessage.setText(TEST_RESULT);
				firstMessage.setTextColor(defaultColor);
				sdcardData.setText("");

			}
		}
	}
}


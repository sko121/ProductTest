package com.test;

import com.test.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RecorderActivity extends Activity {
	private final static String LOG_TAG = "Recorder";
	private final static String RECORDER_SUCCESS = "录音测试成功";
	private final static String RECORDER_FIALED = "录音测试失败";
	private int defaultColor = Color.BLACK;
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;

	private Button startButton, confirmButton;
	private TextView mText;
	private ProductTest mProduct;
	private AlertDialog mDialog = null;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recorder);
		mProduct = (ProductTest)getApplication();
		startButton = (Button)findViewById(R.id.button_start);
		confirmButton = (Button)findViewById(R.id.button_confirm);
		startButton.setOnClickListener(viewlisten);
		confirmButton.setOnClickListener(viewlisten);
		mText = (TextView)findViewById(R.id.message1);
	}
	OnClickListener viewlisten = new OnClickListener(){
		public void onClick(View v) {
			if(v.equals(startButton)) {
				recordTest();
			}else if(v.equals(confirmButton)){
				showConfirmDialog();
			}
		}
	};
	private void recordTest(){
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName("com.android.soundrecorder","com.android.soundrecorder.SoundRecorder");
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.MAIN");
        startActivity(mIntent);
	}
	private void showConfirmDialog(){
		mDialog = (new AlertDialog.Builder(this))
			.setTitle("录音")
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mText.setText(RECORDER_SUCCESS);
					mText.setTextColor(okColor);
					mProduct.addKeyEvent(12, RECORDER_SUCCESS);
				}
			})
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mText.setText(RECORDER_FIALED);
					mText.setTextColor(errColor);
					mProduct.addKeyEvent(12, RECORDER_FIALED);
				}
			})
			.create();
		mDialog.setMessage("录音是否测试成功");
		mDialog.show();
	}
}

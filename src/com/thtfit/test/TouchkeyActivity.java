package com.thtfit.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.content.res.Resources.NotFoundException;

public class TouchkeyActivity extends Activity {
	private final static String LOG_TAG = "Touchkey";
	private static String VOLUME_UP_SUCCESS = "音量+ 测试成功";
	private static String VOLUME_DOWN_SUCCESS = "音量- 测试成功";
	private static String CLICK_SOUND_BUTTON ="请按音量按键";
	private int okColor = Color.GREEN;
	private TextView mIndicates, mText;
	private ProductTest mProduct;
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touchkey);
		mProduct = (ProductTest)getApplication();
		mIndicates=(TextView) findViewById(R.id.textIndicates); 
		mText = (TextView)findViewById(R.id.message1);

		try{
            VOLUME_UP_SUCCESS= this.getResources().getString(R.string.volume_up_success);
            VOLUME_DOWN_SUCCESS = this.getResources().getString(R.string.volume_down_failed);
            CLICK_SOUND_BUTTON = this.getResources().getString(R.string.click_sound_button);
           }catch(NotFoundException NotFoundEx){
             Log.d(LOG_TAG, "NotFoundException: " + NotFoundEx.getMessage());
           }
		mIndicates.setText(CLICK_SOUND_BUTTON);
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(LOG_TAG, "keyCode : "+keyCode);	
        switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
				mText.setText(VOLUME_UP_SUCCESS);
				mText.setTextColor(okColor);
				mProduct.addKeyEvent(4, VOLUME_UP_SUCCESS);
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				mText.setText(VOLUME_DOWN_SUCCESS);
				mText.setTextColor(okColor);
				mProduct.addKeyEvent(4, VOLUME_DOWN_SUCCESS);
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
}

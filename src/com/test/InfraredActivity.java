package com.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class InfraredActivity extends Activity {
	private final static String LOG_TAG = "Infrared";
	private TextView mTextViewTestkey;
	private int okColor = Color.GREEN;
	private int errColor = Color.RED;
	private int textSize = 200;
	private String STRING_MUTE = "éé³";
    private String STRING_1 = "1";
    private String STRING_2 = "2";
	private String STRING_3 = "3";
	private String STRING_4 = "4";
	private String STRING_5 = "5";
	private String STRING_6 = "6";
	private String STRING_7 = "7";
	private String STRING_8 = "8";
	private String STRING_9 = "9";
	private String STRING_0 = "0";
	private String STRING_POINT = ".";
	private String STRING_BACK = "è¿å";
	private String STRING_LAST_CH = "LAST CH";
	private String STRING_TV_GUIDE = "TV GUIDE";
	private String STRING_CHANNELS = "CHANNELS";
	private String STRING_MENU = "MENU";
	private String STRING_VOL_UP = "VOL +";
	private String STRING_VOL_DOWN = "VOL -";
	private String STRING_CH_UP = "CH +";
	private String STRING_CH_DOWN = "CH -";
	private String STRING_OK = "OK";
	private String STRING_UP = "上";
	private String STRING_DOWN = "ÏÂ";
	private String STRING_LEFT = "×ó";
	private String STRING_RIGHT = "ÓÒ";
	private String STRING_REW = "ºóÍË";
	private String STRING_FFWD = "¿ì½ø";
	private String STRING_PLAY = "²¥·Å";
	private String STRING_PAUSE = "ÔÝÍ£";
	private String STRING_REC = "Â¼ÖÆ";
	private String STRING_STOP = "Í£Ö¹";
	private String STRING_SPEED = "éåº¦";
	private String STRING_SETTING = "è®¾ç½®";
	private String STRING_ERR = "X";

	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infrared);
		mTextViewTestkey=(TextView) findViewById(R.id.textInfrared); 
		mTextViewTestkey.setTextSize(textSize);  
    }

	public boolean onKeyDown(int keyCode, KeyEvent event) {
        mTextViewTestkey.setTextColor(okColor);
        switch (keyCode) {
			case KeyEvent.KEYCODE_MUTE:
				mTextViewTestkey.setText(STRING_MUTE);
				break;
			case KeyEvent.KEYCODE_0:
				mTextViewTestkey.setText(STRING_0);
				break;
			case KeyEvent.KEYCODE_1:
				mTextViewTestkey.setText(STRING_1);
				break;
			case KeyEvent.KEYCODE_2:
				mTextViewTestkey.setText(STRING_2);
				break;
			case KeyEvent.KEYCODE_3:
				mTextViewTestkey.setText(STRING_3);
				break;
			case KeyEvent.KEYCODE_4:
				mTextViewTestkey.setText(STRING_4);
				break;
			case KeyEvent.KEYCODE_5:
				mTextViewTestkey.setText(STRING_5);
				break;
			case KeyEvent.KEYCODE_6:
				mTextViewTestkey.setText(STRING_6);
				break;
			case KeyEvent.KEYCODE_7:
				mTextViewTestkey.setText(STRING_7);
				break;
			case KeyEvent.KEYCODE_8:
               	mTextViewTestkey.setText(STRING_8);
				break;
			case KeyEvent.KEYCODE_9:
				mTextViewTestkey.setText(STRING_9);
				break;
			case KeyEvent.KEYCODE_PERIOD:
				mTextViewTestkey.setText(STRING_POINT);
				break;
			case KeyEvent.KEYCODE_DEL:
				mTextViewTestkey.setText(STRING_LAST_CH);
				break;
			case KeyEvent.KEYCODE_GUIDE:
				mTextViewTestkey.setText(STRING_TV_GUIDE);
				break;
			case KeyEvent.KEYCODE_INFO:
				mTextViewTestkey.setText(STRING_CHANNELS);
				break;
			case KeyEvent.KEYCODE_MENU:
				mTextViewTestkey.setText(STRING_MENU);
				break;
			case KeyEvent.KEYCODE_VOLUME_UP:
				mTextViewTestkey.setText(STRING_VOL_UP);
			    break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
                mTextViewTestkey.setText(STRING_VOL_DOWN);
                break;
			case KeyEvent.KEYCODE_CHANNEL_UP:
				mTextViewTestkey.setText(STRING_CH_UP);
				break;
			case KeyEvent.KEYCODE_CHANNEL_DOWN:
				mTextViewTestkey.setText(STRING_CH_DOWN);
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				mTextViewTestkey.setText(STRING_LEFT);
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				mTextViewTestkey.setText(STRING_RIGHT);
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				mTextViewTestkey.setText(STRING_UP);
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				mTextViewTestkey.setText(STRING_DOWN);
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				mTextViewTestkey.setText(STRING_OK);
				break;
			case KeyEvent.KEYCODE_MEDIA_REWIND:
				mTextViewTestkey.setText(STRING_REW);
				break;
			case KeyEvent.KEYCODE_MEDIA_PLAY:
				mTextViewTestkey.setText(STRING_PLAY);
				break;
			case KeyEvent.KEYCODE_BREAK:
				mTextViewTestkey.setText(STRING_PAUSE);
				break;
			case KeyEvent.KEYCODE_FORWARD:
				mTextViewTestkey.setText(STRING_FFWD);
				break;
			case KeyEvent.KEYCODE_MEDIA_STOP:
				mTextViewTestkey.setText(STRING_STOP);
				break;
			case KeyEvent.KEYCODE_BACK:
				mTextViewTestkey.setText(STRING_BACK);
				break;
			default:
				mTextViewTestkey.setTextColor(errColor);
				mTextViewTestkey.setText(STRING_ERR);
				break;
		}
		Log.d(LOG_TAG, "keyCode : "+keyCode);

        //return bHandled;
        return super.onKeyDown(keyCode,event) ;
    }
}

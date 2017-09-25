package com.thtfit.test;

import com.thtfit.test.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EthernetActivity extends Activity implements OnClickListener {
	private final static String LOG_TAG = "Adversiting-Ethernet";
	private TextView mText;
	private Button startButton;
	private Button stopButton;
	private Handler mHandle;
	private EthernetManager mEthernet;

	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ethernet);

		startButton = (Button)findViewById(R.id.button_start);
		startButton.setOnClickListener(this);
		stopButton = (Button)findViewById(R.id.button_stop);
		stopButton.setOnClickListener(this);
		mText = (TextView)findViewById(R.id.textResult);
		mHandle = new Handler() {
			public void handleMessage(Message msg) {
			 	Log.d(LOG_TAG, "message: "+msg.toString());
				mEthernet.handleEthernetMessage(msg.what, mText);
			}
		};
		mEthernet = new EthernetManager(this, mHandle);
	}	

	public void onClick(View v) {
		if(v.equals(startButton)){
			new Thread(new Runnable(){
				public void run(){
				mEthernet.EthernetTest();	
			}}).start();
		}else if(v.equals(stopButton)){
			
		}
	}
}

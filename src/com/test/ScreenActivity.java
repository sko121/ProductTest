package com.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;

public class ScreenActivity extends Activity implements OnClickListener{
	View main;
	int i = 0;
	int Array[] = {Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.GRAY, Color.WHITE};
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = getLayoutInflater().from(this).inflate(R.layout.screen, null);
        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		main.setBackgroundColor(Array[i]);
        main.setOnClickListener(this);
        setContentView(main);
    }

	@Override
	public void onClick(View v) {
		i++;
		Log.d("Screen", "i:"+i);
		if(i < 6){
			main.setBackgroundColor(Array[i]);
			main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}else{
			finish();
		}
	}
}

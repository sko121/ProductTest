package com.ckl.PointerLocation;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Demonstrates wrapping a layout in a ScrollView.
 *
 */
public class PointerLocation extends Activity {
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(new PointerLocationView(this));
        
        // Make the screen full bright for this activity.
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getWindow().setAttributes(lp);
    }
}

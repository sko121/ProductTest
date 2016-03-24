package com.thtfit.test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Bitmap.Config;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class MactobarcodeActivity extends Activity{
	
	private String TAG = "MactobarcodeActivity";

	String macAddress = null;
	int defBright = 0;
	private static  int setBright = 255;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mactobarcode);
        
       defBright = getScreenBrightness();
       saveScreenBrightness(255);
       setScreenBrightness(255);
        
        
        macAddress = getWifiaddr();
        try{
        	macAddress = macAddress.replaceAll(":", "");
        }catch(NullPointerException e){
        	e.printStackTrace();
        	Toast.makeText(getApplicationContext(), "macAddress is error", Toast.LENGTH_LONG).show();
        	return;
        }
		Bitmap bitmap = null;
		ImageView iv = new ImageView(this);
		bitmap = creatBarcode(getApplicationContext(),
				macAddress, 600, 300, true);
		iv.setImageBitmap(bitmap);
		iv.setScaleType(ScaleType.FIT_CENTER);
		setContentView(iv, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
            
    }  
    private String getWifiaddr() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        String macAddressRet = wifiInfo == null ? null : wifiInfo.getMacAddress();
        return macAddressRet;
    }
    /** 
     * 获得当前屏幕亮度的模式     
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度 
     */  
      private int getScreenMode(){  
        int screenMode=0;  
        try{  
            screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);  
        }  
        catch (Exception localException){  
              
        }  
        return screenMode;  
      }  
        
     /** 
     * 获得当前屏幕亮度值  0--255 
     */  
      private int getScreenBrightness(){  
        int screenBrightness=255;  
        try{  
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);  
        }  
        catch (Exception localException){  
            
        }  
        return screenBrightness;  
      }  

      private void setScreenMode(int paramInt){  
        try{  
          Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);  
        }catch (Exception localException){  
          localException.printStackTrace();  
        }  
      }  
      /** 
       * 设置当前屏幕亮度值  0--255 
       */  
      private void saveScreenBrightness(int paramInt){  
        try{  
          Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);  
        }  
        catch (Exception localException){  
          localException.printStackTrace();  
        }  
      }  
      /** 
       * 保存当前的屏幕亮度值，并使之生效 
       */  
      @SuppressLint("NewApi") 
      private void setScreenBrightness(int paramInt){  
        Window localWindow = getWindow();  
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();  
        float f = paramInt / 255.0F;  
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);  
      }  



  	
	  /**
	   * ͼƬ�����������Ŀհ׵Ŀ��
	   */
	   private static int marginW=20; 
	   /**
	    * ������ı�������
	    */
	   private static BarcodeFormat barcodeFormat= BarcodeFormat.CODE_128;

	   /**
	    * ����������
	    * @param context
	    * @param contents  ��Ҫ���ɵ�����
	    * @param desiredWidth ����������Ŀ��
	    * @param desiredHeight ����������ĸ߶�
	    * @param displayCode �Ƿ����������·���ʾ����
	    * @return
	    */
	   public static Bitmap creatBarcode(Context context,String contents ,
		        int desiredWidth,int desiredHeight,boolean displayCode){
		Bitmap ruseltBitmap=null;
		if (displayCode) {
			Bitmap barcodeBitmap=encodeAsBitmap(contents, barcodeFormat, desiredWidth, desiredHeight);
			Bitmap codeBitmap=creatCodeBitmap(contents, desiredWidth+2*marginW, desiredHeight, context);
			ruseltBitmap=mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(0, desiredHeight));
			//ruseltBitmap = codeBitmap;
		} else {
			ruseltBitmap=encodeAsBitmap(contents,barcodeFormat, desiredWidth, desiredHeight);
		}
		
		return ruseltBitmap;
	}
	   
	 /**
	  * ������ʾ�����Bitmap
	  * @param contents
	  * @param width
	  * @param height
	  * @param context
	  * @return
	  */
	protected static Bitmap creatCodeBitmap(String contents,int width,int height,Context context) {
		TextView tv=new TextView(context);
	    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setText(contents);
        tv.setHeight(height);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setWidth(width);
        tv.setDrawingCacheEnabled(true);  
        tv.setTextColor(Color.BLACK);
        tv.measure(  
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),  
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
        tv.layout(0, 0, tv.getMeasuredWidth(),  
        		tv.getMeasuredHeight());
  
        tv.buildDrawingCache();  
        Bitmap bitmapCode=tv.getDrawingCache();
        return bitmapCode;
	}
	   
	   /**
	    * �����������Bitmap
	    * @param contents  ��Ҫ���ɵ�����
	    * @param format    �����ʽ
	    * @param desiredWidth 
	    * @param desiredHeight
	    * @return
	    * @throws WriterException
	    */
	  protected  static Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
		        int desiredWidth, int desiredHeight){
		        final int WHITE = 0xFFFFFFFF; 
		        final int BLACK = 0xFF000000;
		        
		        MultiFormatWriter writer = new MultiFormatWriter();
		        BitMatrix result=null;
				try {
					result = writer.encode(contents, format, desiredWidth,
					        desiredHeight, null);
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        int width = result.getWidth();
		        int height = result.getHeight();
		        int[] pixels = new int[width * height];
		        // All are 0, or black, by default
		        for (int y = 0; y < height; y++) {
		            int offset = y * width;
		            for (int x = 0; x < width; x++) {
		                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
		            }
		        }

		        Bitmap bitmap = Bitmap.createBitmap(width, height,
		                Bitmap.Config.ARGB_8888);
		        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		        return bitmap;
		    }

		    
		 /**
		  * ������Bitmap�ϲ���һ��
		  * @param first
		  * @param second
		  * @param fromPoint �ڶ���Bitmap��ʼ���Ƶ���ʼλ�ã�����ڵ�һ��Bitmap��
		  * @return 
		  */
		    protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second,PointF fromPoint) {  
		        if (first == null || second == null || fromPoint == null) {  
		            return null;  
		        }  
		        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth()+second.getWidth()+marginW, first.getHeight()+second.getHeight()
		        		, Config.ARGB_4444); 
		        Canvas cv = new Canvas(newBitmap);  
		        cv.drawBitmap(first,marginW,0,null);  
		        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);  
		        cv.save(Canvas.ALL_SAVE_FLAG);  
		        cv.restore();
		        
		        return newBitmap;  
		    }   

}

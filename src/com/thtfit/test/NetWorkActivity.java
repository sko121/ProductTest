package com.thtfit.test;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;






/**
 * @author 杨碧华
 * 
 * 2017年5月12日
 *
 * TODO 检测当前网络状态，从网上下载，并测量下载的速度
 */
public class NetWorkActivity extends Activity {
  

	private Button start,stop;
	
	private EditText address;
	DownloadManager downloadManager ;
	private TextView tv,tv2,tv3,tv4,status;
	private android.net.NetworkInfo wifi;
	private android.net.NetworkInfo mobile;
	private android.net.NetworkInfo ethernet;
	private ConnectivityManager connMgr;
	Boolean _pressed = false;
	long id;
	long idPro;
	private SharedPreferences prefs;  
    private static final String DL_ID = "downloadId";
    private float z;
	private float i;
	
	//Timer timer1 = new Timer(); 
    private float t=0;
    private float speed;
	private float speed2;
	private Handler mHnadler = new Handler(){  
		@Override  
	     public void handleMessage(Message msg) {  
	         switch (msg.what) {  
	             case 100:  
	            	 status.setText(msg.obj.toString());  
	            	 
	            	
			        i = msg.getData().getInt("i");
	              	 
				   speed = msg.arg1;
				   speed2 = msg.getData().getFloat("v2");
          	
	                 break; 
	           
	              
	                 
	         }  
	         super.handleMessage(msg);  
	     }  
	 };

	private DownloadManager.Request request;
	
   
	 
	 private BroadcastReceiver receiver1 = new BroadcastReceiver() {   
	        @Override   
	        public void onReceive(Context context, Intent intent) {   
	            //这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听  
	            Log.v("intent", ""+intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)); 
	            queryDownloadStatus();  
	          
	            z = (speed+speed2)/i;	 
           	    tv2.setText("平均下载速度："+String.valueOf(z)+ " KB/s");
           	    if(ethernet.isAvailable()){
           	    	tv3.setVisibility(View.VISIBLE);
           	    	if((float)((speed+speed2)/t)<=640){
             			tv3.setText("有线(平均下载速度<5Mb/s)：不正常");
             		}else{
    	            		
    	            		tv3.setText("有线(平均下载速度>5Mb/s)：正常");
    	            	}
           	    }
           	 if(wifi.isAvailable()&&!ethernet.isAvailable()){
         		tv4.setVisibility(View.VISIBLE);;
         		
         		if((float)((speed+speed2)/t)<=128){
         			tv4.setText("无线（平均下载速度<1Mb/s）：不正常");
         		}else{
	            		
	            		tv4.setText("无线(平均下载速度>=1Mb/s)：正常");
	            	}
        		
        	}
	         //abortBroadcast();
	            
	        }   
	    };

	private long id2;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_network);

         address = (EditText) findViewById(R.id.edit1);
         status=(TextView) findViewById(R.id.edit2);
         start=(Button) findViewById(R.id.btnDown);
         stop=(Button) findViewById(R.id.btnstop);
         tv = (TextView) findViewById(R.id.tv);
         tv2 = (TextView) findViewById(R.id.tv2);
         tv3 = (TextView) findViewById(R.id.tv3);
         tv4 = (TextView) findViewById(R.id.tv4);
       

        
         downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
         prefs = PreferenceManager.getDefaultSharedPreferences(this); 		
         
         new NetWorkSpeedUtils(this,mHnadler).startShowNetSpeed();	            
	     registerReceiver(receiver1, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
         
	
	}
	

	public void clickMe(View arg0) {
		// TODO Auto-generated method stub
		
		int switId = arg0.getId();
		switch (switId) {
		
		case R.id.btnDown:
			String url=address.getText().toString();
		    Uri uri=Uri.parse(encodeGB(address.getText().toString()));
			request = new DownloadManager.Request(uri);
			//request = new DownloadManager.Request(
					//Uri.parse("http://gdown.baidu.com/data/wisegame/55dc62995fe9ba82/jinritoutiao_448.apk"));
			request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
			request.setAllowedOverRoaming(false);
			 //设置文件类型  
	        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();  
	        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));  
	        request.setMimeType(mimeString);  
	        //在通知栏中显示   
	        request.setShowRunningNotification(true);  
	        request.setVisibleInDownloadsUi(true);  
	        //sdcard的目录下的download文件夹  
	        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "mydown"); 
	        request.setTitle("下载");
			_pressed = true;
			
			if(!prefs.contains(DL_ID)){
				
				 id2 = downloadManager.enqueue(request);
	            //保存id 
	            prefs.edit().putLong(DL_ID, id2).commit();
	            
			} 
			else {   
	            //下载已经开始，检查状态  
	            queryDownloadStatus();
	            Toast.makeText(getApplicationContext(), "若要重新下载，请点击clear清除已下载的该内容,", Toast.LENGTH_SHORT).show();
	        }   
		  
//			  new NetWorkSpeedUtils(this,mHnadler).startShowNetSpeed();	            
//	          registerReceiver(receiver1, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

			break;
		case R.id.btnstop:
			 //清除已下载的内容，重新下载  
            Log.v("down", "STATUS_FAILED");  
          //  NetWorkSpeedUtils.getInstance().getTimer().cancel();
            downloadManager.remove(prefs.getLong(DL_ID, 0));   
            prefs.edit().clear().commit();		
         //   downloadManager.remove(id2);
           
           
			break;

		default:
			break;
		}
		
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		connMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		ethernet=connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
		if (!isNetworkConnected()) {
			// Toast.makeText(getApplicationContext(), "ertgewrtergh",
			// Toast.LENGTH_SHORT).show();
			Log.e("ggggggggggggggggggggggggg", "fds");
			showSetNetworkDialog();
			tv.setText("网络状态：无网络连接");
		} else if (wifi.isAvailable()) {
			tv.setText("网络状态：wifi状态");
		} else if (mobile.isAvailable() && !wifi.isAvailable()) {
			tv.setText("网络状态：移动数据状态");
			showWifiNetworkDialog();
		} else {
			tv.setText("网络状态：有线");
		}
		
		  
		super.onResume();
		
	}
	 private void queryDownloadStatus() {   
	        DownloadManager.Query query = new DownloadManager.Query();   
	        query.setFilterById(prefs.getLong(DL_ID, 0));   
	        Cursor c = downloadManager.query(query);   
	        if(c.moveToFirst()) {   
	            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));   
	            switch(status) {   
	            case DownloadManager.STATUS_PAUSED:   
	                Log.v("down", "STATUS_PAUSED");  
	            case DownloadManager.STATUS_PENDING:   
	            	Toast.makeText(getApplicationContext(), "准备下载", Toast.LENGTH_SHORT).show();
	                Log.v("down", "STATUS_PENDING");  
	            case DownloadManager.STATUS_RUNNING:   
	                //正在下载，不做任何事情  
	                Log.v("down", "STATUS_RUNNING"); 
	                Toast.makeText(getApplicationContext(), "正在下载", Toast.LENGTH_SHORT).show();
	                break;   
	            case DownloadManager.STATUS_SUCCESSFUL:   
	                //完成  
	            	Toast.makeText(getApplicationContext(), "已下载", Toast.LENGTH_SHORT).show();
	                Log.v("down", "下载完成");  

	                
	                break;   
	            case DownloadManager.STATUS_FAILED:   
	                //清除已下载的内容，重新下载  
	                Log.v("down", "STATUS_FAILED");  
	                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
	                downloadManager.remove(prefs.getLong(DL_ID, 0));   
	                prefs.edit().clear().commit();   
	                break;   
	            }   
	        }  
	 }
	 
	 public String encodeGB(String string)  
	    {  
	        //转换中文编码  
	        String split[] = string.split("/");  
	        for (int i = 1; i < split.length; i++) {  
	            try {  
	                split[i] = URLEncoder.encode(split[i], "GB2312");  
	            } catch (UnsupportedEncodingException e) {  
	                e.printStackTrace();  
	            }  
	            split[0] = split[0]+"/"+split[i];  
	        }  
	        split[0] = split[0].replaceAll("\\+", "%20");//处理空格  
	        return split[0];  
	    }  




	
	    

	/**
	 * 获取当前手机的网络状态
	 * 
	 * @return
	 */
	private boolean isNetworkConnected() {

		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info != null && info.isConnected());

	}

	/**
	 * 提示网络状态不可用，并进行设置
	 */
	private void showWifiNetworkDialog() {

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("设置网络");
		builder.setMessage("当前为移动网络状态，建议切换到wifi状态");
		builder.setCancelable(false);
		builder.setPositiveButton("切换到wifi", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Intent intent = new Intent();
				// 类名一定要包含包名(这种显示意图不是很好，因为不同的系统可能包名，类名都不同，因此最好采用隐式意图进行跳转)
				// intent.setClassName("com.android.phone",
				// "com.android.phone.MiuiMobileNetworkSettings");

				Intent mIntent = new Intent();
				ComponentName comp = new ComponentName("com.android.settings",
						"com.android.settings.Settings");
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				// intent.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS);
				// startActivity(intent);
				startActivity(mIntent); // 如果在设置完成后需要再次进行操作，可以重写操作代码，在这里不再重写
				// finish();

			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.create().show();

	}

	/**
	 * 提示网络状态不可用，并进行设置
	 */
	private void showSetNetworkDialog() {

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("设置网络");
		builder.setMessage("网络错误，请检查网络状态");
		builder.setCancelable(false);
		builder.setPositiveButton("设置网络", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Intent intent = new Intent();
				// 类名一定要包含包名(这种显示意图不是很好，因为不同的系统可能包名，类名都不同，因此最好采用隐式意图进行跳转)
				// intent.setClassName("com.android.phone",
				// "com.android.phone.MiuiMobileNetworkSettings");

				Intent mIntent = new Intent();
				ComponentName comp = new ComponentName("com.android.settings",
						"com.android.settings.Settings");
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.MAIN");
				// intent.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS);
				// startActivity(intent);
				startActivity(mIntent); // 如果在设置完成后需要再次进行操作，可以重写操作代码，在这里不再重写
				// finish();

			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.create().show();

	}
	 @Override  
	    protected void onDestroy() {  
	        // TODO Auto-generated method stub  
	        super.onPause();  
//	       if(_pressed = true){
//	    	   unregisterReceiver(receiver1); 
//	       }
	        	 
	      
	       
	    }  
	


}

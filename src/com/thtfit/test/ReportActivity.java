package com.thtfit.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
public class ReportActivity extends Activity
{
	private ProductTest mProduct;
	private ListView lv;
	private ListViewAdapter lvAdapter;
//	private ArrayList<String> list;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
		mProduct = (ProductTest)getApplication();
        lv = (ListView) findViewById(R.id.ReportListView);
      //list=new ArrayList<Map<String,Object>>();
       // initDate();
        lvAdapter =new ListViewAdapter(mProduct.reportList, this);
        lv.setAdapter(lvAdapter);
    }
/*	
	//为了省事，这里就不改了，大概就是请求数据，返回json值进行解析
	private void initDate() {
		Map<String, Object> map = new HashMap<String, Object>();
		list.clear();
		map.put("title", "测试");	
		map.put("买糖", 2);
		map.put("买花", "51");
		list.add(map);
	}
		// TODO Auto-generated method stub
		list=getCapsuleListData("servlet/ShowCapsuleServlet.do");
	}
	public void onetreasuremorebtn(View v){
		Intent intent = new Intent (OneTreasureActivity.this,OneTreasureMore.class);			
		startActivity(intent);	
	}
	
	
	public List<Map<String,Object>> getCapsuleListData(String s){
    	Map<String,Object> map =new HashMap<String,Object>();
    	  try{
    		  String url=HttpUtil.BASE_URL+s;
    		  String body=HttpUtil.queryStringForPost(url);
				JSONArray array=new JSONArray(body);
				list.clear();
    		    for(int i=0;i<array.length();i++){
    		    	JSONObject obj=array.getJSONObject(i);
    		    	map.put("text",obj.getString("text"));
    		    	map.put("text1", obj.getString("text1"));
    		        list.add(map);		 
    		    	map =new HashMap<String,Object>(); 	
    		    }	 
    		   
    		    }catch(Exception e){
    		     e.printStackTrace();
    		    }
    	  return list;
    
    }
*/	
}

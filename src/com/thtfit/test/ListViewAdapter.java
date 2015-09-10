package com.thtfit.test;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;
public class ListViewAdapter extends BaseAdapter {
	//填充数据的List
	List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
	//上下文
	private Context context;
	//用来导入布局
	private LayoutInflater inflater =null;
	//构造器
	public ListViewAdapter(List<Map<String,Object>> list,Context context){
		this.context=context;
		this.list=list;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	//核心部分，返回Listview视图
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null){
			Log.v("ListViewAdapter", "convertView == null");
			holder=new ViewHolder();
			convertView = inflater.inflate(R.layout.listview, null);
			holder.title=(TextView)convertView.findViewById(R.id.textTitle);
			holder.tv=(TextView)convertView.findViewById(R.id.item_tv1);
			//为view设置标签
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.title.setText(list.get(position).get("title").toString()+":");
		holder.tv.setText(getMapMessage(position));
		return convertView;
	}
	static class ViewHolder {
		TextView title;
		TextView tv;
    } 
	private String getMapMessage(int position)
	{
		String result = "";
		Map<String, Object> map = list.get(position);
		Set keys = map.keySet();//所有的key的集合
		for(Object key : keys){
			if(key.equals("title"))
				continue;
			result = result + map.get(key).toString()+"次 "+key.toString()+"\n";
		}
		Log.v("ListViewAdapter", result);
		return result;
	}
}

package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ServiceCountsAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;

	public ServiceCountsAdapter(Context context){
		this.context=context;
	}
	
	@Override
	public int getCount() {
		if(array!=null) {
			return array.size();
		}else {
			return 0;
		}
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_service_counts, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		JSONObject jsonObject=array.getJSONObject(position);

		holder.typeTv.setText(typeOf(jsonObject.getInt("Type")));
		holder.countsTv.setText(jsonObject.getInt("Counts")+"");

		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.service_counts_type_tv)
		TextView typeTv;
		@ViewInject(R.id.service_counts_tv)
		TextView countsTv;
	}

	String typeOf(int i){
		String str=null;
		switch (i){
			case 0:
				str=context.getResources().getString(R.string.pic_text);
				break;
			case 1:
				str=context.getResources().getString(R.string.video_audio);
				break;
			case 2:
				str=context.getResources().getString(R.string.hospital_treatment);
				break;
			case 3:
				str=context.getResources().getString(R.string.visit_service);
				break;
			default:
				str="";
				break;
		}
		return str;
	}
	
}

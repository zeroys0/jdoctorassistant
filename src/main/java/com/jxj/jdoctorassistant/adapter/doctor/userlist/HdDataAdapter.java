package com.jxj.jdoctorassistant.adapter.doctor.userlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.doctor.userlist.HdDataActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HdDataAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;

	private int type;
//	private String[] Type;
//	private String[] Unit;
	public HdDataAdapter(Context context){
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

	public void setType(int type) {
		this.type = type;
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

//	public void setType(String[] type) {
//		Type = type;
//	}
//
//	public void setUnit(String[] unit) {
//		Unit = unit;
//	}

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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_hd_data, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		JSONObject jsonObject=array.getJSONObject(position);
//"PS":110,"PD":90,
		if(type==0){
			holder.valueTv.setText(jsonObject.getString("PS")+"/"+jsonObject.getString("PD"));
		}else {
			if(jsonObject.has(HdDataActivity.TYPE[type])) {
				holder.valueTv.setText(jsonObject.getString(HdDataActivity.TYPE[type]));
			}else {
				holder.valueTv.setText("--");
			}
		}
		holder.unitTv.setText(HdDataActivity.UNIT[type]);
		holder.timeTv.setText(jsonObject.getString("TestDate"));
		holder.sourceTv.setText(jsonObject.getString("DataSources"));

		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.hd_data_source_tv)
		TextView sourceTv;
		@ViewInject(R.id.hd_data_time_tv)
		TextView timeTv;
		@ViewInject(R.id.hd_data_value_tv)
		TextView valueTv;
		@ViewInject(R.id.hd_data_unit_tv)
		TextView unitTv;
	}
	
}

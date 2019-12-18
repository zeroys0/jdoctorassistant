package com.jxj.jdoctorassistant.adapter.doctor.userlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ScheduleListAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;
	private String[] types;
	private String[] states;

	public ScheduleListAdapter(Context context){
		this.context=context;
		types=new String[]{context.getResources().getString(R.string.pic_text),
							context.getResources().getString(R.string.video_audio),
							context.getResources().getString(R.string.hospital_treatment),
							context.getResources().getString(R.string.visit_service)};
		states=context.getResources().getStringArray(R.array.arr_schedule_state);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_schedule_customer, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		JSONObject jsonObject=array.getJSONObject(position);
		holder.timeTv.setText(jsonObject.getString("BeginDatetime"));
		holder.typrTv.setText(types[jsonObject.getInt("Type")]);
		holder.stateTv.setText(states[jsonObject.getInt("Status")]);
		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.time_tv)
		TextView timeTv;
		@ViewInject(R.id.type_tv)
		TextView typrTv;
		@ViewInject(R.id.state_tv)
		TextView stateTv;
	}
	
}

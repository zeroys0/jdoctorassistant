package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnConfirmListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ScheduleAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private JSONArray array;
	private String[] typeArr;
	private OnConfirmListener onConfirmListener;


	public ScheduleAdapter(Context context,OnConfirmListener onConfirmListener){
		this.context=context;
		this.onConfirmListener = onConfirmListener;
		layoutInflater=LayoutInflater.from(context);

	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	public void setTypeArr(String[] typeArr) {
		this.typeArr = typeArr;
	}

	@Override
	public int getCount() {
		if(array!=null) {
			return array.size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return array.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_schedule, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}


		JSONObject jsonObject=array.getJSONObject(position);
		holder.timeTv.setText(jsonObject.getString("BeginDatetime").split(" ")[1].substring(0,5));
		holder.typeTv.setText(typeArr[jsonObject.getInt("Type")]);
		holder.stateTv.setText("");

		holder.nameTv.setText(jsonObject.getString("Cname"));
		holder.infoTv.setText(jsonObject.getString("Community"));
		holder.symptomTv.setText(jsonObject.getString("Symptoms"));
		if(jsonObject.getInt("Status")==0x03) {
			holder.btn_complete.setVisibility(View.VISIBLE);
			holder.btn_complete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onConfirmListener.onConfirm(v);
				}
			});
		}
		return convertView;
	}

	
	class ViewHolder{
		@ViewInject(R.id.schedule_time_tv)
		TextView timeTv;
		@ViewInject(R.id.schedule_type_tv)
		TextView typeTv;
		@ViewInject(R.id.schedule_state_tv)
		TextView stateTv;
		@ViewInject(R.id.schedule_user_igv)
		ImageView userIgv;
		@ViewInject(R.id.schedule_user_name_tv)
		TextView nameTv;
		@ViewInject(R.id.schedule_user_info_tv)
		TextView infoTv;
		@ViewInject(R.id.schedule_user_symptom_tv)
		TextView symptomTv;
		@ViewInject(R.id.btn_complete)
		Button btn_complete;
	}
	
}

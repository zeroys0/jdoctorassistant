package com.jxj.jdoctorassistant.adapter;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.jxj.jdoctorassistant.R;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HeartRecordAdapter extends BaseAdapter {
	class ViewHolder {

		@Bind(R.id.heart_record_time_tv)
		public TextView time;
		@Bind(R.id.heart_record_tv)
		public TextView record;
		@Bind(R.id.heart_record_img)
		public ImageView image;

	}

	private Context context;
	private JSONArray jsonarray;
	private LayoutInflater layoutinflater;

	public HeartRecordAdapter(JSONArray jsonarray, Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
		this.jsonarray = jsonarray;
	}

	public void setJsonarray(JSONArray jsonarray) {
		this.jsonarray = jsonarray;
	}

	@Override
	public int getCount() {
		if (jsonarray!=null) {
			return jsonarray.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutinflater.inflate(R.layout.lv_item_heart_record,
					null);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		JSONObject json = jsonarray.getJSONObject(position);
		viewHolder.record.setText(String.valueOf(json.getInt("HR")));
		viewHolder.time.setText(String.valueOf(json.getString("TestTime")));
		if(json.getInt("HR")<30){
			viewHolder.image.setImageResource(R.drawable.heart_record_worn);
			viewHolder.record.setTextColor(context.getResources().getColor(
					R.color.gray));
		}else if (json.getInt("HR") < 60) {
			viewHolder.image.setImageResource(R.drawable.heart_record_low);
			viewHolder.record.setTextColor(context.getResources().getColor(
					R.color.yellow));
		} else if (json.getInt("HR") <120) {
			viewHolder.record.setTextColor(context.getResources().getColor(
					R.color.black));
			viewHolder.image.setImageResource(R.color.white_bg);
		} else if (json.getInt("HR") <220) {
			viewHolder.image.setImageResource(R.drawable.heart_record_high);
			viewHolder.record.setTextColor(context.getResources().getColor(
					R.color.red));
		} else{
			viewHolder.image.setImageResource(R.drawable.heart_record_worn);
			viewHolder.record.setTextColor(context.getResources().getColor(
					R.color.gray));
		}

		return convertView;

	}

}

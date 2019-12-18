package com.jxj.jdoctorassistant.adapter.thirdData;

import java.util.List;

import com.jxj.jdoctorassistant.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ThirdDataBfAdapter extends BaseAdapter {


	class ViewHolder {
		@Bind(R.id.test_time_tv)
		public TextView testTimeTv;
		@Bind(R.id.weight_goals_tv)
		public TextView weightGoalsTv;
		@Bind(R.id.measuring_weight_tv)
		public TextView measuringWeightTv;
		@Bind(R.id.body_fat_ratio_tv)
		public TextView bodyFatRatioTv;
		@Bind(R.id.basal_metabolism_tv)
		public TextView basalMetabolismTv;
		@Bind(R.id.visceral_fat_rating_tv)
		public TextView visceralFatRatingTv;
		@Bind(R.id.bone_mass_tv)
		public TextView boneMassTv;
		@Bind(R.id.muscle_tv)
		public TextView muscleTv;
		@Bind(R.id.body_impedance_tv)
		public TextView bodyImpedanceTv;
		@Bind(R.id.moisture_tv)
		public TextView moistureTv;
	}
	
	private Context context;
	private LayoutInflater inflater;
	private JSONArray array;

	public ThirdDataBfAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(array==null){
			return 0;
		}else {
			return array.size();
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
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lv_item_third_data_bf, null);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		JSONObject json=array.getJSONObject(position);

		viewHolder.testTimeTv.setText(json.getString("TestTime"));
		viewHolder.weightGoalsTv.setText(json.getInt("TargetWeight")+"kg");
		viewHolder.measuringWeightTv.setText(json.getInt("Weight")+"kg");
		viewHolder.bodyFatRatioTv.setText(String.valueOf(json.getInt("BFR")));
		viewHolder.basalMetabolismTv.setText(String.valueOf(json.getInt("BM")));
		viewHolder.visceralFatRatingTv.setText(String.valueOf(json.getInt("VFL")));
		viewHolder.boneMassTv.setText(String.valueOf(json.getDouble("Bone")));
		viewHolder.muscleTv.setText(String.valueOf(json.getDouble("Muscle")));
		viewHolder.bodyImpedanceTv.setText(String.valueOf(json.getInt("Impedance")));
		viewHolder.moistureTv.setText(String.valueOf(json.getDouble("Moisture")));


		return convertView;
	}

}

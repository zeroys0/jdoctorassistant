package com.jxj.jdoctorassistant.adapter;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;

import butterknife.Bind;
import butterknife.ButterKnife;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BloodMeasureRecordAdapter extends BaseAdapter {

	private SharedPreferences sp;
	private int psValue, pdValue;

	class ViewHolder {

		@Bind(R.id.recode_time_tv)
		public TextView testDate;
		@Bind(R.id.recode_pulse_tv)
		public TextView pr;
		@Bind(R.id.recode_sp_tv)
		public TextView ps;
		@Bind(R.id.recode_dp_tv)
		public TextView pd;

		@Bind(R.id.recode_img)
		public ImageView image;

	}

	private Context context;
	private JSONArray jsonarray;
	private LayoutInflater layoutinflater;

	public BloodMeasureRecordAdapter(JSONArray jsonarray, Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
		this.jsonarray = jsonarray;
	}

	public void setJsonarray(JSONArray jsonarray) {
		this.jsonarray = jsonarray;
	}

	@Override
	public int getCount() {
		if (jsonarray != null) {
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutinflater.inflate(
					R.layout.lv_item_measure_record, null);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		JSONObject jsonObj = jsonarray.getJSONObject(position);
		sp = context.getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);

		String mProductModel = sp.getString(AppConstant.USER_jwotchModel, null);
		System.out.println(mProductModel+"========mProductModel");
//		int age = sp.getInt(AppConstant.USER_age, 18);
//		int sex = sp.getInt(AppConstant.USER_sex, 0);

//		calculatePR(sex, age);

		if (mProductModel.equals("JXJ-HM041")) {
			String[] dateStr = jsonObj.getString("TestTime").split(" ");
			viewHolder.testDate.setText(dateStr[dateStr.length - 1]);
			viewHolder.pr.setText(jsonObj.getString("HR"));
		} else {
			viewHolder.testDate.setText(jsonObj.getString("TestDate")
					.substring(11, 16));
			viewHolder.pr.setText(jsonObj.getString("PR"));
		}

		int ps = jsonObj.getInt("PS");
		int pd = jsonObj.getInt("PD");

		String state = null;
		if (ps < 1 || pd < 1) {
			viewHolder.image.setBackground(context.getResources().getDrawable(
					R.drawable.heart_record_worn));
		} else if (ps < 90 || pd < 60) {
			viewHolder.image.setBackground(context.getResources().getDrawable(
					R.drawable.lower));
		} else if (ps < 139 || pd < 89) {
			viewHolder.image.setBackground(null);			
		} else if (ps < 159 || pd < 99) {
			viewHolder.image.setBackground(null);			
		} else if (ps <181|| pd <111) {			
			viewHolder.image.setBackground(context.getResources().getDrawable(
					R.drawable.higher));
		} else {
			viewHolder.image.setBackground(context.getResources().getDrawable(
					R.drawable.highst));

		}

		viewHolder.ps.setText(jsonObj.getString("PS"));
		viewHolder.pd.setText(jsonObj.getString("PD"));

		return convertView;

	}

//	private void calculatePR(int sex, int age) {
//		if (sex == 0)// 男
//		{
//			if (age < 21) {
//				psValue = 115;
//				pdValue = 73;
//			} else if (age < 26) {
//				psValue = 115;
//				pdValue = 73;
//			} else if (age < 31) {
//				psValue = 115;
//				pdValue = 75;
//			} else if (age < 36) {
//				psValue = 117;
//				pdValue = 76;
//			} else if (age < 41) {
//				psValue = 120;
//				pdValue = 80;
//			} else if (age < 46) {
//				psValue = 124;
//				pdValue = 81;
//			} else if (age < 51) {
//				psValue = 128;
//				pdValue = 82;
//			} else if (age < 56) {
//				psValue = 134;
//				pdValue = 84;
//			} else if (age < 61) {
//				psValue = 137;
//				pdValue = 84;
//			} else {
//				psValue = 148;
//				pdValue = 86;
//			}
//		} else {
//			if (age < 21) {
//				psValue = 110;
//				pdValue = 70;
//			} else if (age < 26) {
//				psValue = 110;
//				pdValue = 71;
//			} else if (age < 31) {
//				psValue = 112;
//				pdValue = 73;
//			} else if (age < 36) {
//				psValue = 114;
//				pdValue = 74;
//			} else if (age < 41) {
//				psValue = 116;
//				pdValue = 77;
//			} else if (age < 46) {
//				psValue = 122;
//				pdValue = 78;
//			} else if (age < 51) {
//				psValue = 128;
//				pdValue = 79;
//			} else if (age < 56) {
//				psValue = 134;
//				pdValue = 80;
//			} else if (age < 61) {
//				psValue = 139;
//				pdValue = 82;
//			} else {
//				psValue = 145;
//				pdValue = 83;
//			}
//		}
//	}
}

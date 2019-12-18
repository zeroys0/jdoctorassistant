package com.jxj.jdoctorassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ChartDataListAdapter extends BaseAdapter {

	private class ViewHolder {

		@ViewInject(R.id.calorie_time_tv)
		public TextView time;
		@ViewInject(R.id.data_tv)
		public TextView title;
		@ViewInject(R.id.data_unit_tv)
		public TextView unit;

	}

	private Context context;
	private LayoutInflater layoutinflater;
	private JSONArray jsonarray;
	private int type;
//	private String jWotchModel;

	public ChartDataListAdapter(Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
	}

	public void setJsonarray(JSONArray jsonarray, int type) {
		this.jsonarray = jsonarray;
		this.type = type;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutinflater.inflate(R.layout.lv_item_data_chart,
					null);
			viewHolder = new ViewHolder();
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		JSONObject jsonObj = jsonarray.getJSONObject(position);


		switch (type) {
		case AppConstant.CHART_CAL:
			viewHolder.title.setText(jsonObj.getString("Calorie")+" kcal");
			viewHolder.unit.setText(jsonObj.getString("SetpNumber")+" "+context.getResources().getString(R.string.step));
			viewHolder.time.setText(jsonObj.getString("TestDate").split(" ")[0]);
			break;
		case AppConstant.CHART_HR:
			if(jsonObj.has("HR")){
				viewHolder.title.setText(jsonObj.getString("HR"));
				viewHolder.time.setText(jsonObj.getString("TD"));
			}else if(jsonObj.has("PR")){
				viewHolder.title.setText(jsonObj.getString("PR"));
				viewHolder.time.setText(jsonObj.getString("TestDate"));
			}

			viewHolder.unit.setText(context.getResources().getString(R.string.pr_unit));

			break;
		case AppConstant.CHART_BP:
			viewHolder.title.setText(jsonObj.getString("PS")+"     "+jsonObj.getString("PD"));
			viewHolder.unit.setText("mmHg");
			if(jsonObj.has("TD")){
				viewHolder.time.setText(jsonObj.getString("TD"));
			}else if(jsonObj.has("TestDate")){
				String testDate=jsonObj.getString("TestDate");
				viewHolder.time.setText(testDate.split(" ")[0]);
			}

			break;

		default:
			break;
		}
		

		return convertView;

	}

}

package com.jxj.jdoctorassistant.adapter;

import com.jxj.jdoctorassistant.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ToolsSportsRecordListAdapter extends BaseAdapter {

	class ViewHolder {

		@Bind(R.id.sport_time_tv)
		public TextView time;
		@Bind(R.id.sport_title_tv)
		public TextView title;

	}

	private Context context;
	private LayoutInflater layoutinflater;
	private JSONArray jsonarray;

	public ToolsSportsRecordListAdapter(Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutinflater.inflate(
					R.layout.lv_item_tools_sports_recode, null);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		JSONObject jsonObj = jsonarray.getJSONObject(position);

		viewHolder.time.setText(jsonObj.getString("StartTime"));

		int str = jsonObj.getInt("SportType");
		viewHolder.title.setText(convertType(str));
		if (str == 0) {
			viewHolder.title.setTextColor(context.getResources().getColor(
					R.color.login_loginBtn_bgColor));
		} else {
			viewHolder.title.setTextColor(context.getResources().getColor(
					R.color.red));
		}

		return convertView;

	}

	public String convertType(int str) {
		String res = null;
		if (str == 0) {
			res = context.getResources().getString(R.string.sport_monitor);

		} else {
			res = context.getResources().getString(R.string.activity_monitor);
		}
		return res;

	}

}

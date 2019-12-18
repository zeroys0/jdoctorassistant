package com.jxj.jdoctorassistant.adapter;

import com.jxj.jdoctorassistant.R;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ToolsLocationListAdapter extends BaseAdapter {

	class ViewHolder {

		@Bind(R.id.location_time_tv)
		public TextView time;
		@Bind(R.id.location_title_tv)
		public TextView title;

	}

	private Context context;
	private LayoutInflater layoutinflater;
	private JSONArray jsonarray;

	public ToolsLocationListAdapter(Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
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
			convertView = layoutinflater.inflate(
					R.layout.lv_item_tools_location,parent,false);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		JSONObject jsonObj = jsonarray.getJSONObject(position);

		viewHolder.time.setText(jsonObj.getString("TestTime"));

		String str = jsonObj.getString("Type");

		viewHolder.title.setText(context.getResources()
				.getString(R.string.type) + "：" + convertType(str));

		return convertView;

	}

	public String convertType(String str) {
		String res = null;
		if (str.equals("GENERAL")) {
			res = context.getResources().getString(R.string.general_location);
		} else if (str.equals("REDCALL")) {
			res = context.getResources().getString(R.string.red_location);
		} else if (str.equals("GREENCALL")) {
			res = context.getResources().getString(R.string.green_location);
		} else {
			res = " ";
		}
		return res;

	}

}

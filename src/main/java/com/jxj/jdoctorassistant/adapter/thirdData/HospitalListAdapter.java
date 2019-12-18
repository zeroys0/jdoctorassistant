package com.jxj.jdoctorassistant.adapter.thirdData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HospitalListAdapter extends BaseAdapter {

	class ViewHolder {
		@Bind(R.id.hospital_name_tv)
		public TextView nameTv;
	}

	private Context context;
	private LayoutInflater layoutinflater;
	private JSONArray jsonarray;

	public HospitalListAdapter(Context context) {
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
					R.layout.lv_item_hospital,parent,false);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		JSONObject jsonObj = jsonarray.getJSONObject(position);

		viewHolder.nameTv.setText(jsonObj.getString("name"));

		return convertView;

	}



}

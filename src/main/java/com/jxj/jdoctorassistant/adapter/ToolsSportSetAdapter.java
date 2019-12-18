package com.jxj.jdoctorassistant.adapter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ToolsSportSetAdapter extends BaseAdapter {

	public static interface ToolsSportSetDelete {
		void deleteItem(int id);

		void updateItem(int position, int id);
	}

	private Context context;
	private LayoutInflater layoutinflater;
	private JSONArray jsonArray;
	private ToolsSportSetDelete mDelegate;

	public ToolsSportSetAdapter(Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
	}

	public class ViewHolder {
		@ViewInject(R.id.sport_starttime_tv)
		public TextView starttime;
		@ViewInject(R.id.sport_endtime_tv)
		
		public TextView endtime;

		@ViewInject(R.id.sport_delete_rl)
		public RelativeLayout sportDeleteRl;
		@ViewInject(R.id.sport_update_rl)
		public RelativeLayout sportUpdateRl;

	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public void setmDelegate(ToolsSportSetDelete mDelegate) {
		this.mDelegate = mDelegate;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (jsonArray != null) {
			return jsonArray.size();
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
			convertView = layoutinflater.inflate(R.layout.lv_item_tools_sports,
					null);
			viewHolder = new ViewHolder();
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		JSONObject jsonSend = jsonArray.getJSONObject(position);
		viewHolder.starttime.setText(convertDate(jsonSend
				.getString("StartTimeHour"))
				+ ":"
				+ convertDate(jsonSend.getString("StartTimeMinute")));
		viewHolder.endtime.setText(convertDate(jsonSend
				.getString("StopTimeHour"))
				+ ":"
				+ convertDate(jsonSend.getString("StopTimeMinute")));

		final int pos = position;
		final int id = jsonSend.getInt("id");
		viewHolder.sportDeleteRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (null != mDelegate) {
					mDelegate.deleteItem(id);
				}
			}
		});
		viewHolder.sportUpdateRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (null != mDelegate) {
					mDelegate.updateItem(pos, id);
				}
			}
		});
		return convertView;

	}

	private String convertDate(String str) {
		if (str.length() == 1) {
			str = "0" + str;
		}
		return str;
	}
}

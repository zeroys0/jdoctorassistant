package com.jxj.jdoctorassistant.adapter;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.util.GetDate;

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

public class ToolsRemindSetAdapter extends BaseAdapter {

	public static interface ToolsRemindSetDelete {
		void deleteItem(int id);

		void updateItem(int position, int id);
	}

	private Context context;
	private LayoutInflater layoutinflater;
	private JSONArray jsonArray;
	private ToolsRemindSetDelete mDelegate;

	public ToolsRemindSetAdapter(Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
	}

	public class ViewHolder {
		@Bind(R.id.remind_title_tv)
		public TextView titleTxt;
		@Bind(R.id.remind_cycle_tv)
		public TextView cycleTxt;
		@Bind(R.id.remind_delete_rl)
		public RelativeLayout remindDeleteRl;
		@Bind(R.id.remind_update_rl)
		public RelativeLayout remindUpdateRl;

	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public void setmDelegate(ToolsRemindSetDelete mDelegate) {
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
			convertView = layoutinflater.inflate(
					R.layout.lv_item_tools_remindset, null);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		JSONObject json = jsonArray.getJSONObject(position);
		try{
		int type = json.getInt("Type");
		viewHolder.titleTxt.setText(json.getString("Title"));

		String time=GetDate.convertTimeOrDate(json.getString("Hour"))+":"+GetDate.convertTimeOrDate(json.getString("Minute"));
		
		if (type == 0) {
			viewHolder.cycleTxt.setText(context.getResources().getString(R.string.cycle_one) + "  " + time);
		} else if (type == 127) {
			viewHolder.cycleTxt.setText(context.getResources().getString(R.string.cycle_day) + "  " + time);
		} else {
			viewHolder.cycleTxt.setText(context.getResources().getString(R.string.cycle_self) + "  " + time);
		}

		final int pos = position;
		final int id = json.getInt("Id");
		viewHolder.remindDeleteRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (null != mDelegate) {
					mDelegate.deleteItem(id);
				}
			}
		});
		viewHolder.remindUpdateRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (null != mDelegate) {
					mDelegate.updateItem(pos, id);
				}
			}
		});
		}catch(Exception e){
			e.printStackTrace();
		}
		return convertView;

	}
}

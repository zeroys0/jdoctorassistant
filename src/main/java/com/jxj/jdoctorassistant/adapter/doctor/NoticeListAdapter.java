package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NoticeListAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;

	public NoticeListAdapter(Context context){
		this.context=context;
	}
	
	@Override
	public int getCount() {
		if(array!=null) {
			return array.size();
		}else {
			return 0;
		}
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_notice, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		JSONObject jsonObject=array.getJSONObject(position);

		if(position==0){
			boolean isTop=jsonObject.getBoolean("TopRelease");
			if(isTop){
				holder.isTopTv.setVisibility(View.VISIBLE);
			}else {
				holder.isTopTv.setVisibility(View.GONE);
			}
		}else {
			holder.isTopTv.setVisibility(View.GONE);
		}
		holder.titleTv.setText(jsonObject.getString("Title"));
		holder.contentTv.setText(jsonObject.getString("Content"));
		holder.timeTv.setText(jsonObject.getString("ReleaseDatetime"));

		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.notice_content_tv)
		TextView contentTv;
		@ViewInject(R.id.notice_time_tv)
		TextView timeTv;
		@ViewInject(R.id.notice_title_tv)
		TextView titleTv;
		@ViewInject(R.id.notice_istop_tv)
		TextView isTopTv;
	}
	
}

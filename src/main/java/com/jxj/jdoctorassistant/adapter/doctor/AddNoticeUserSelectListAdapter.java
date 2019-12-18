package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AddNoticeUserSelectListAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;

	public AddNoticeUserSelectListAdapter(Context context){
		this.context=context;
	}

	private boolean[] arrIsSelect;
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

	public void setArrIsSelect(boolean[] arrIsSelect) {
		this.arrIsSelect = arrIsSelect;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_add_notice_select_user, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		JSONObject jsonObject=array.getJSONObject(position);
		holder.userTv.setText(jsonObject.getString("Cname"));
		if(arrIsSelect[position]){
			holder.stateIgv.setImageDrawable(context.getResources().getDrawable(R.drawable.rb_checked));
		}else {
			holder.stateIgv.setImageDrawable(context.getResources().getDrawable(R.drawable.rb_unchecked));
		}

		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.user_tv)
		TextView userTv;
		@ViewInject(R.id.user_state_igv)
		ImageView  stateIgv;
	}
	
}

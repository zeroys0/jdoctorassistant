package com.jxj.jdoctorassistant.adapter;

import java.util.List;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.model.ControlCenterItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ControlCenterAdapter extends BaseAdapter {

	private Context context;
	private int resourceId;
	private List<ControlCenterItem> list;
	
	public ControlCenterAdapter(Context context,int resourceId,List<ControlCenterItem> list) {
		this.context=context;
		this.resourceId=resourceId;
		this.list=list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ControlCenterItem item=(ControlCenterItem) getItem(position);
		ViewHolder holder;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(resourceId, parent, false);
			holder=new ViewHolder();
			holder.itemImage=(ImageView) convertView.findViewById(R.id.control_center_item_image);
			holder.itemText=(TextView) convertView.findViewById(R.id.control_center_item_text);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.itemImage.setImageResource(item.getResourceId());
		holder.itemText.setText(item.getItemString());
		return convertView;
	}

	class ViewHolder{
		ImageView itemImage;
		TextView itemText;
	}
}

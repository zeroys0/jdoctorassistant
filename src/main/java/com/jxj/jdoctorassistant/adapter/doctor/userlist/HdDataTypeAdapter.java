package com.jxj.jdoctorassistant.adapter.doctor.userlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HdDataTypeAdapter extends BaseAdapter {

	private Context context;
	private String[] array;
	private int selectPos;

	public HdDataTypeAdapter(Context context){
		this.context=context;
	}
	
	@Override
	public int getCount() {
		if(array!=null) {
			return array.length;
		}else {
			return 0;
		}
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	public void setSelectPos(int selectPos) {
		this.selectPos = selectPos;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_hd_data_type, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

//		holder.igv.setText(user.getCustomerId()+"");
		holder.tv.setText(array[position]);
		if(selectPos==position){
			holder.tv.setTextColor(context.getResources().getColor(R.color.white));
			holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.blue_corners5_bg));
		}else {
			holder.tv.setTextColor(context.getResources().getColor(R.color.black));
			holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.black_border_white_corners5_bg));
		}

//		holder.contentTv.setText(" ");
		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.hd_data_tv)
		TextView tv;
	}
	
}

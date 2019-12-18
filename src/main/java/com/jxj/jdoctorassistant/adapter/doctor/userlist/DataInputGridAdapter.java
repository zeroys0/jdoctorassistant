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

import net.sf.json.JSONArray;

public class DataInputGridAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
//	private JSONArray array;
	private String[] arr;
	private boolean[] boolArr;

	public DataInputGridAdapter(Context context){
		this.context=context;
		layoutInflater=LayoutInflater.from(context);
	}

	public void setArr(String[] arr) {
		this.arr = arr;
	}

	public void setBoolArr(boolean[] boolArr) {
		this.boolArr = boolArr;
	}

	@Override
	public int getCount() {
		return arr.length;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.gv_item_data_input, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		holder.tv.setText(arr[position]);

		if(boolArr[position]){
			holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.blue_corners5_bg));
			holder.tv.setTextColor(context.getResources().getColor(R.color.white));
		}else {
			holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.black_border_white_corners5_bg));
			holder.tv.setTextColor(context.getResources().getColor(R.color.black));
		}
//		holder.igv.setText(user.getCustomerId()+"");
//		holder.titleTv.setText(" ");
//		holder.contentTv.setText(" ");
		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.data_input_gv_item_tv)
		TextView tv;
//		@ViewInject(R.id.patient_function_lv_tv)
//		TextView tv;
	}
	
}

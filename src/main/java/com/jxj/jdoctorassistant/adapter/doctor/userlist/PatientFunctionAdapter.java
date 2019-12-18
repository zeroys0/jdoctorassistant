package com.jxj.jdoctorassistant.adapter.doctor.userlist;

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

public class PatientFunctionAdapter extends BaseAdapter {

	private Context context;
	private int[] img;
	private String[] name;

	public PatientFunctionAdapter(Context context){
		this.context=context;
	}

	public void setImg(int[] img) {
		this.img = img;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	@Override
	public int getCount() {
		return name.length;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.gv_item_patient_function, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		holder.igv.setImageResource(img[position]);
		holder.tv.setText(name[position]);
		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.patient_function_lv_igv)
		ImageView igv;
		@ViewInject(R.id.patient_function_lv_tv)
		TextView tv;
	}
	
}

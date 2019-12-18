package com.jxj.jdoctorassistant.adapter.doctor.userlist;

import android.content.Context;
import android.util.Log;
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

public class ExceptionRecordAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private JSONArray array;
	private String[] arrDataType;

	public ExceptionRecordAdapter(Context context){
		this.context=context;
		layoutInflater=LayoutInflater.from(context);
		arrDataType=new String[]{context.getResources().getString(R.string.bp),
				context.getResources().getString(R.string.hr),
				context.getResources().getString(R.string.blood_oxygen),
				context.getResources().getString(R.string.bloodsugar),
				" ",
				" ",
				context.getResources().getString(R.string.tempera),
				" ",
				context.getResources().getString(R.string.body_fat),
				context.getResources().getString(R.string.water),
				context.getResources().getString(R.string.data_muscle),
				context.getResources().getString(R.string.bone_mass),
				context.getResources().getString(R.string.data_metabolism)
		};
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
		return array.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_exception_record, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		JSONObject jsonObject=array.getJSONObject(position);
		holder.timeTv.setText(jsonObject.getString("CrtDate").substring(5).replace(" ","\n"));
		Log.d( "用户信息:错误到底出现在哪",jsonObject.toString());
		holder.typeTv.setText(arrDataType[jsonObject.getInt("DType")]);
		int num=jsonObject.getInt("DSID");
		if(num<1||num>15){
			holder.sourceTv.setText(" ");
		}else {
			holder.sourceTv.setText(context.getResources().getStringArray(R.array.arr_data_source)[num
					-1]);
		}

		holder.valueTv.setText(jsonObject.getString("DValue"));
		holder.rangeTv.setText(jsonObject.getString("RValue"));
		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.exception_record_time_tv)
		TextView timeTv;
		@ViewInject(R.id.exception_record_type_tv)
		TextView typeTv;
		@ViewInject(R.id.exception_record_source_tv)
		TextView sourceTv;
		@ViewInject(R.id.exception_record_value_tv)
		TextView valueTv;
		@ViewInject(R.id.exception_record_range_tv)
		TextView rangeTv;
	}
	
}

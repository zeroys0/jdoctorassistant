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
import net.sf.json.JSONObject;

public class TopDataAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;
	private String[] unit;
	private int[] img;

	public TopDataAdapter(Context context){
		this.context=context;
		img=new int[]{
				R.drawable.b_hd_data_0,
				R.drawable.b_hd_data_1,
				R.drawable.b_hd_data_2,
				R.drawable.b_hd_data_3,
				R.drawable.b_hd_data_4,
				R.drawable.b_hd_data_5,
				R.drawable.b_hd_data_6,
				R.drawable.b_hd_data_7,
				R.drawable.b_hd_data_8,
				R.drawable.b_hd_data_9,
				R.drawable.b_hd_data_10,
				R.drawable.b_hd_data_11,
				R.drawable.b_hd_data_12};
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

	public void setUnit(String[] unit) {
		this.unit = unit;
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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_top_data, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		JSONObject jsonObject=array.getJSONObject(position);
//
		holder.topDataIgv.setImageResource(img[Integer.parseInt(jsonObject.getString("Type"))]);
		holder.valueTv.setText(jsonObject.getString("Values"));
		holder.unitTv.setText(unit[Integer.parseInt(jsonObject.getString("Type"))]);
		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.top_data_igv)
		ImageView topDataIgv;
		@ViewInject(R.id.top_data_value_tv)
		TextView valueTv;
		@ViewInject(R.id.top_data_unit_tv)
		TextView unitTv;
	}
	
}

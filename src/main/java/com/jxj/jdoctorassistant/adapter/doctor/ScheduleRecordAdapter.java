package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ScheduleRecordAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private JSONArray array;
	private String[] typeArr;
	private boolean[] checkArr;
	private boolean isDelete;
	private int selectId=-1;

	public ScheduleRecordAdapter(Context context){
		this.context=context;
		layoutInflater=LayoutInflater.from(context);
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	public void setTypeArr(String[] typeArr) {
		this.typeArr = typeArr;
	}

	public void setSelectId(int selectId) {
		this.selectId = selectId;
	}

	public void setCheckArr(boolean[] checkArr) {
		this.checkArr = checkArr;
	}

	public void setDelete(boolean delete) {
		isDelete = delete;
	}

	@Override
	public int getCount() {
		if(array!=null){
			return array.size();
		}else {
			return 0;
		}
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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_schedule_record, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		JSONObject jsonObject=array.getJSONObject(position);
		String beginTime=jsonObject.getString("BeginTime");
		String endTime=jsonObject.getString("EndTime").split(" ")[1];
		holder.timeTv.setText(beginTime.substring(0,beginTime.length()-3)+"～"+endTime.substring(0,endTime.length()-3));
		holder.numberTv.setText("人数："+jsonObject.getInt("MaxCount")+"人");
//		holder.typeTv.setText("类别："+typeArr[jsonObject.getInt("Type")]);
		holder.addressTv.setText(jsonObject.getString("Address"));
//		if(selectId==position){
//			if(checkArr[position]=true){
//				checkArr[position]=false;
//			}else {
//				checkArr[position]=true;
//			}
//		}
		System.out.println("位置："+position+" 是否选中："+checkArr[position]);
//		int currentCount=jsonObject.getInt("CurrentCount");
		holder.typeTv.setText(typeArr[jsonObject.getInt("Type")]);
		if(isDelete){
			holder.checkIgv.setVisibility(View.VISIBLE);
			if(checkArr[position]){
				holder.checkIgv.setImageResource(R.drawable.radiobutton_on_background);
//			holder.typeTv.setText("  选中 类别："+typeArr[jsonObject.getInt("Type")]);
			}else {
				holder.checkIgv.setImageResource(R.drawable.radiobutton_off_background);
//			holder.typeTv.setText("没选中 类别："+typeArr[jsonObject.getInt("Type")]);
			}
		}else {
			holder.checkIgv.setVisibility(View.GONE);
		}

		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.schedule_record_time_tv)
		TextView timeTv;
		@ViewInject(R.id.schedule_record_number_tv)
		TextView numberTv;
		@ViewInject(R.id.schedule_record_type_tv)
		TextView typeTv;
		@ViewInject(R.id.schedule_record_address_tv)
		TextView addressTv;
		@ViewInject(R.id.schedule_record_check_igv)
		ImageView checkIgv;
	}
	
}

package com.jxj.jdoctorassistant.adapter.doctor.userlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;

import net.sf.json.JSONArray;

public class RemarkRecordAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;

	public RemarkRecordAdapter(Context context){
		this.context=context;
	}
	
	@Override
	public int getCount() {
		if(array!=null) {
			return array.size();
		}else {
			return 2;
		}
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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_remark_record, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

//		holder.igv.setText(user.getCustomerId()+"");
//		holder.titleTv.setText(" ");
//		holder.contentTv.setText(" ");
		return convertView;
	}
	
	class ViewHolder{
//		@ViewInject(R.id.e)
//		TextView timeTv;
//		@ViewInject(R.id.patient_function_lv_tv)
//		TextView tv;
	}
	
}

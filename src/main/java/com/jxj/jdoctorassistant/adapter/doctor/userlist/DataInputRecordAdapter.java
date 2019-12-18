package com.jxj.jdoctorassistant.adapter.doctor.userlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.doctor.userlist.DataInputRecordInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataInputRecordAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;
	private String[] dataTypes=DataInputRecordInfoActivity.DATATYPE;
	private String[] types= DataInputRecordInfoActivity.TYPE;
	private String[] units=DataInputRecordInfoActivity.UNIT;

	public DataInputRecordAdapter(Context context){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_data_input_record, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		JSONObject jsonObject=array.getJSONObject(position);
		StringBuffer stringBuffer=new StringBuffer();
		for(int i=0;i<types.length;i++){
			if(jsonObject.has(types[i])){
				stringBuffer.append(dataTypes[i]+":"+jsonObject.getString(types[i])+units[i]+"   ");
			}
		}
		holder.infoTv.setText(stringBuffer.toString());


		holder.dateTv.setText(jsonObject.getString("TestDate").substring(5));
		String images=jsonObject.getString("Images");
		if(images!=null&&!images.equals("null")){
			JSONArray array=JSONArray.fromObject(images);
			holder.picNumTv.setText(array.size()+"张图片");
		}else {
			holder.picNumTv.setText("没有图片");
		}
		holder.sourceTv.setText(context.getResources().getStringArray(R.array.arr_data_source_record)[jsonObject.getInt("Type")]);


		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.data_input_info_tv)
		TextView infoTv;
		@ViewInject(R.id.data_source_tv)
		TextView sourceTv;
		@ViewInject(R.id.data_pic_num_tv)
		TextView picNumTv;
		@ViewInject(R.id.date_tv)
		TextView dateTv;
	}
	
}

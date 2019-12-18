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
import net.sf.json.JSONObject;

public class SuggestionRecordAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;

	public SuggestionRecordAdapter(Context context){
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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_suggestion_record, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		JSONObject object=array.getJSONObject(position);

		holder.contentTv.setText(object.getString("Suggestion"));
		String images=object.getString("Images");
		if(images!=null&&!images.equals("null")){
			JSONArray array=JSONArray.fromObject(images);
			holder.picTv.setText(array.size()+"张图片");
		}else {
			holder.picTv.setText("没有图片");
		}
		holder.timeTv.setText(object.getString("SubmissionTime"));

		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.suggestion_record_content_tv)
		TextView contentTv;
		@ViewInject(R.id.suggestion_record_pic_tv)
		TextView picTv;
		@ViewInject(R.id.suggestion_record_time_tv)
		TextView timeTv;
	}
	
}

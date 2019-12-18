package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AddNoticeUserGridAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;

	public interface Delegete{
		void delete(int position);
	}

	private Delegete delegete;

	public AddNoticeUserGridAdapter(Context context){
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

	public void setDelegete(Delegete delegete) {
		this.delegete = delegete;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.gv_item_add_notice_user, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.nameTv.setText("张大中");
		holder.deleteIgv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				delegete.delete(position);
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.name_tv)
		TextView nameTv;
		@ViewInject(R.id.delete_igv)
		ImageView deleteIgv;
	}
	
}

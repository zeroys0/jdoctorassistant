package com.jxj.jdoctorassistant.adapter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class DiseaseDetailLvAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private JSONArray array;
	
	class ViewHolder{
		@ViewInject(R.id.disease_detail_name_tv)
		TextView nameTv;
		@ViewInject(R.id.disease_detail_symptom)
		TextView symptomTv;
	}
	
	
	public DiseaseDetailLvAdapter(Context context) {
		this.context = context;
		inflater=LayoutInflater.from(context);
	}
	
	public void setArray(JSONArray array) {
		this.array = array;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(array!=null){
			return array.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int postion) {
		// TODO Auto-generated method stub
		return postion;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
	    if(convertView==null){
	    	holder=new ViewHolder();
	    	convertView=inflater.inflate(R.layout.lv_item_disease_detail, null);
	    	ViewUtils.inject(holder, convertView);
	    	convertView.setTag(holder);
	    }else{
	    	holder=(ViewHolder) convertView.getTag();
	    }
	    JSONObject object=array.getJSONObject(postion);
	    holder.nameTv.setText(object.getString("name"));
		holder.symptomTv.setText(object.getString("symptom"));
		return convertView;
	}

}

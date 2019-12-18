package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HospitalAddressListAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;

	public HospitalAddressListAdapter(Context context){
		this.context=context;
	}
	public interface AdapterDelegete{
		public void edit(int position,int addressId);
		public void delete(int addressId);
		public void setDefalut(int addressId);
	}
	public AdapterDelegete delegete;

	public void setDelegete(AdapterDelegete delegete) {
		this.delegete = delegete;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_hospital_address, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		JSONObject object=array.getJSONObject(position);
		holder.nameTv.setText(object.getString("Hospital"));
		holder.addressTv.setText(object.getString("Address"));
		final boolean isDefault=object.getBoolean("IsDefault");
		if(isDefault){
			holder.defaultIgv.setImageDrawable(context.getResources().getDrawable(R.drawable.rb_checked));
		}else {
			holder.defaultIgv.setImageDrawable(context.getResources().getDrawable(R.drawable.rb_unchecked));
		}
		final int addressId=object.getInt("Id");

		holder.editTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				delegete.edit(position,addressId);
			}
		});
		holder.deleteTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				delegete.delete(addressId);
			}
		});
		holder.defaultIgv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isDefault){
					delegete.setDefalut(addressId);
				}
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.hospital_name_tv)
		TextView nameTv;
		@ViewInject(R.id.hospital_address_tv)
		TextView addressTv;
		@ViewInject(R.id.hospital_lv_edit_tv)
		TextView editTv;
		@ViewInject(R.id.hospital_lv_delete_tv)
		TextView deleteTv;
		@ViewInject(R.id.default_igv)
		ImageView defaultIgv;
	}

	
}

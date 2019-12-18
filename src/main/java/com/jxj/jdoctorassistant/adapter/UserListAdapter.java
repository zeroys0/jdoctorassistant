package com.jxj.jdoctorassistant.adapter;

import java.util.List;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.model.User;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserListAdapter extends BaseAdapter {
	
	private Context context;
	private int resourceId;
	private List<User> userList;

	public UserListAdapter(Context context,int resourceId,List<User> userList){
		this.context=context;
		this.userList=userList;
		this.resourceId=resourceId;
	}
	
	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(resourceId, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		User user=userList.get(position);
		holder.userIdTv.setText(user.getCustomerId()+"");
		holder.usernameTv.setText(user.getcName());
		holder.userJwotchIdTv.setText(user.getcPhoneNumber());
		holder.userPsTv.setText(user.getPs()>0?String.valueOf(user.getPs()):"--");
		holder.userPdTv.setText(user.getPd()>0?String.valueOf(user.getPd()):"--");
		holder.userHrTv.setText(user.getHr()>0?String.valueOf(user.getHr()):"--");
		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.user_id_tv)
		TextView userIdTv;
		@ViewInject(R.id.user_name_tv)
		TextView usernameTv;
		@ViewInject(R.id.user_jwotch_id_tv)
		TextView userJwotchIdTv;
		@ViewInject(R.id.user_ps_tv)
		TextView userPsTv;
		@ViewInject(R.id.user_pd_tv)
		TextView userPdTv;
		@ViewInject(R.id.user_hr_tv)
		TextView userHrTv;
	}
	
}

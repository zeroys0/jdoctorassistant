package com.jxj.jdoctorassistant.adapter.doctor;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.bean.MessageBean;
import com.jxj.jdoctorassistant.bean.Urls;
import com.jxj.jdoctorassistant.main.doctor.adapter.CommentAdapter;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnItemClickListener;

import java.util.List;

public class DoctorMessageListAdapter extends RecyclerView.Adapter<DoctorMessageListAdapter.ViewHolder> {
Context context;
List<MessageBean> list;
OnItemClickListener onItemClickListener;
	public DoctorMessageListAdapter(Context context,List<MessageBean> list,OnItemClickListener onItemClickListener){
		this.context = context;
		this.list = list;
		this.onItemClickListener = onItemClickListener;
	}

	@NonNull
	@Override
	public DoctorMessageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lv_item_doctor_message_list,viewGroup,false);
		DoctorMessageListAdapter.ViewHolder viewHolder = new DoctorMessageListAdapter.ViewHolder(view);
		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onItemClickListener.onItemClick(v);
			}
		});
		return  viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull DoctorMessageListAdapter.ViewHolder viewHolder, int i) {
		String url = Urls.IMAGEURL+list.get(i).getPhoto();
		Glide.with(context).load(url).into(viewHolder.doctor_message_lv_igv);
		viewHolder.doctor_message_lv_title_tv.setText(list.get(i).getCname());
		viewHolder.doctor_message_lv_content_tv.setText(list.get(i).getSuggestion());
	}

	@Override
	public int getItemCount() {
		return list == null?0:list.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		ImageView doctor_message_lv_igv;
		TextView doctor_message_lv_title_tv,doctor_message_lv_content_tv;
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			doctor_message_lv_igv = itemView.findViewById(R.id.doctor_message_lv_igv);
			doctor_message_lv_title_tv = itemView.findViewById(R.id.doctor_message_lv_title_tv);
			doctor_message_lv_content_tv = itemView.findViewById(R.id.doctor_message_lv_content_tv);
		}
	}
}

package com.jxj.jdoctorassistant.main.doctor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.bean.SignListBean;
import com.jxj.jdoctorassistant.bean.Urls;

import java.util.ConcurrentModificationException;
import java.util.List;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.ViewHolder> {
    private Context context;
    private List<SignListBean> list;
    private OnSignClickListener onSignClickListener;


    public SignAdapter(Context context, List<SignListBean> list, OnSignClickListener onSignClickListener){
        this.context = context;
        this.list = list;
        this.onSignClickListener = onSignClickListener;
    }
    public void updateData(List<SignListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public SignAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sign_list_item, parent, false); // 实例化viewholder
        SignAdapter.ViewHolder viewHolder = new SignAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SignAdapter.ViewHolder viewHolder, final int i) {
        switch (list.get(i).getState()) {
            case 0x00:      //请求签约
                viewHolder.apply_sign_agree_btn.setVisibility(View.VISIBLE);
                viewHolder.apply_sign_refuse_btn.setVisibility(View.VISIBLE);
                break;
            case 0x01:      //已取消
                viewHolder.tv_state.setText("已取消");
                break;
            case 0x02:      //已同意
                viewHolder.apply_sign_agree_btn.setVisibility(View.GONE);
                viewHolder.apply_sign_refuse_btn.setVisibility(View.GONE);
                viewHolder.tv_state.setText("已同意");
                break;
            case 0x03:      //已拒绝
                viewHolder.apply_sign_agree_btn.setVisibility(View.GONE);
                viewHolder.apply_sign_refuse_btn.setVisibility(View.GONE);
                viewHolder.tv_state.setText("已拒绝");
                break;
                default:
                    break;
        }
        viewHolder.apply_sign_agree_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignClickListener.onConfirmClick(i,list.get(i).getId());
            }
        });
        viewHolder.apply_sign_refuse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignClickListener.onCancelClick(i,list.get(i).getId());
            }
        });
        Glide.with(context).load(Urls.IMAGEURL+list.get(i).getPhoto()).into(viewHolder.img_head);
        viewHolder.tv_name.setText(list.get(i).getCname());
        viewHolder.tv_content.setText(list.get(i).getGender()+"|"+list.get(i).getAge()+"|"+list.get(i).getInsurance());
        viewHolder.tv_time.setText(list.get(i).getLogDate());

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button apply_sign_refuse_btn,apply_sign_agree_btn;
        ImageView img_head;
        TextView tv_name,tv_content,tv_state,tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            apply_sign_refuse_btn = itemView.findViewById(R.id.apply_sign_refuse_btn);
            apply_sign_agree_btn = itemView.findViewById(R.id.apply_sign_agree_btn);
            img_head = itemView.findViewById(R.id.img_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }


}

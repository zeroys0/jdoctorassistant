package com.jxj.jdoctorassistant.main.doctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.bean.ToProcessedBean;
import com.jxj.jdoctorassistant.bean.Urls;

import java.util.List;

import butterknife.OnTextChanged;

public class ToProcessedAdapter extends RecyclerView.Adapter<ToProcessedAdapter.ViewHolder> {
    private List<ToProcessedBean> list;
    Context context;
    OnItemClickListener onItemClickListener;

    public ToProcessedAdapter(List<ToProcessedBean> list, Context context, OnItemClickListener onItemClickListener) {
        this.list  = list;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ToProcessedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_processed_item, parent, false); // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ToProcessedAdapter.ViewHolder holder, int i) {
        holder.tv_name.setText(list.get(i).getCname());
        holder.tv_time.setText(list.get(i).getBeginDatetime()+list.get(i).getEndDatetime()+" "+getTpye(list.get(i).getType()));
        Glide.with(context).load(Urls.IMAGEURL+list.get(i).getPhoto()).into(holder.img_head);
        holder.tv_info.setText(list.get(i).getGender()+"|"+list.get(i).getAge()+"|"+list.get(i).getInsurance());
        holder.tv_content.setText(list.get(i).getSymptoms());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView tv_time,tv_name,tv_info,tv_content;
    ImageView img_head;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_head = itemView.findViewById(R.id.img_head);
            tv_info = itemView.findViewById(R.id.tv_info);
            tv_content = itemView.findViewById(R.id.tv_content);
        }

    }

    public String getTpye(int type){
        String s = "";
        switch (type) {
            case 0x00:
                s = "图文咨询";
                break;
            case 0x01:
                s = "视话咨询";
                break;
            case 0x02:
                s = "门诊就医";
                break;
            case 0x03:
                s= "上门看诊";
                break;
            default:
                break;
        }
        return s;
    }
}

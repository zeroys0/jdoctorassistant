package com.jxj.jdoctorassistant.main.doctor.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.bean.CommentBean;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<CommentBean> list;
    private Context context;

    public CommentAdapter(List<CommentBean> list,Context context){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_list_item,viewGroup,false);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder viewHolder, int i) {
        viewHolder.fragment_doctor_personal_ratingbar.setRating(list.get(i).getScore());
        viewHolder.tv_comment.setText(list.get(i).getEvaluation());
        viewHolder.tv_name.setText(list.get(i).getCName()+getType(list.get(i).getType()));
        viewHolder.tv_time.setText(list.get(i).getAddDate());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class
    ViewHolder extends RecyclerView.ViewHolder {
        RatingBar fragment_doctor_personal_ratingbar;
        TextView tv_comment,tv_name,tv_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fragment_doctor_personal_ratingbar = itemView.findViewById(R.id.fragment_doctor_personal_ratingbar);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

    public String getType(int type){
        String s  = "";
        switch (type) {
            case 0x00:
                s = "图文咨询";
                break;
            case 0x01:
                s= "视话咨询";
                break;
            case 0x02:
                s = "门诊就医";
                break;
            case 0x03:
                s= "上门看诊";
                break;
        }
        return s;
    }
}

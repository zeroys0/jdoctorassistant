package com.jxj.jdoctorassistant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnItemClickListener;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnPhoneClickListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PhoneListAdapter  extends RecyclerView.Adapter<PhoneListAdapter.ViewHolder> {

    Set<String> set;
    Context context;
    OnPhoneClickListener onPhoneClickListener;

    public PhoneListAdapter(Context context,Set<String> set,OnPhoneClickListener onPhoneClickListener) {
        this.context  =context;
        this.set = set;
        this.onPhoneClickListener  = onPhoneClickListener;
    }
    @NonNull
    @Override
    public PhoneListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.phone_list_item,viewGroup,false);
        PhoneListAdapter.ViewHolder viewHolder = new PhoneListAdapter.ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneListAdapter.ViewHolder viewHolder, final int i) {
        final List<String> list = getList(set);
        viewHolder.tv_phone.setText(list.get(i));
        viewHolder.ll_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhoneClickListener.OnPhoneClick(v, list.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return set.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_phone;
        LinearLayout ll_phone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            ll_phone = itemView.findViewById(R.id.ll_phone);
        }
    }

    public List<String> getList(Set<String> set) {
        List<String> list = new ArrayList<>();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return  list;
    }
}

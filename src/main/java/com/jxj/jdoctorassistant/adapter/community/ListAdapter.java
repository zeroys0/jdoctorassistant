package com.jxj.jdoctorassistant.adapter.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.community.view.SwipeListLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/30.
 */

public class ListAdapter extends BaseAdapter {

    List<String> list =new ArrayList();
    Context mContext;

    public ListAdapter(List list) {
        this.list =list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int arg0, View view, ViewGroup arg2) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.yy_slip_listview_item_layout, null);
        }
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name.setText(list.get(arg0));
        final SwipeListLayout sll_main = (SwipeListLayout) view
                .findViewById(R.id.sll_main);
        TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        //sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener( sll_main));
        tv_top.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sll_main.setStatus(SwipeListLayout.Status.Close, true);
                String str = list.get(arg0);
                list.remove(arg0);
                list.add(0, str);
                notifyDataSetChanged();
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sll_main.setStatus(SwipeListLayout.Status.Close, true);
                list.remove(arg0);
                notifyDataSetChanged();
            }
        });
        return view;
    }




}

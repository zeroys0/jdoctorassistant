package com.jxj.jdoctorassistant.adapter.community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.community.activity.personal.AddApplicantActivity;
import com.jxj.jdoctorassistant.main.community.fragment.ApplicantFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


import net.sf.json.JSONArray;

/**
 *      县区社区 的下拉列表的  适配器
 *
 *
 *
 *   与第二个 一致  只是 不用getView 方法 不用 展示 4个 坐标
 *
 *
 */

public class ChooseCommunityListAdapterYY extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private JSONArray array;

    private int type;

    public ChooseCommunityListAdapterYY(Context context){
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getCount() {
        if(array!=null){
            if(type==ApplicantFragment.TYPE_1) {
                return array.size() + 1;
            }else if(type==AddApplicantActivity.TYPE_02){
                return array.size();
            }else {
                return 0;
            }
        }else {
            return 0;
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_add_new_applicant_yy, null);
            ViewUtils.inject(holder,convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        Log.v("qqq","适配器 获取社区列表"+array.toString() );

//        if(array.size()==0){
//            holder.title.setText("没有数据");
//        }else {
        if(type== ApplicantFragment.TYPE_1) {
            if (position == 0) {
                holder.title.setText(context.getResources().getString(R.string.all));
            } else {
                holder.title.setText(array.getJSONObject(position - 1).getString("CommunityName"));
            }
        }else if(type== AddApplicantActivity.TYPE_02){
            holder.title.setText(array.getJSONObject(position).getString("CommunityName"));
        }
//        }


        //holder.title.setText(array.getJSONObject(position-1).getString("CommunityName"));


        return convertView;

    }


    public  class ViewHolder{
        @ViewInject(R.id.list_item_classify_content_tv)
        public TextView title;

    }



}
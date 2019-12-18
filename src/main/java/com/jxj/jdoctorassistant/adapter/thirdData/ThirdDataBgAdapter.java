package com.jxj.jdoctorassistant.adapter.thirdData;

import com.jxj.jdoctorassistant.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ThirdDataBgAdapter extends BaseAdapter {

    class ViewHolder{
        @Bind(R.id.td_bg_test_time_tv)
        TextView testTimeTv;
        @Bind(R.id.td_bg_tv)
        TextView bgTv;
    }

    private Context context;
    private LayoutInflater inflater;

    private JSONArray array;

    public ThirdDataBgAdapter(Context context) {
        this.context = context;
        inflater=LayoutInflater.from(context);

    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    @Override
    public int getCount() {
        if(array!=null){
            return array.size();
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
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.lv_item_third_data_bg,null);
            viewHolder=new ViewHolder();
            ButterKnife.bind(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject=array.getJSONObject(position);
        viewHolder.testTimeTv.setText(jsonObject.getString("TestTime"));
        viewHolder.bgTv.setText(jsonObject.getInt("Glucose")+"mg/dl");

        return convertView;
    }
}

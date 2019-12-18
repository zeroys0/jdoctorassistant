package com.jxj.jdoctorassistant.adapter.thirdData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.jxj.jdoctorassistant.R;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ThirdDataBtAdapter extends BaseAdapter {

    class ViewHolder{
        @Bind(R.id.td_bt_test_time_tv)
        TextView testTimeTv;
        @Bind(R.id.td_bt_ft_tv)
        TextView ftTv;
        @Bind(R.id.td_bt_ct_tv)
        TextView ctTv;
    }

    private Context context;
    private LayoutInflater inflater;

    private JSONArray array;

    public ThirdDataBtAdapter(Context context) {
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
            convertView=inflater.inflate(R.layout.lv_item_third_data_bt,null);
            viewHolder=new ViewHolder();
            ButterKnife.bind(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject=array.getJSONObject(position);
        viewHolder.testTimeTv.setText(jsonObject.getString("TestTime"));
        viewHolder.ftTv.setText(jsonObject.getDouble("FT")+"℉");
        viewHolder.ctTv.setText(jsonObject.getDouble("CT")+"℃");

        return convertView;
    }
}

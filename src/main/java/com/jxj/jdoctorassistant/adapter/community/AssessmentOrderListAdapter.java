package com.jxj.jdoctorassistant.adapter.community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class AssessmentOrderListAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater layoutinflater;
    private JSONArray jsonarray;


    public AssessmentOrderListAdapter(Context context) {
        this.context = context;
        this.layoutinflater = LayoutInflater.from(context);
    }

    public void setJsonarray(JSONArray jsonarray) {
        this.jsonarray = jsonarray;
    }

    @Override
    public int getCount() {
        if (jsonarray != null) {
            return jsonarray.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int id) {

        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.yy_order_lv_item, parent, false);
            viewHolder = new ViewHolder();

            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObj = jsonarray.getJSONObject(position);

        Log.v("mmm", jsonObj.toString());

        viewHolder.usernameTv.setText(jsonObj.getString("CName"));
        String address =jsonObj.getString("Address");
        if (address.equals("")){
            viewHolder.mItemOrdeAddressTv.setText("暂无");

        }else {
            viewHolder.mItemOrdeAddressTv.setText(address);

        }

        String cardId =jsonObj.getString("CardId");
        if (cardId.equals("")){
            viewHolder.mItemOrderIdCardNumTv.setText("暂无");

        }else {
            viewHolder.mItemOrderIdCardNumTv.setText(cardId);
        }


        String phone =jsonObj.getString("MobilePhone");
        if (phone.equals("")){
            viewHolder.mItemOrderPhoneNumTv.setText("暂无");

        }else {
            viewHolder.mItemOrderPhoneNumTv.setText(phone);
        }




        viewHolder.mItemOrderAddTime.setText(jsonObj.getString("CrtDate"));


        String AppDate = jsonObj.getString("AppDate");

        String[] dateTiem = AppDate.split(" ");
        String[] time = dateTiem[1].split(":");


        viewHolder.mItemOrderTimeTv.setText(time[0]+":"+time[1]);
        viewHolder.mItemOrderDateTv.setText(dateTiem[0]);


        return convertView;

    }


    static class ViewHolder {

        @ViewInject(R.id.item_order_name_tv)
        public TextView usernameTv;
        @ViewInject(R.id.item_order_time_tv)
        TextView mItemOrderTimeTv;
        @ViewInject(R.id.item_order_date_tv)
        TextView mItemOrderDateTv;
        @ViewInject(R.id.item_order_phone_num_tv)
        TextView mItemOrderPhoneNumTv;
        @ViewInject(R.id.item_order_id_card_num_tv)
        TextView mItemOrderIdCardNumTv;
        @ViewInject(R.id.item_orde_address_tv)
        TextView mItemOrdeAddressTv;
        @ViewInject(R.id.item_order_add_time)
        TextView mItemOrderAddTime;
    }


}

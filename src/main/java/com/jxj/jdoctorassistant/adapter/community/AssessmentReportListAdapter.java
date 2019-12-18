package com.jxj.jdoctorassistant.adapter.community;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*第二个Fragment  的适配器  */
public class AssessmentReportListAdapter extends BaseAdapter {

    class ViewHolder {
        @ViewInject(R.id.order_username_tv)
        public TextView usernameTv;
        @ViewInject(R.id.order__assess_number_tv)
        public TextView assessNumberTv;
        @ViewInject(R.id.order__item1_tv)
        public TextView orderItemTv;
        @ViewInject(R.id.order_item1_iv)
        public ImageView orderItemIv;
        //四个RadioButton
        @ViewInject(R.id.order_item1_zhidaoyuan_queren_rb)
        public RadioButton zhidaoyuanQuerenRb;
        @ViewInject(R.id.order_item1_jiedao_rb)
        public RadioButton jiedaoRb;
        @ViewInject(R.id.order_item1_zhidaoyuan_choucha_rb)
        public RadioButton zhidaoyuanChouchaRb;
        @ViewInject(R.id.order_item1_xiangqu_rb)
        public RadioButton xiangquRb;


        @ViewInject(R.id.order_item_num)
        public TextView orderItemNum;
        @ViewInject(R.id.order_item_time)
        public TextView orderItemTime;


    }

    private Context context;
    private LayoutInflater layoutinflater;
    private JSONArray jsonarray;

    public AssessmentReportListAdapter(Context context) {
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
            convertView = layoutinflater.inflate(R.layout.lv_item_assessment_record_yy, parent, false);
            viewHolder = new ViewHolder();


            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObj = jsonarray.getJSONObject(position);//这是得到的这条数据  下边开始填充


        JSONObject user = jsonObj.getJSONObject("Assess");


        Log.v("mmm", user.toString());
        //需要填充8个数据

        viewHolder.usernameTv.setText(user.getString("CName"));//1
        viewHolder.assessNumberTv.setText("第" + user.getString("AssessNumber") + "次评估");//2


		/*TODO  这里是 首次 复检 持续 */


        if (user.getInt("AssessType") == 0) {//0:初次1:复检；2:持续
            viewHolder.orderItemTv.setText("初次");
        } else if (user.getInt("AssessType") == 1) {
            viewHolder.orderItemTv.setText("复检");
        } else if (user.getInt("AssessType") == 2) {
            viewHolder.orderItemTv.setText("持续");
        } else {
            viewHolder.orderItemTv.setText("  ");
        }


        viewHolder.orderItemNum.setText(user.getInt("AssessID") + "");
        viewHolder.orderItemTime.setText(user.getString("LastAssessDateTime"));


        /*
        * Item1 1234  生活自理 认知 情绪 视觉

        Item2 12    项目描述 服务建议

        Uid1  评估员编号
        Status1   评估员确认

        * */

        JSONObject report = jsonObj.getJSONObject("Report");
        Log.v("mmmr", report.toString());

        //Conclude
        int Conclude = report.getInt("Conclude");



        if (Conclude == 1) {//3，程度：根据不同程度 切换图片
            viewHolder.orderItemIv.setImageResource(R.drawable.level_zhengchang);
        } else if (Conclude == 2) {
            viewHolder.orderItemIv.setImageResource(R.drawable.level_qindu);
        } else if (Conclude == 3) {
            viewHolder.orderItemIv.setImageResource(R.drawable.level_zhongdu);
        } else if (Conclude == 4) {
            viewHolder.orderItemIv.setImageResource(R.drawable.level_zhongdu_deep);
        } else {
            viewHolder.orderItemIv.setImageResource(R.color.white);
        }


        //1
        if (report.getInt("Status1") == 0) {
            viewHolder.zhidaoyuanQuerenRb.setChecked(false);
            viewHolder.zhidaoyuanChouchaRb.setTextColor(ContextCompat.getColor(context, R.color.color3));
        } else if (report.getInt("Status1") == 1)  {
            viewHolder.zhidaoyuanQuerenRb.setChecked(true);
            viewHolder.zhidaoyuanChouchaRb.setTextColor(ContextCompat.getColor(context, R.color.color5));
        }

        //2
        if (report.getInt("Status2") == 0) {
            viewHolder.jiedaoRb.setChecked(false);
            viewHolder.jiedaoRb.setTextColor(ContextCompat.getColor(context, R.color.color3));
        } else if (report.getInt("Status2") == 1) {
            viewHolder.jiedaoRb.setChecked(true);
            viewHolder.jiedaoRb.setTextColor(ContextCompat.getColor(context, R.color.color5));
        }

        //3
        if (report.getInt("Status3") == 0) {
            viewHolder.zhidaoyuanChouchaRb.setChecked(false);
            viewHolder.zhidaoyuanChouchaRb.setTextColor(ContextCompat.getColor(context, R.color.color3));
        } else if (report.getInt("Status3") == 1) {
            viewHolder.zhidaoyuanChouchaRb.setChecked(true);
            viewHolder.zhidaoyuanChouchaRb.setTextColor(ContextCompat.getColor(context, R.color.color5));
        }

        //4
        if (report.getInt("Status4") == 0) {
            viewHolder.xiangquRb.setChecked(false);
            viewHolder.xiangquRb.setTextColor(ContextCompat.getColor(context, R.color.color3));
        } else if (report.getInt("Status4") == 1) {
            viewHolder.xiangquRb.setChecked(true);
            viewHolder.xiangquRb.setTextColor(ContextCompat.getColor(context, R.color.color5));
        }


        return convertView;

    }


}

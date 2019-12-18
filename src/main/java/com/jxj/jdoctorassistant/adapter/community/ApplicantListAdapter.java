package com.jxj.jdoctorassistant.adapter.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/*第一个Fragment  的适配器 */
public class ApplicantListAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater layoutinflater;
    private JSONArray jsonarray;

    public ApplicantListAdapter(Context context) {
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
            convertView = layoutinflater.inflate(R.layout.lv_item_applicant_yy, parent, false);
            viewHolder = new ViewHolder(convertView);
            ViewUtils.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /* 需要填充 9个数据  */
        JSONObject jsonObj = jsonarray.getJSONObject(position);
        //1.姓名
        viewHolder.mApplicantUsernameTv.setText(jsonObj.getString("Name"));
        //2，性别
        if (jsonObj.getInt("Gender") == 1) {
            viewHolder.mApplicantGenderTv.setText("男");

        } else if (jsonObj.getInt("Gender") == 2) {
            viewHolder.mApplicantGenderTv.setText("女");
        } else {
            viewHolder.mApplicantGenderTv.setText("暂无");

        }
        //3，社保类型
        viewHolder.mApplicantSocialSecurityTv.setText(jsonObj.getString("SocialSecurity"));
        //4，身份证号
        viewHolder.mApplicantCardidTv.setText(jsonObj.getString("CardId"));
        //5，评估次数
        viewHolder.mApplicantAssessNumberTv.setText("第"+jsonObj.getString("AssessNumber") + "次评估");

        //6，程度：根据不同程度 切换图片
        if (jsonObj.getInt("Item1") == 1) {
            viewHolder.mApplicantItem1Tv.setText("正常");
            viewHolder.mApplicantItem1Iv.setImageResource(R.drawable.level_zhengchang);

        } else if (jsonObj.getInt("Item1") == 2) {

            viewHolder.mApplicantItem1Tv.setText("轻度");
            viewHolder.mApplicantItem1Iv.setImageResource(R.drawable.level_qindu);
        } else if (jsonObj.getInt("Item1") == 3) {

            viewHolder.mApplicantItem1Tv.setText("中度");
            viewHolder.mApplicantItem1Iv.setImageResource(R.drawable.level_zhongdu);
        } else if (jsonObj.getInt("Item1") == 4) {

            viewHolder.mApplicantItem1Tv.setText("重度");
            viewHolder.mApplicantItem1Iv.setImageResource(R.drawable.level_zhongdu_deep);
        } else {
            viewHolder.mApplicantItem1Tv.setText("服务器没有返回结果");
            viewHolder.mApplicantItem1Iv.setImageResource(R.color.white);
        }

        //7，社区地址  现在是 现住地址
        JSONObject ResudentialAddress =jsonObj.getJSONObject("ResudentialAddress");
        String address =ResudentialAddress.getString("PAddressStr");
        if (address.equals("/////")){
            viewHolder.mApplicantNativeTv.setText("暂无");

        }else {

            String addressTemp =ResudentialAddress.getString("PAddressStr");
            String add=addressTemp.replace("/"," ");
            viewHolder.mApplicantNativeTv.setText(add);

        }



        //8，评估人 姓名
        viewHolder.mPingguName.setText("评估人 姓名");
        //9，上次评估时间
        viewHolder.mLastAssessDateTime.setText(jsonObj.getString("LastAssessDateTime"));


        return convertView;

    }


    class ViewHolder {
        @Bind(R.id.applicant_username_tv)
        TextView mApplicantUsernameTv;//姓名
        @Bind(R.id.applicant_assess_number_tv)
        TextView mApplicantAssessNumberTv;//评估次数
        @Bind(R.id.applicant_item1_tv)
        TextView mApplicantItem1Tv;//程度
        @Bind(R.id.applicant_item1_iv)
        ImageView mApplicantItem1Iv;//程度图片
        @Bind(R.id.applicant_gender_tv)
        TextView mApplicantGenderTv;//性别 1 男 2女
        @Bind(R.id.applicant_social_security_tv)
        TextView mApplicantSocialSecurityTv;//社保类型
        @Bind(R.id.applicant_cardid_tv)
        TextView mApplicantCardidTv;//身份证号
        @Bind(R.id.applicant_native_tv)
        TextView mApplicantNativeTv;//社区地址
        @Bind(R.id.pinggu_name)
        TextView mPingguName;//评估人姓名
        @Bind(R.id.last_assess_date_time)
        TextView mLastAssessDateTime;//上次评估时间

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}

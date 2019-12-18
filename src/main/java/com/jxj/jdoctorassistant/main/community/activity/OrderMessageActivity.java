package com.jxj.jdoctorassistant.main.community.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderMessageActivity extends Activity {

    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
//    @Bind(R.id.order_message_lv)
//    ListView mOrderMessageLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_message);
//        ButterKnife.bind(this);
//        mTitleTv.setText("预约消息提醒");


    }


    @OnClick(R.id.back_igv)
    public void onViewClicked() {
        finish();
    }



}

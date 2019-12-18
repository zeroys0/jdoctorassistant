package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.just.agentweb.AgentWeb;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.app.MyApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一体机数据对比
 */
public class ActivityContrast extends Activity {

    ImageView imBack;
    TextView tvTitle;
    Context mContext;
    LinearLayout ll;
    AgentWeb agentweb;
    private String customerId;
    private SharedPreferences sp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrast);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }


    @OnClick(R.id.im_back)
    public void onViewClicked() {
        finish();
    }

    void init() {
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        customerId=sp.getString(AppConstant.USER_customerId,null);
        imBack = findViewById(R.id.im_back);
        tvTitle = findViewById(R.id.tv_title);
        ll = findViewById(R.id.ll);
        String url = "http://122.225.60.118:6280/h5/hsdataCompare.html?key=" + MyApplication.key + "&customerId=" + customerId + "&datetime=" + getIntent().getStringExtra("time");
        setWeb(url);

    }

    @SuppressLint("AddJavascriptInterface")
    void setWeb(String url) {
        if (agentweb == null) {
            agentweb = AgentWeb.with(ActivityContrast.this)
                    .setAgentWebParent(ll, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(url);
        } else {
            ll.setVisibility(View.GONE);
            agentweb.getWebCreator().getWebView().loadUrl(url);

            ll.setVisibility(View.VISIBLE);
        }

    }

}

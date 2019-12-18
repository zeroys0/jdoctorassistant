package com.jxj.jdoctorassistant.main.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.util.Acache;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

public class ResultActivity extends Activity {
    @ViewInject(value = R.id.title_tv)
    private TextView titleTv;
    @OnClick({R.id.back_igv,R.id.tv_re_edit})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.tv_re_edit:
                startActivity(new Intent(context,PerfectInfoActivity.class));
                break;
        }
    }

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);
        ViewUtils.inject(this);
        context=this;
        JSONObject jb = Acache.get(context).getAsJSONObject("userdata");


    }
}

package com.jxj.jdoctorassistant.main.register;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Locale;

public class ProtocolActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.protocol_title)
    private TextView titleTv;
    @ViewInject(R.id.protocol_wv)
    private WebView protocolWv;

    @OnClick(R.id.back_igv)
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            default:

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_protocol);
        ViewUtils.inject(this);

        titleTv.setText(getResources().getString(R.string.register_protocol));

        String localUrl=null;
        String lan= Locale.getDefault().getLanguage();
        if(lan.equals("zh")){
            localUrl ="file:///android_asset/protocol.html";
        }else{
            localUrl = "file:///android_asset/protocol_en.html";
        }
        protocolWv.loadUrl(localUrl);
    }
}

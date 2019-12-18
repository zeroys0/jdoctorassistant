package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.jxj.jdoctorassistant.R;

public class WatchDataActivity extends Activity {
WebView webView;
    AgentWeb agentweb;
    Context mContext;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_data);
        mContext = this;
        init();
    }

    public void init(){
        ll = findViewById(R.id.ll);


        setWeb("http://122.225.60.118:6280/h5/healthDataList.html?key=58f1d615-ed3c-457a-98fe-320dcdf08b74&customerId=100000659&phoneType=1");
    }

    @SuppressLint("AddJavascriptInterface")
    void setWeb(String url) {
        if (agentweb == null) {
            agentweb = AgentWeb.with(WatchDataActivity.this)
                    .setAgentWebParent(ll, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(url);
            agentweb.getWebCreator().getWebView().addJavascriptInterface(new JsToJava(), "android"); // JsToJava是内部类，代码在后面。st
        } else {
            ll.setVisibility(View.GONE);
            agentweb.getWebCreator().getWebView().loadUrl(url);
            
            ll.setVisibility(View.VISIBLE);
        }

    }

    public class JsToJava {
        @JavascriptInterface
        //曲线图
        public void graph(String paramFromJS) {
            //Mytoast.show(mContext, "曲线图" + paramFromJS);
         //   startActivity(new Intent(mContext, CurveOneActivity.class).putExtra("time", paramFromJS));
        }

        @JavascriptInterface
        //对比
        public void compare(String paramFromJS) {
            // Mytoast.show(mContext, "对比" + paramFromJS);
            startActivity(new Intent(mContext, ActivityContrast.class).putExtra("time", paramFromJS));

        }
    }
}

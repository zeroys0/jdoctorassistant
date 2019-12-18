package com.jxj.jdoctorassistant.app;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;


/**
 * Created by Administrator on 2017/8/25.
 */

public class MyApplication extends Application {

public final static String key = "58f1d615-ed3c-457a-98fe-320dcdf08b74";
public static int Doctor_ID;
//    private Application applicationContext;
//    private Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
//        EaseUI.getInstance().init(this, null);
    }
}

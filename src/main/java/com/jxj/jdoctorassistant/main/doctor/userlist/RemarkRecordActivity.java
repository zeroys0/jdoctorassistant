package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.RemarkRecordAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

public class RemarkRecordActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.remark_record_title)
    private TextView titleTv;

    @ViewInject(R.id.remark_record_lv)
    private ListView recordLv;

    @OnClick({R.id.back_igv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            default:
                break;
        }
    }

    private Context context;
    private RemarkRecordAdapter adapter;
    private DoctorSHThread getRemarkThread;
    private int doctorId;
    private String customerId;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_remark_record);
        ViewUtils.inject(this);

        context=this;
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        customerId=sp.getString(AppConstant.USER_customerId,null);
        adapter=new RemarkRecordAdapter(context);
        recordLv.setAdapter(adapter);
        getRemark();
    }

    void getRemark(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getRemarkThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        UiUtil.showToast(context,jsonObject.getString("Data"));
                    }
                }
            }
        };
        getRemarkThread=new DoctorSHThread(ApiConstant.GETCUSTOMERREMARK,handler,context);
        getRemarkThread.setDoctorId(doctorId);
        getRemarkThread.setCustomerId(customerId);
        getRemarkThread.start();
    }

}

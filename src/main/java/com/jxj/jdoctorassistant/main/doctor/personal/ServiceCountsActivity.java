package com.jxj.jdoctorassistant.main.doctor.personal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.WorkSource;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.ServiceCountsAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ServiceCountsActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.service_counts_title)
    private TextView titleTv;

    @ViewInject(R.id.service_counts_lv)
    private ListView countsLv;

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
    private DoctorSHThread getServiceCountsThread;
    private ServiceCountsAdapter adapter;
    private SharedPreferences sp;
    private int doctorId;
    private JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_service_counts);
        ViewUtils.inject(this);

        context=this;

        titleTv.setText(getResources().getString(R.string.service_counts));

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);

        adapter=new ServiceCountsAdapter(context);
        countsLv.setAdapter(adapter);

        getServiceCounts();
    }

    void getServiceCounts(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                    String result=getServiceCountsThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("Data");
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                    adapter.setArray(array);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        getServiceCountsThread=new DoctorSHThread(ApiConstant.GETDOCTORSCHEDULECOUNTS,handler,context);
        getServiceCountsThread.setDoctorId(doctorId);
        getServiceCountsThread.start();
    }

}

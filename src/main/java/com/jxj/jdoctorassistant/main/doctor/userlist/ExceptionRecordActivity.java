package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.ExceptionRecordAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.TimeDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ExceptionRecordActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.exception_record_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_igv,parentId = R.id.exception_record_title)
    private ImageView rightIgv;

    @ViewInject(R.id.exception_record_lv)
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
    private ExceptionRecordAdapter adapter;
    private JSONArray array;
    private SharedPreferences sp;
    private int doctorId;
    private String customerId;
    private DoctorSHThread getRecordThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exception_record);
        ViewUtils.inject(this);

        context=this;

        titleTv.setText(getResources().getStringArray(R.array.patient_function_arr)[5]);

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        customerId=sp.getString(AppConstant.USER_customerId,null);

        adapter=new ExceptionRecordAdapter(context);
        recordLv.setAdapter(adapter);
        getRecord();

    }

    void getRecord(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getRecordThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("DataList");
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                    adapter.setArray(array);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        getRecordThread=new DoctorSHThread(ApiConstant.GETABNORMALRECORDSBYCUSTOMERID,handler,context);
        getRecordThread.setDoctorId(doctorId);
        getRecordThread.setCustomerId(customerId);
        getRecordThread.setPageIndex(0);
        getRecordThread.setPageSize(10);
        getRecordThread.start();
    }

}

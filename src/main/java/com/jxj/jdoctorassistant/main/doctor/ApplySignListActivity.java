package com.jxj.jdoctorassistant.main.doctor;

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
import com.jxj.jdoctorassistant.adapter.doctor.ApplySignAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ApplySignListActivity extends Activity implements ApplySignAdapter.ApplySignDelegete{
    @ViewInject(value = R.id.title_tv,parentId = R.id.apply_sign_title)
    private TextView titleTv;

    @ViewInject(R.id.apply_sign_lv)
    private ListView userLv;

    @OnClick({R.id.back_igv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
        }
    }

    private Context context;
    private ApplySignAdapter adapter;
    private DoctorSHThread getListThread,refuseSignThread,agreeSignThread;
    private SharedPreferences sp;
    private int doctorId;
    private JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_list);
        ViewUtils.inject(this);

        context=this;

        titleTv.setText(getResources().getString(R.string.apply_sign));

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);


        initView();

    }

    void initView(){
        adapter=new ApplySignAdapter(context);
        userLv.setAdapter(adapter);

        getList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setDelegete(this);
    }

    @Override
    public void refuse(int signId) {
        refuseSign(signId);
    }

    @Override
    public void agree(int signId) {
        agreeSign(signId);
    }

    void getList(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getListThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                             array=jsonObject.getJSONArray("DataList");
                        }else {
                            UiUtil.showToast(context, jsonObject.getString("message"));
                        }
                    }
                    adapter.setArray(array);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        getListThread=new DoctorSHThread(ApiConstant.GETSHSIGNRECORDLIST,handler,context);
        getListThread.setDoctorId(doctorId);
        getListThread.setPageIndex(0);
        getListThread.setPageSize(15);
        getListThread.start();
    }

    void refuseSign(int signId){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=refuseSignThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            getList();
                        }else {
                            UiUtil.showToast(context, jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        refuseSignThread=new DoctorSHThread(ApiConstant.SHSIGNREFUSE,handler,context);
        refuseSignThread.setDoctorId(doctorId);
        refuseSignThread.setSignId(signId);
        refuseSignThread.start();
    }

    void agreeSign(int signId){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=agreeSignThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            getList();
                        }else {
                            UiUtil.showToast(context, jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        agreeSignThread=new DoctorSHThread(ApiConstant.SHSIGNAGREE,handler,context);
        agreeSignThread.setDoctorId(doctorId);
        agreeSignThread.setSignId(signId);
        agreeSignThread.start();
    }

}

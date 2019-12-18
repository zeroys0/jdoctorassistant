package com.jxj.jdoctorassistant.main.doctor.personal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.ServiceListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ServiceListActivity extends Activity {
    @ViewInject(R.id.title_tv)
    private TextView titleTv;
    @ViewInject(R.id.right_btn_tv)
    private TextView rightTv;

    @ViewInject(R.id.service_list_words_etv)
    private EditText wordsEtv;
//    @ViewInject(R.id.search_igb)
//    private ImageButton searchIgb;
    @ViewInject(R.id.service_lv)
    private ListView serviceLv;

    @OnClick({R.id.back_igv,R.id.search_igb})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.search_igb:
                if(TextUtils.isEmpty(wordsEtv.getEditableText())){
                    UiUtil.showToast(context,context.getResources().getString(R.string.input_words));
                    return;
                }
                getService();
                break;
            default:
                break;
        }
    }

    private Context context;
    private ServiceListAdapter adapter;
    private SharedPreferences sp;
    private int doctorId;
    private DoctorSHThread getServiceThread;
    private JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_service_list);
        ViewUtils.inject(this);
        context=this;

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);

        adapter=new ServiceListAdapter(context);
        serviceLv.setAdapter(adapter);

        titleTv.setText(getResources().getString(R.string.service_record));
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText(getResources().getString(R.string.counts));
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ServiceCountsActivity.class));
            }
        });
        getService();

    }
    void getService(){
        array=null;
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                    String result=getServiceThread.getResult();
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
        getServiceThread=new DoctorSHThread(ApiConstant.GETSHSCHEDULELIST,handler,context);
        getServiceThread.setDoctorId(doctorId);
        getServiceThread.setCondition(wordsEtv.getEditableText().toString().trim());
        getServiceThread.setPageIndex(0);
        getServiceThread.setPageSize(20);
        getServiceThread.start();
    }

}

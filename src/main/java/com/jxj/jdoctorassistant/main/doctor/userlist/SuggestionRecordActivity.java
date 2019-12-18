package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.SuggestionRecordAdapter;
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

public class SuggestionRecordActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.suggestion_record_title)
    private TextView titleTv;

    @ViewInject(R.id.suggestion_record_lv)
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
    private SuggestionRecordAdapter adapter;
    private DoctorSHThread getSuggestionThread;
    private SharedPreferences sp;
    private int doctorId;
    private String customerId;
    private JSONArray array;

    public static final String SUGGESTIONINFO="suggestion_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_suggestion_record);
        ViewUtils.inject(this);

        context=this;
        titleTv.setText(getResources().getString(R.string.history_record));

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        customerId=sp.getString(AppConstant.USER_customerId,null);

        adapter=new SuggestionRecordAdapter(context);
        recordLv.setAdapter(adapter);
        recordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                JSONObject jsonObject=array.getJSONObject(position);
                String images=jsonObject.getString("Images");
                if(images!=null&&!images.equals("null")) {
                    Intent intent = new Intent(context, SuggestionInfoActivity.class);
                    intent.putExtra(SUGGESTIONINFO, jsonObject.toString());
                    startActivity(intent);
                }
            }
        });
        getSuggestion();

    }

    void getSuggestion(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                    String result=getSuggestionThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("DataList");
                            adapter.setArray(array);
                            adapter.notifyDataSetChanged();
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }


                    }
                }
            }
        };
        getSuggestionThread=new DoctorSHThread(ApiConstant.GETSHSUGGESTIONLIST,handler,context);
        getSuggestionThread.setDoctorId(doctorId);
        getSuggestionThread.setCustomerId(customerId);
        getSuggestionThread.setPageIndex(0);
        getSuggestionThread.setPageSize(10);
        getSuggestionThread.start();
    }

}

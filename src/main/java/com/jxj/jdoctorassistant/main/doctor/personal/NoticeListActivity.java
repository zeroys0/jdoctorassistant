package com.jxj.jdoctorassistant.main.doctor.personal;

import android.content.Context;
import android.content.Intent;
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
import com.jxj.jdoctorassistant.adapter.doctor.NoticeListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NoticeListActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.notice_num_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_tv,parentId = R.id.notice_num_title)
    private TextView addTv;

    @ViewInject(R.id.notice_top_title_tv)
    private TextView topTitleTv;
    @ViewInject(R.id.notice_top_content_tv)
    private TextView topContentTv;
    @ViewInject(R.id.notice_top_time_tv)
    private TextView topTimeTv;

    @ViewInject(R.id.notice_lv)
    private ListView noticeLv;

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
    private DoctorSHThread getNoticeThread;
    private NoticeListAdapter adapter;
    private SharedPreferences sp;
    private JSONArray array;
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notice_list);
        ViewUtils.inject(this);

        context=this;

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);

        titleTv.setText(getResources().getString(R.string.notice_record));
        addTv.setVisibility(View.VISIBLE);
        addTv.setText(getResources().getString(R.string.add_notice));
        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,AddNoticeActivity.class));
            }
        });
        adapter=new NoticeListAdapter(context);
        noticeLv.setAdapter(adapter);
    }

    void getNotice(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getNoticeThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("DataList");
//                            for(int i=0;i<array.size();i++){
//                                JSONObject object=array.getJSONObject(i);
//                                boolean isTop=object.getBoolean("TopRelease");
//
//                            }
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                    adapter.setArray(array);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        getNoticeThread=new DoctorSHThread(ApiConstant.GETSHNOTICELIST,handler,context);
        getNoticeThread.setDoctorId(doctorId);
        getNoticeThread.setPageIndex(0);
        getNoticeThread.setPageSize(15);
        getNoticeThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotice();
    }
}

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
import com.jxj.jdoctorassistant.adapter.doctor.userlist.DataInputRecordAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataInputRecordActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.data_record_title)
    private TextView titleTv;
    @ViewInject(R.id.data_input_record_lv)
    private ListView recordLv;


    @OnClick({R.id.back_igv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
        }
    }

    private DoctorSHThread getRecordThread;
    private SharedPreferences sp;
    private int doctorId;
    private String customerId;
    private Context context;
    private DataInputRecordAdapter adapter;
    private JSONArray array;

    public final static String DATAINFO="data_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_data_input_record);
        ViewUtils.inject(this);

        context=this;
        titleTv.setText(getResources().getString(R.string.history_record));

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        customerId=sp.getString(AppConstant.USER_customerId,null);

        adapter=new DataInputRecordAdapter(context);
        recordLv.setAdapter(adapter);
        recordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                JSONObject jsonObject=array.getJSONObject(position);
                String images=jsonObject.getString("Images");
//                if(images!=null&&!images.equals("null")){
//                    JSONArray array=JSONArray.fromObject(images);
                    Intent intent=new Intent(context,DataInputRecordInfoActivity.class);
                    intent.putExtra(DATAINFO,jsonObject.toString());
                    startActivity(intent);
//                }else {
//
//                }
            }
        });

        getRecord();

    }

    void getRecord(){
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getRecordThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject json = JSONObject.fromObject(result);
                        int code = json.getInt("code");
                        if (code == 200) {
                            array=json.getJSONArray("DataList");
                        } else {
                            UiUtil.showToast(context,
                                    json.getString("message"));
                        }
                    }
                    adapter.setArray(array);
                    adapter.notifyDataSetChanged();
                }

            };
        };

        getRecordThread=new DoctorSHThread(ApiConstant.GETSHINPUTDATALIST,handler,context);
        getRecordThread.setDoctorId(doctorId);
        getRecordThread.setCustomerId(customerId);
        getRecordThread.setPageIndex(0);
        getRecordThread.setPageSize(20);
        getRecordThread.start();
    }

}

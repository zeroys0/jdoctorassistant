package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.DoctorUserListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ExceptionUserListActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.exception_userlist_title)
    private TextView titleTv;

    @ViewInject(R.id.exception_record_date_rg)
    private RadioGroup dateRg;
    @ViewInject(R.id.exception_user_lv)
    private ListView userLv;

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

    private DoctorUserListAdapter adapter;
    private JSONArray array;
    private DoctorSHThread getCustomerListThread;
    private SharedPreferences sp;
    private int doctorId;

    private String startDate;
    private String endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_user_list);
        ViewUtils.inject(this);

        context=this;
        titleTv.setText(getResources().getString(R.string.data_exception));

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);

        adapter=new DoctorUserListAdapter(context);
        userLv.setAdapter(adapter);
        userLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                JSONObject object=array.getJSONObject(position);
                String customerId=object.getString("CustomerId");
                sp.edit().putString(AppConstant.USER_customerInfo, object.toString()).commit();
                sp.edit().putString(AppConstant.USER_customerId,customerId).commit();
                startActivity(new Intent(context, PatientInfoActivity.class));
            }
        });

        dateRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {

                switch (checkId){
                    case R.id.exception_record_date_today_rb:
                        startDate= GetDate.getDateBefore(1);
//                        endDate=GetDate.currentDate();
                        break;
                    case R.id.exception_record_date_week_rb:
                        startDate=GetDate.lastWeek();
//                        endDate=GetDate.currentDate();
                        break;
                    case R.id.exception_record_date_month_rb:
                        startDate=GetDate.lastMonth();
                        break;
//                    case R.id.exception_record_date_other_rb:
//
//                        break;

                }
                endDate=GetDate.currentDate();
                getCustomerList();

            }
        });

        startDate=GetDate.getDateBefore(1);
        endDate=GetDate.currentDate();
        getCustomerList();
    }

    void getCustomerList(){
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = getCustomerListThread.getResult();
                Log.d("test","获取签约用户列表 result："+result);
                if(UiUtil.isResultSuccess(context,result)){
                    JSONObject jsonObject=JSONObject.fromObject(result);
                    int code=jsonObject.getInt("code");
                    if(code==200){
                        array=jsonObject.getJSONArray("Data");
                        adapter.setArray(array);
                    }else {
                        UiUtil.showToast(context,jsonObject.getString("message"));
                        adapter.setArray(null);
                    }
                }else{
                    adapter.setArray(null);
                }
                adapter.notifyDataSetChanged();

            }
        };
        getCustomerListThread = new DoctorSHThread(
                ApiConstant.GETALLCUSTOMER, handler, context);
        getCustomerListThread.setDoctorId(doctorId);
        getCustomerListThread.setCategory(AppConstant.CUSTOMER_EXCEPTION);
        getCustomerListThread.setCondition("");
        getCustomerListThread.setStartDate(startDate);
        getCustomerListThread.setEndDate(endDate);
        getCustomerListThread.start();
    }
}

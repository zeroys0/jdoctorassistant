package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.DoctorUserListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentUserList;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SpecialUserActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.special_user_title)
    private TextView titleTv;

    @ViewInject(R.id.special_care_lv)
    ListView userLv;

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
    private JSONArray array=null;
    private DoctorSHThread getCustomerListThread;
    private DoctorUserListAdapter adapter;
    private SharedPreferences sp;
    private int doctorId;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_special_care);
        ViewUtils.inject(this);
        context=this;
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        category =bundle.getInt(FragmentUserList.CUSTOMERTYPE,0);
        titleTv.setText(bundle.getString(FragmentUserList.CUSTOMERTITLE),null);
        Log.d( "handleMessage:coustmer ",category+"");
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
                    }else {
                        UiUtil.showToast(context,jsonObject.getString("message"));
                    }
                }else{

                }
                adapter.setArray(array);
                adapter.notifyDataSetChanged();

            }
        };
        getCustomerListThread = new DoctorSHThread(
                ApiConstant.GETALLCUSTOMER, handler, context);
        getCustomerListThread.setDoctorId(doctorId);
        getCustomerListThread.setCategory(category);
        getCustomerListThread.setCondition("");
        getCustomerListThread.start();
    }
}

package com.jxj.jdoctorassistant.main.doctor.personal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.LoginActivity;
import com.jxj.jdoctorassistant.main.doctor.DoctorMainActivity;
import com.jxj.jdoctorassistant.main.register.JobTitleListActivity;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.RequestPermissionType;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DoctorPersonalSetActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.personal_set_title)
    private TextView titleTv;

    @ViewInject(R.id.schedule_remind_cb)
    private CheckBox scheduleRemindCb;
    @ViewInject(R.id.sign_remind_cb)
    private CheckBox signRemindCb;
    @ViewInject(R.id.new_msg_remind_cb)
    private CheckBox newMsgRemindCb;
    @ViewInject(R.id.donot_disturb_remind_cb)
    private CheckBox notDistureRemindCb;

    @ViewInject(R.id.phone_tv)
    private TextView phoneTv;
    @ViewInject(R.id.hospital_address_tv)
    private TextView addressTv;

    @OnClick({R.id.back_igv,R.id.password_update_tv,R.id.contact_service_tv,R.id.feedback_tv,R.id.exit_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.password_update_tv:
                startActivity(new Intent(context,ChangePasswordActivity.class));
                break;
            case R.id.contact_service_tv:
                requestPermission();
                break;
            case R.id.feedback_tv:
                startActivity(new Intent(context,FeedbackActivity.class));
                break;
            case R.id.exit_btn:
                sp.edit().putInt(AppConstant.LOGIN_state,0).commit();
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                DoctorMainActivity.instance.finish();
                break;
            default:

                break;
        }
    }

    private Context context;
    private DoctorSHThread getMsgSetThread,setMsgSetThread,getHospitalListThread;
    private SharedPreferences sp;
    private int doctorId;
    public final static int   REQUESTCODE=115;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_doctor_personal_set);
        ViewUtils.inject(this);

        context=this;
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        phoneTv.setText(sp.getString(AppConstant.USER_doctor_username,""));
        initView();
    }


    void initView(){
        titleTv.setText(getResources().getString(R.string.setting));

        addressTv.setText("无");
        addressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context,HospitalAddressListActivity.class),REQUESTCODE);
            }
        });

        phoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,UpdatePhoneActivity.class));
            }
        });

        scheduleRemindCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setMsgSet();
            }
        });
        signRemindCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setMsgSet();
            }
        });
        newMsgRemindCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setMsgSet();
            }
        });
        notDistureRemindCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setMsgSet();
            }
        });

        getMsgSet();
        getHospitalList();
    }


    void getHospitalList(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result= getHospitalListThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            JSONArray array=jsonObject.getJSONArray("Data");
                            for(int i=0;i<array.size();i++){
                                JSONObject object=array.getJSONObject(i);
                                boolean isDefault=object.getBoolean("IsDefault");
                                if(isDefault){
                                    addressTv.setText(object.getString("Hospital"));
                                    break;
                                }
                            }

                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        getHospitalListThread =new DoctorSHThread(ApiConstant.GETDOCTORADDRESSLIST,handler,context);
        getHospitalListThread.setDoctorId(doctorId);
        getHospitalListThread.start();

    }

    void getMsgSet(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getMsgSetThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            JSONObject data=jsonObject.getJSONObject("Data");
                            scheduleRemindCb.setChecked(data.getBoolean("Schedule"));
                            signRemindCb.setChecked(data.getBoolean("Sign"));
                            newMsgRemindCb.setChecked(data.getBoolean("NewMsg"));
                            notDistureRemindCb.setChecked(data.getBoolean("DonotDisturb"));
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        getMsgSetThread=new DoctorSHThread(ApiConstant.GETSHMSGSETTINGS,handler,context);
        getMsgSetThread.setDoctorId(doctorId);
        getMsgSetThread.start();
    }

    void setMsgSet(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=setMsgSetThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");

                    }
                }
            }
        };
        setMsgSetThread=new DoctorSHThread(ApiConstant.SETSHMSGSETTINGS,handler,context);
        setMsgSetThread.setDoctorId(doctorId);
        setMsgSetThread.setSchedule(scheduleRemindCb.isChecked());
        setMsgSetThread.setSign(signRemindCb.isChecked());
        setMsgSetThread.setNewMsg(newMsgRemindCb.isChecked());
        setMsgSetThread.setDonotDisturb(notDistureRemindCb.isChecked());
        setMsgSetThread.start();
    }

    /**
     * 申请权限
     */
    private void requestPermission()
    {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        RequestPermissionType.REQUEST_CODE_ASK_CALL_PHONE);
                return;
            }
            else
            {
                callPhone();
            }
        }
        else
        {
            callPhone();
        }
    }

    /**
     * 注册权限申请回调
     * @param requestCode 申请码
     * @param permissions 申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case  RequestPermissionType.REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    callPhone();
                }
                else
                {
                    // Permission Denied
                    Toast.makeText(context, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 拨号方法
     */
    private void callPhone()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+getResources().getString(R.string.service_phone)));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUESTCODE){
            switch (resultCode){
                case HospitalAddressListActivity.RESULTCODE:
                    Bundle bundle=data.getExtras();
                    String hospital=bundle.getString(HospitalAddressListActivity.HOSPITALNAME);
                    addressTv.setText(hospital);
                    break;
                default:
                    getHospitalList();
                    break;
            }
        }

    }
//    void contactService(){
//        String phone=getResources().getString(R.string.service_phone);
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    this,
//                    new String[]{Manifest.permission.CALL_PHONE},
//                    Integer.parseInt(phone));
//        } else {
//            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+phone)));
//        }
//    }
}

package com.jxj.jdoctorassistant.main.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.doctor.personal.HospitalAddressListActivity;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

public class PerfectInfoActivity extends Activity {
    @ViewInject(value = R.id.title_tv)
    TextView titleTv;

    @ViewInject(R.id.perfect_name_et)
    EditText nameEt;
    @ViewInject(R.id.perfect_hospital_tv)
    TextView hospitalTv;
    @ViewInject(R.id.perfect_department_etv)
    EditText departmentEtv;
    @ViewInject(R.id.perfect_job_title_tv)
    TextView jobTitleTv;
    @ViewInject(R.id.perfect_phone_et)
    EditText phoneEt;
    @ViewInject(R.id.perfect_personal_introduction_et)
    EditText introductionEt;

    @OnClick({R.id.back_igv,R.id.perfect_next_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.perfect_next_btn:
                perfectInfo();
//                startActivity(new Intent(context,DoctorCertificateActivity.class));
                break;
            default:
                break;
        }
    }


    private Context context;
//    private final static int hospitalCode=0x01;
//    private final static int departmentCode=0x02;
//    private final static int jobTitleCode=0x03;
    private final static int REQUESTCODE=0x01;
    private DoctorSHThread updateDoctorInfoThread;
    private int doctorId=0;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_perfect_info);
        ViewUtils.inject(this);

        context=this;
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);

        titleTv.setText(getResources().getString(R.string.perfect_personal_info));
        hospitalTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(context,HospitalAddressListActivity.class);
                startActivityForResult(intent,REQUESTCODE);
            }
        });


//        departmentTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent();
//                intent.setClass(context,DepartmentListActivity.class);
//                startActivityForResult(intent,REQUESTCODE);
//            }
//        });

        jobTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(context,JobTitleListActivity.class);
                startActivityForResult(intent,REQUESTCODE);
            }
        });

        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        if(sp.contains(AppConstant.USER_doctor_info)){
            String str=sp.getString(AppConstant.USER_doctor_info,null);
            if(str!=null){
                JSONObject json=JSONObject.fromObject(str);
                nameEt.setText(json.getString("Name"));
                hospitalTv.setText(json.getString("Hospital"));
                departmentEtv.setText(json.getString("Department"));
                jobTitleTv.setText(json.getString("Title"));
                phoneEt.setText(json.getString("Tel"));
                introductionEt.setText(json.getString("Resume"));
            }
        }

    }

    void perfectInfo(){
        String name=nameEt.getEditableText().toString().trim();
        if(name.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_name_hint));
            return;
        }
        String hospital=hospitalTv.getText().toString().trim();
        if(hospital.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_hospital_hint));
            return;
        }
        String department=departmentEtv.getEditableText().toString().trim();
        if(department.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_department_hint));
            return;
        }
        String title=jobTitleTv.getText().toString().trim();
        if(title.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_title_hint));
            return;
        }
        String tel=phoneEt.getEditableText().toString().trim();
        if(tel.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_phone_hint));
            return;
        }
        String rusume=introductionEt.getEditableText().toString().trim();
        if(rusume.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_resume_hint));
            return;
        }
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=updateDoctorInfoThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            startActivity(new Intent(context,DoctorCertificateActivity.class));
                            finish();
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        updateDoctorInfoThread=new DoctorSHThread(ApiConstant.UPDATEDOCTORINFO,handler,context);
        updateDoctorInfoThread.setDoctorId(doctorId);
        updateDoctorInfoThread.setNamee(name);
        updateDoctorInfoThread.setHospital(hospital);
        updateDoctorInfoThread.setDepartment(department);
        updateDoctorInfoThread.setTitle(title);
        updateDoctorInfoThread.setTel(tel);
        updateDoctorInfoThread.setResume(rusume);
        updateDoctorInfoThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUESTCODE){
            Bundle bundle=data.getExtras();
            switch (resultCode){
                case HospitalAddressListActivity.RESULTCODE:
                    String hospital=bundle.getString(HospitalAddressListActivity.HOSPITALNAME);
                    hospitalTv.setText(hospital);
                    break;
//                case departmentCode:
//                    String department=bundle.getString("department");
//                    departmentTv.setText(department);
//                    break;
                case JobTitleListActivity.RESULTCODE:
                    String jobTitle=bundle.getString(JobTitleListActivity.JOBTITLE);
                    jobTitleTv.setText(jobTitle);
                    break;
                default:
                    break;
            }
        }

    }
}

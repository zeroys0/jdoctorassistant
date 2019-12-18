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
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.DataUtil;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.DecimalFormat;

public class ContrastDataActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.contrast_data_title)
    private TextView titleTv;

    @ViewInject(R.id.time1_tv)
    private TextView time1Tv;
    @ViewInject(R.id.height1_tv)
    private TextView height1Tv;
    @ViewInject(R.id.weight1_tv)
    private TextView weight1Tv;
    @ViewInject(R.id.fat1_tv)
    private TextView fat1Tv;
    @ViewInject(R.id.ps1_tv)
    private TextView ps1Tv;
    @ViewInject(R.id.pd1_tv)
    private TextView pd1Tv;
    @ViewInject(R.id.bo1_tv)
    private TextView bo1Tv;
    @ViewInject(R.id.hr1_tv)
    private TextView hr1Tv;
    @ViewInject(R.id.temperature1_tv)
    private TextView temperature1Tv;
    @ViewInject(R.id.whr1_tv)
    private TextView whr1Tv;
    @ViewInject(R.id.bs1_tv)
    private TextView bs1Tv;
    @ViewInject(R.id.ua1_tv)
    private TextView ua1Tv;
    @ViewInject(R.id.chol1_tv)
    private TextView chol1Tv;

    @ViewInject(R.id.time2_tv)
    private TextView time2Tv;
    @ViewInject(R.id.height2_tv)
    private TextView height2Tv;
    @ViewInject(R.id.weight2_tv)
    private TextView weight2Tv;
    @ViewInject(R.id.fat2_tv)
    private TextView fat2Tv;
    @ViewInject(R.id.ps2_tv)
    private TextView ps2Tv;
    @ViewInject(R.id.pd2_tv)
    private TextView pd2Tv;
    @ViewInject(R.id.bo2_tv)
    private TextView bo2Tv;
    @ViewInject(R.id.hr2_tv)
    private TextView hr2Tv;
    @ViewInject(R.id.temperature2_tv)
    private TextView temperature2Tv;
    @ViewInject(R.id.whr2_tv)
    private TextView whr2Tv;
    @ViewInject(R.id.bs2_tv)
    private TextView bs2Tv;
    @ViewInject(R.id.ua2_tv)
    private TextView ua2Tv;
    @ViewInject(R.id.chol2_tv)
    private TextView chol2Tv;

    @ViewInject(R.id.height_mtv)
    private TextView height_mTv;
    @ViewInject(R.id.weight_mtv)
    private TextView weight_mTv;
    @ViewInject(R.id.fat_mtv)
    private TextView fat_mTv;
    @ViewInject(R.id.ps_mtv)
    private TextView ps_mTv;
    @ViewInject(R.id.pd_mtv)
    private TextView pd_mTv;
    @ViewInject(R.id.bo_mtv)
    private TextView bo_mTv;
    @ViewInject(R.id.hr_mtv)
    private TextView hr_mTv;
    @ViewInject(R.id.temperature_mtv)
    private TextView temperature_mTv;
    @ViewInject(R.id.whr_mtv)
    private TextView whr_mTv;
    @ViewInject(R.id.bs_mtv)
    private TextView bs_mTv;
    @ViewInject(R.id.ua_mtv)
    private TextView ua_mTv;
    @ViewInject(R.id.chol_mtv)
    private TextView chol_mTv;

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


    private DoctorSHThread getDataThread;
    private Context context;

    private SharedPreferences sp;
    private int doctorId;

    private String date,idCord,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contrast_data);
        ViewUtils.inject(this);

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);

        context=this;

        titleTv.setText("对比-体检数据");

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        date= GetDate.currentDate();
        if(bundle.containsKey(PatientInfoActivity.RECORDDATE)){
            date=bundle.getString(PatientInfoActivity.RECORDDATE);
        }
        if(bundle.containsKey(PatientInfoActivity.IDCORD)){
            idCord=bundle.getString(PatientInfoActivity.IDCORD);
        }
        if(bundle.containsKey(PatientInfoActivity.PHONE)){
            phone=bundle.getString(PatientInfoActivity.PHONE);
        }

        getData();
    }

    void getData(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getDataThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            JSONArray dataList=jsonObject.getJSONArray("DataList");
                            if(dataList.size()>1) {
                                JSONObject json1 = dataList.getJSONObject(1);
                                time1Tv.setText(json1.getString("MeasureTime"));
                                height1Tv.setText(isNull(json1.getString("Height")));
                                weight1Tv.setText(isNull(json1.getString("Weight")));
                                fat1Tv.setText(isNull(json1.getString("Weight")));
                                ps1Tv.setText(isNull(json1.getString("HighPressure")));
                                pd1Tv.setText(isNull(json1.getString("LowPressure")));
                                bo1Tv.setText(isNull(json1.getString("Oxygen")));
                                hr1Tv.setText(isNull(json1.getString("Hr_ECG")));
                                temperature1Tv.setText(isNull(json1.getString("Temperature")));
                                whr1Tv.setText(isNull(json1.getString("Whr")));
                                bs1Tv.setText(isNull(json1.getString("BloodSugar")));
                                ua1Tv.setText(isNull(json1.getString("Ua")));
                                chol1Tv.setText(isNull(json1.getString("Chol")));

                                JSONObject json2=dataList.getJSONObject(0);
                                time2Tv.setText(json2.getString("MeasureTime"));
                                height2Tv.setText(isNull(json2.getString("Height")));
                                weight2Tv.setText(isNull(json2.getString("Weight")));
                                fat2Tv.setText(isNull(json2.getString("Weight")));
                                ps2Tv.setText(isNull(json2.getString("HighPressure")));
                                pd2Tv.setText(isNull(json2.getString("LowPressure")));
                                bo2Tv.setText(isNull(json2.getString("Oxygen")));
                                hr2Tv.setText(isNull(json2.getString("Hr_ECG")));
                                temperature2Tv.setText(isNull(json2.getString("Temperature")));
                                whr2Tv.setText(isNull(json2.getString("Whr")));
                                bs2Tv.setText(isNull(json2.getString("BloodSugar")));
                                ua2Tv.setText(isNull(json2.getString("Ua")));
                                chol2Tv.setText(isNull(json2.getString("Chol")));

                                height_mTv.setText(toValue(height1Tv,height2Tv));
                                weight_mTv.setText(toValue(weight1Tv,weight2Tv));
                                fat_mTv.setText(toValue(fat1Tv,fat2Tv));
                                ps_mTv.setText(toValue(ps1Tv,ps2Tv));
                                pd_mTv.setText(toValue(pd1Tv,pd2Tv));
                                bo_mTv.setText(toValue(bo1Tv,bo2Tv));
                                hr_mTv.setText(toValue(hr1Tv,hr2Tv));
                                temperature_mTv.setText(toValue(temperature1Tv,temperature2Tv));
                                whr_mTv.setText(toValue(whr1Tv,whr2Tv));
                                bs_mTv.setText(toValue(bs1Tv,bs2Tv));
                                ua_mTv.setText(toValue(ua1Tv,ua2Tv));
                                chol_mTv.setText(toValue(chol1Tv,chol2Tv));

                            }


                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        getDataThread=new DoctorSHThread(ApiConstant.GETSHHEALTHDATACONTRAST,handler,context);
        getDataThread.setDoctorId(doctorId);
        getDataThread.setIdCord(idCord);
        getDataThread.setMobile(phone);
        getDataThread.setDatetime(date);
        getDataThread.start();
    }

    String toValue(TextView t1,TextView t2){
        String s1=t1.getText().toString().trim();
        String s2=t2.getText().toString().trim();
        if(isEmpty(s1)){
            return "--";
        }else if(isEmpty(s2)) {
            return "--";
        }else {
            double d1=Double.parseDouble(s1);
            double d2=Double.parseDouble(s2);
            double d=d2-d1;
            if(d>0){
                return "↑"+String.format("%.2f",d2-d1);
            }else if(d<0){
                return "↓"+String.format("%.2f",d1-d2);
            }else {
                return "--";
            }
        }

    }

    private String isNull(String str){
        if(str==null||str.equals("null")||str.equals("--")){
            return "--";
        }else {
            return str;
        }
    }

    private boolean isEmpty(String str){
        if(str==null||str.equals("null")||str.equals("--")){
            return true;
        }else {
            return false;
        }
    }

}

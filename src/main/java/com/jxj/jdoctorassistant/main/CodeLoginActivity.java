package com.jxj.jdoctorassistant.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.community.activity.MainPinguActivity;
import com.jxj.jdoctorassistant.main.doctor.DoctorMainActivity;
import com.jxj.jdoctorassistant.main.register.RegisterForDoctorActivity;
import com.jxj.jdoctorassistant.main.register.ResultActivity;
import com.jxj.jdoctorassistant.thread.DoctorLoginThread;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.Acache;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeLoginActivity extends Activity {
    @ViewInject(R.id.text_code_register)
    TextView text_code_register;
    @ViewInject(R.id.tv_password_login)
    TextView tv_password_login;
    @ViewInject(R.id.text_get_code)
    TextView text_get_code;
    @ViewInject(R.id.login_username_etv)
    EditText login_username_etv;
    @ViewInject(R.id.login_password_etv)
    EditText login_password_etv;

    private Context context;

    private DoctorSHThread loginThread;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String phone,code;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_login);
        ButterKnife.bind(this);
        context = this;
        sp = getSharedPreferences(AppConstant.USER_sp_name,
                MODE_APPEND);
        editor=sp.edit();
    }


    @OnClick({R.id.text_code_register,R.id.tv_password_login,R.id.text_get_code})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.text_code_register:    //去注册
                Intent intent = new Intent(this,RegisterForDoctorActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_password_login:    //返回密码登录
                finish();
                break;
            case R.id.text_get_code:
                phone = login_username_etv.getText().toString().trim();
                code =login_password_etv.getText().toString().trim();
                DoctorLogin();
                break;
                default:
                    break;
        }
    }

    public void DoctorLogin(){  //验证码登录
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x133) {
                    try {
                        String result=loginThread.getResult();
                        if(UiUtil.isResultSuccess(context, result)) {
                            JSONObject json = JSONObject.fromObject(result);
                            int code = json.getInt("code");
                            if (code == 200) {
                                JSONObject data = json.getJSONObject("Data");

                                int audit=data.getInt("AUDIT");
                                int doctorId=data.getInt("Id");
                                String account=data.getString("Account");
                                sp.edit().putInt(AppConstant.USER_doctor_id,doctorId).commit();
                                sp.edit().putString(AppConstant.USER_doctor_info,data.toString()).commit();
                                sp.edit().putInt(AppConstant.USER_doctor_community_id,data.getInt("CommunityId")).commit();
                                sp.edit().putString(AppConstant.USER_doctor_ease_name,account).commit();
                                if(audit==1){

                                    int userId = data.getInt("UserId");
                                    sp.edit().putInt(AppConstant.ADMIN_userId, userId).commit();
                                    Acache.get(context).put("userdata",data);
                                    sp.edit().putString(AppConstant.USER_doctor_ease_name,account).commit();
                                    sp.edit().putInt(AppConstant.LOGIN_state,AppConstant.DOCTOR_LOGIN).commit();
                                    startActivity(new Intent(context, DoctorMainActivity.class));
                                }else{
                                    startActivity(new Intent(context, ResultActivity.class));
                                }



                            } else {
                                UiUtil.showToast(context, json.getString("message"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        loginThread = new DoctorSHThread(ApiConstant.DOCTOR_LOGIN_BY_CODE,handler, context);
        loginThread.setPhone(phone);
        loginThread.setPassword(code);
        loginThread.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

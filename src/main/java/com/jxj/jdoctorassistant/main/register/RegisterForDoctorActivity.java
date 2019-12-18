package com.jxj.jdoctorassistant.main.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;

import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.IprecareThread;
import com.jxj.jdoctorassistant.thread.PopularThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("all")
public class RegisterForDoctorActivity extends Activity {


    @ViewInject(R.id.phone_number_et)
    EditText phoneNumberEt;
    @ViewInject(R.id.sms_code_et)
    EditText smsCodeEt;
    @ViewInject(R.id.pwd_et)
    EditText pwdEt;
    @ViewInject(R.id.text_login)
    TextView text_login;


    @ViewInject(R.id.pwd_cb)
    CheckBox pwdCb;
    @ViewInject(R.id.register_btn)
    Button registerBtn;
    @ViewInject(R.id.get_sms_code_tv)
    TextView get_sms_code_tv;

    //    @ViewInject(R.id.register_btn)
//    Button registerBtn;
    @OnClick({R.id.back_igv, R.id.get_sms_code_tv})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.get_sms_code_tv:
                sendCode();
                break;
//            case R.id.register_protocol_tv:
//                startActivity(new Intent(context, ProtocolActivity.class));
//                break;
            default:
                break;
        }
    }

    private DoctorSHThread verifyPhoneThread, registerThread;
    private PopularThread getImageCodeThread, sendCodeThread, addEasemobAccountThread;
    private String token;


    private Context context;
    private final int PHONENUM = 11;
    private boolean isValid = false;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_for_doctor);
        ViewUtils.inject(this);
        context = this;
        sp = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
        editor = sp.edit();
//        titleTv.setText(getResources().getString(R.string.doctor_register));
//        getImageCode();
        setEditListener();
//        graphCodeIgv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isValid) {
//                    getImageCode();
//                }
//            }
//        });

        setInit();


    }

    private void setInit() {
        pwdCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    pwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
                    registerBtn.setBackgroundResource(R.drawable.blue_bg);
                    registerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        //    register();
                            Intent intent = new Intent(RegisterForDoctorActivity.this,RegisterSucessActivity.class);
                            startActivity(intent);
                        }
                    });

        text_login.setOnClickListener(new View.OnClickListener() {  //退回登录页
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setEditListener() {
        phoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = editable.toString().trim();
                if (phone.length() == PHONENUM) {
                    isValid(phone);
                } else {
                    isValid = false;
                }
            }
        });
    }


    private void register() {


        final String phone = phoneNumberEt.getEditableText().toString().trim();
        if (phone.length() < 1) {
            UiUtil.showToast(context, getResources().getString(R.string.input_phone));
            return;
        }
        String smsCode = smsCodeEt.getEditableText().toString().trim();
        if (smsCode.length() < 1) {
            UiUtil.showToast(context, getResources().getString(R.string.input_sms_code));
            return;
        }
        final String pwd = pwdEt.getEditableText().toString().trim();
        if (pwd.length() < 1) {
            UiUtil.showToast(context, getResources().getString(R.string.set_password));
            return;
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = registerThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        UiUtil.showToast(context, jsonObject.getString("message"));
                        if (code == 200) {
                            int data = jsonObject.getInt("Data");
                            sp.edit().putInt(AppConstant.USER_doctor_id, data).commit();

//                                register(phone,pwd);

                            startActivity(new Intent(context, PerfectInfoActivity.class));
                            finish();
                        }
//                            else {
////                                UiUtil.showToast(context,jsonObject.getString("message"));
////                            }
                    }
                }
            }
        };
        registerThread = new DoctorSHThread(ApiConstant.REGISTERDOCTOR, handler, context);
        registerThread.setPhone(phone);
        registerThread.setPassword(pwd);
        registerThread.setSmsCode(smsCode);
        registerThread.start();


    }

    //    private void register(final String userPhone, final String pwd){
//        if(TextUtils.isEmpty(userPhone)||
//                TextUtils.isEmpty(pwd)){
//            UiUtil.showToast(context,"不能为空");
//
//        }
//
//        new Thread(){
//            @Override
//            public void run() {
//                try{
//                    String pwdd=userPhone.substring(7)+pwd.substring(0,3);
//                    System.out.println("注册  环信密码："+pwdd);
//                    EMClient.getInstance().createAccount(userPhone,pwdd);
//
//                    addEasemobAccount(userPhone,pwdd);
////                    handler.sendEmptyMessage(REG_SUCCESS);
//
//                }catch (HyphenateException e){
//                    e.printStackTrace();
//                    Log.e("错误信息:" ,e.getMessage());
//                    e.getErrorCode();
////                  User already exist    203
////                  Registration failed   208
//
////                    handler.sendEmptyMessage(REG_FAILED);
////                    UiUtil.showToast(context,"环信 账号注册失败");
//                }
//            }
//        }.start();
//    }
//    void addEasemobAccount(String account,String password){
//        Handler handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if(msg.what==ApiConstant.MSG_API_HANDLER){
//                    String result=addEasemobAccountThread.getResult();
//                    if(UiUtil.isResultSuccess(context,result)){
//                        JSONObject jsonObject=JSONObject.fromObject(result);
//                        int code=jsonObject.getInt("code");
//                        if(code==200){
//                            startActivity(new Intent(context,PerfectInfoActivity.class));
//                            loaded();
//                        }else {
//                            UiUtil.showToast(context, jsonObject.getString("message"));
//                        }
//                    }
//                }
//            }
//        };
//        addEasemobAccountThread=new PopularThread(ApiConstant.ADDEASEMOBACCOUNT,handler,context);
//        addEasemobAccountThread.setAccount(account);
//        addEasemobAccountThread.setPassword(password);
//        addEasemobAccountThread.start();
//    }
    int s = 0;
    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 59) {
                s--;
                get_sms_code_tv.setText(s+"s");
                get_sms_code_tv.setEnabled(false);
                if(s<1) {
                    get_sms_code_tv.setText("获取验证码");
                    get_sms_code_tv.setEnabled(true);
                }
            }
        }
    };
    private void sendCode() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = sendCodeThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if(code==200){
                          UiUtil.showToast(context, jsonObject.getString("message"));
                          s = 120;
                          TimerTask timerTask = new TimerTask() {
                              @Override
                              public void run() {
                                  Message msg = new Message();
                                  msg.what = 59;
                                  mhandler.sendMessage(msg);
                              }
                          };
                          Timer timer = new Timer();
                          timer.schedule(timerTask,0,1000);
                        }else {
                        UiUtil.showToast(context, jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        sendCodeThread = new PopularThread(ApiConstant.SENDAUTHCODE, handler, context);
        sendCodeThread.setPhone(phoneNumberEt.getEditableText().toString().trim());
        sendCodeThread.setKey(MyApplication.key);
        sendCodeThread.setUsed(AppConstant.USED_REGISTER);
        sendCodeThread.start();
    }



    private void isValid(String string) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = verifyPhoneThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            isValid = true;
                            getImageCode();
                        } else {
                            UiUtil.showToast(context, jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        verifyPhoneThread = new DoctorSHThread(ApiConstant.VERIFYPHONE, handler, context);
        verifyPhoneThread.setPhone(string);
        verifyPhoneThread.start();
    }

    private void getImageCode() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x133) {
                    String result = getImageCodeThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject json = JSONObject.fromObject(result);
                        int code = json.getInt("code");
                        if (code == 200) {
                            token = json.getString("Token");
                            String image = json.getString("Image");

                        } else {
                            UiUtil.showToast(context, json.getString("message"));
                        }

                    }
                }
            }
        };

        getImageCodeThread = new PopularThread(ApiConstant.GETIMAGECALIDATE, handler, context);
        getImageCodeThread.start();
    }
}

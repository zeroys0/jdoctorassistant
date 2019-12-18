package com.jxj.jdoctorassistant.main.doctor.personal;

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
import com.jxj.jdoctorassistant.main.register.PerfectInfoActivity;
import com.jxj.jdoctorassistant.main.register.ProtocolActivity;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.IprecareThread;
import com.jxj.jdoctorassistant.thread.PopularThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

public class UpdatePhoneActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.update_phone_title)
    private TextView titleTv;

    @ViewInject(R.id.phone_number_et)
    EditText phoneNumberEt;
    @ViewInject(R.id.get_graph_code_igv)
    ImageView graphCodeIgv;
    @ViewInject(R.id.graph_code_et)
    EditText graphCodeEt;
    @ViewInject(R.id.sms_code_et)
    EditText smsCodeEt;
//    @ViewInject(R.id.pwd_et)
//    EditText pwdEt;
//    @ViewInject(R.id.pwd_again_et)
//    EditText pwdAgainEt;

//    @ViewInject(R.id.pwd_cb)
//    CheckBox pwdCb;
//    @ViewInject(R.id.pwd_again_cb)
//    CheckBox pwdAgainCb;
//    @ViewInject(R.id.agree_protocol_cb)
//    CheckBox agreeProtocolCb;
//    @ViewInject(R.id.register_btn)
//    Button registerBtn;

//    @ViewInject(R.id.register_btn)
//    Button registerBtn;
    @OnClick({R.id.back_igv,R.id.get_sms_code_tv,R.id.finish_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.get_sms_code_tv:
                sendCode();
                break;
            case R.id.finish_btn:
                updatePhone();
                break;
            default:
                break;
        }
    }

    private PopularThread getImageCodeThread,sendCodeThread;
    private DoctorSHThread verifyPhoneThread,updatePhoneThread;
//    private PopularThread addEasemobAccountThread;
    private String token;

    private Context context;
    private final int PHONENUM=11;
    private boolean isValid=false;
    private SharedPreferences sp;
//    private SharedPreferences.Editor editor;
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_phone);
        ViewUtils.inject(this);
        context=this;
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        phoneNumberEt.setText(sp.getString(AppConstant.USER_doctor_username,""));
//        editor=sp.edit();
        titleTv.setText("验证手机");
//        getImageCode();
        setEditListener();
        graphCodeIgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid){
                    getImageCode();
                }
            }
        });
        getImageCode();
    }

    private void setEditListener(){
        phoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone=editable.toString().trim();
                if(phone.length()==PHONENUM){
                    isValid(phone);
                }else {
                    isValid=false;
                }
            }
        });
    }

    private void updatePhone(){



        final String phone=phoneNumberEt.getEditableText().toString().trim();
        if(phone.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.input_phone));
            return;
        }
        String smsCode=smsCodeEt.getEditableText().toString().trim();
        if(smsCode.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.input_sms_code));
            return;
        }

            Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what==ApiConstant.MSG_API_HANDLER){
                        String result=updatePhoneThread.getResult();
                        if(UiUtil.isResultSuccess(context,result)){
                            JSONObject jsonObject=JSONObject.fromObject(result);
                            int code=jsonObject.getInt("code");
                            UiUtil.showToast(context,jsonObject.getString("message"));
//                            if(code==200){
//
//                            }else {
//
//                            }
                        }
                    }
                }
            };
        updatePhoneThread=new DoctorSHThread(ApiConstant.CHANGESHDOCTORPHONE,handler,context);
        updatePhoneThread.setDoctorId(doctorId);
        updatePhoneThread.setPhone(phone);
        updatePhoneThread.setSmsCode(smsCode);
        updatePhoneThread.start();

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
    private void sendCode(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                    String result=sendCodeThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
//                        if(code==200){
//
//                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
//                        }
                    }
                }
            }
        };
        sendCodeThread=new PopularThread(ApiConstant.SENDAUTHCODE,handler,context);
        sendCodeThread.setPhone(phoneNumberEt.getEditableText().toString().trim());
        sendCodeThread.setKey(MyApplication.key);
        sendCodeThread.setUsed(AppConstant.USED_UPDATE_PHONENUMBER);
        sendCodeThread.start();
    }
    private void isValid(String string){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                String result= verifyPhoneThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            isValid=true;
                            getImageCode();
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        verifyPhoneThread=new DoctorSHThread(ApiConstant.VERIFYPHONE,handler,context);
        verifyPhoneThread.setPhone(string);
        verifyPhoneThread.start();
    }
    private void getImageCode(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x133){
                    String result=getImageCodeThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject json=JSONObject.fromObject(result);
                        int code=json.getInt("code");
                        if(code==200){
                            token=json.getString("Token");
                            String image=json.getString("Image");
                            graphCodeIgv.setImageBitmap(ImageUtil.getBitmap(image));

                        }else{
                            UiUtil.showToast(context,json.getString("message"));
                        }

                    }
                }
            }
        };

        getImageCodeThread=new PopularThread(ApiConstant.GETIMAGECALIDATE,handler,context);
        getImageCodeThread.start();
    }
}

package com.jxj.jdoctorassistant.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.PopularThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends Activity {
@ViewInject(R.id.text_register)
TextView text_register;

EditText new_password_edit;
EditText new_password_again_edit;
EditText login_code;
EditText login_username_etv;
TextView text_get_code;

PopularThread sendCodeThread;
DoctorSHThread resetPasswordThread;
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        context = this;
        init();
    }

    public void init(){
        login_username_etv = findViewById(R.id.login_username_etv);
        text_get_code = findViewById(R.id.text_get_code);
        login_code = findViewById(R.id.login_code);
        new_password_edit = findViewById(R.id.new_password_edit);
        new_password_again_edit = findViewById(R.id.new_password_again_edit);
    }
    @OnClick({R.id.text_register,R.id.btn_submit,R.id.text_get_code})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_register:    //返回登录
                finish();
                break;
            case R.id.btn_submit:   //提交修改
                check();
                break;
            case R.id.text_get_code:    //发送短信验证码
                if (login_username_etv.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_LONG).show();
                } else {
                    sendCode();
                }
                break;

        }
    }


    public void check(){
        if(!login_code.getText().toString().trim().equals("")) {
            if(new_password_edit.getText().toString().trim().equals(new_password_again_edit.getText().toString().trim())) {
                resetPassword();
            } else {
                Toast.makeText(this, "两次输入的密码不同,请重新确认", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
        }
    }

    //重置密码
    public void resetPassword(){
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = resetPasswordThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        UiUtil.showToast(context, jsonObject.getString("message"));
                    }
                }
            }
        };
        resetPasswordThread = new DoctorSHThread(ApiConstant.RESETSHDOCTORPASSWORD, handler, context);
        resetPasswordThread.setPhone(login_username_etv.getEditableText().toString().trim());
        resetPasswordThread.setPwd(new_password_edit.getText().toString().trim());
        resetPasswordThread.setSmsCode(login_code.getText().toString().trim());
        resetPasswordThread.start();
    }

    //发送短信验证码
    private void sendCode() {
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
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
        sendCodeThread.setPhone(login_username_etv.getEditableText().toString().trim());
        sendCodeThread.setKey(MyApplication.key);
        sendCodeThread.setUsed(AppConstant.USED_RESET_PASSWORD);
        sendCodeThread.start();
    }

    int s = 0;
    @SuppressLint("HandlerLeak")
    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 59) {
                s--;
                text_get_code.setText(s+"s");
                text_get_code.setEnabled(false);
                if(s<1) {
                    text_get_code.setText("获取验证码");
                    text_get_code.setEnabled(true);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

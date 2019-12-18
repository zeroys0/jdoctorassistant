package com.jxj.jdoctorassistant.main.doctor.personal;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jxj.jdoctorassistant.R;

public class PhoneActivity extends Activity implements View.OnClickListener {
private LinearLayout ll_back;
private Button btn_update_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_phone);
        init();
    }

    public void init() {
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        btn_update_phone = findViewById(R.id.btn_update_phone);
        btn_update_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:      //后退
                finish();
                break;
            case R.id.btn_update_phone: //修改手机号码
                Intent intent = new Intent(this,UpdatePhoneActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}

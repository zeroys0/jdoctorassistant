package com.jxj.jdoctorassistant.main.register;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterSucessActivity extends Activity {
@ViewInject(R.id.register_btn)
    Button register_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sucess);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.register_btn)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.register_btn:     //去提交审核资料
                Intent intent = new Intent(this,PerfectInfoActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

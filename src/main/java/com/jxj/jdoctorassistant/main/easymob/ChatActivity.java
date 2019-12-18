package com.jxj.jdoctorassistant.main.easymob;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Window;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.AppConstant;


public class ChatActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        EMClient.getInstance().addConnectionListener(new MyConnectionListener(this));

//        AudioRecord();

        EaseChatFragment chatFragment=new EaseChatFragment();


        Bundle args=new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID,getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));
        args.putString("nickname",getIntent().getStringExtra(AppConstant.USER_cname));
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_chat_content,chatFragment).commit();

    }

    void AudioRecord(){
        if(Build.VERSION.SDK_INT>=23){
            int request= ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if(request!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},825638);

            }else {

            }
        }
    }
}

package com.jxj.jdoctorassistant.main.doctor;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentMessage;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentPersonal;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentSchedule;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentUserList;
import com.jxj.jdoctorassistant.main.easymob.MyConnectionListener;
import com.jxj.jdoctorassistant.util.Acache;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONObject;


public class DoctorMainActivity extends Activity {

    @ViewInject(R.id.doctor_main_rg)
    private RadioGroup mainRg;
    @ViewInject(R.id.doctor_message_rb)
    private RadioButton messageRb;
    @ViewInject(R.id.doctor_schedule_rb)
    private RadioButton scheduleRb;
    @ViewInject(R.id.doctor_userlist_rb)
    private RadioButton userListRb;
    @ViewInject(R.id.doctor_personal_rb)
    private RadioButton personalRb;


    private Context context;
    private static final int SIZE_1=3;
    private static final int SIZE_2=4;
    public static DoctorMainActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_doctor_main);
        ViewUtils.inject(this);
        context=this;

        instance=this;
        JSONObject jb = Acache.get(context).getAsJSONObject("userdata");
        Log.d( "如果这里有的话就太好了",jb.isEmpty()+"");

//        EMClient.getInstance().addConnectionListener(new MyConnectionListener(this));

        initRadioButton(messageRb);
        initRadioButton(scheduleRb);
        initRadioButton(userListRb);
        initRadioButton(personalRb);

//        initEasy();
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    List<String> usernames=EMClient.getInstance().contactManager().getAllContactsFromServer();
//                    Log.e("环信信息：","好友数量："+usernames.size()+usernames.get(0));
//
//                }catch (HyphenateException e){
//                    e.printStackTrace();
//                    Log.e("错误信息:" ,e.getMessage());
//                    e.getErrorCode();
//                }
//
//            }
//
//
//        }.start();

        switchFragment(0);

        setFragmentPage();
    }

    void initRadioButton(RadioButton rb){
        Drawable[] drs=rb.getCompoundDrawables();
        Rect r=new Rect(0,0,drs[1].getMinimumWidth()*SIZE_1/SIZE_2,drs[1].getMinimumHeight()*SIZE_1/SIZE_2);
        drs[1].setBounds(r);

        rb.setCompoundDrawables(null,drs[1],null,null);
//        userListRb.setCompoundDrawables(null,drs[1],null,null);
    }

//    void initEasy(){
//        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
//            @Override
//            public void onContactAdded(String username) {
//                //增加了联系人时回调此方法
//                Log.e("环信消息","添加了好友" + username);
//            }
//
//            @Override
//            public void onContactDeleted(String username) {
//                //被删除时回调此方法
//                Log.e("环信消息","删除了好友" + username);
//            }
//
//            @Override
//            public void onContactInvited(final String username, String reason) {
//                //收到好友邀请
//                Log.e("环信消息",username+"请求添加你为好友，因为"+reason);
//                new Thread(){
//                    @Override
//                    public void run() {
//                        try{
//                            EMClient.getInstance().contactManager().acceptInvitation(username);
//                        }catch (HyphenateException exception){
//                            Log.e("环信错误消息","同意" + username+"添加我为好友失败"+exception.getMessage()+"   "+exception.getErrorCode());
//                        }
//                    }
//                }.start();
//
//            }
//
//
//                        @Override
//            public void onContactAgreed(String username) {
//                //好友请求被同意
//                Log.e("环信消息",username+"同意添加你为好友");
//            }
//
//            @Override
//            public void onContactRefused(String username) {
//                //好友请求被拒绝
//                Log.e("环信消息",username+"拒绝添加你为好友");
//            }
//        });
//    }

    private void switchFragment(int i){
        Fragment fragment=null;

        switch (i){
            case 0:
                fragment=new FragmentMessage();
                break;
            case 1:
                fragment=new FragmentSchedule();
                break;
            case 2:
                fragment=new FragmentUserList();
                break;
            case 3:
                fragment=new FragmentPersonal();
                break;
            default:

                break;
        }

        FragmentTransaction transaction=this.getFragmentManager().beginTransaction();
        transaction.replace(R.id.doctor_main_ll,fragment);
        transaction.commitAllowingStateLoss();


    }
    private void setFragmentPage(){
        mainRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
                switch (checkId){
                    case R.id.doctor_message_rb:
                        switchFragment(0);
                        break;
                    case R.id.doctor_schedule_rb:
                        switchFragment(1);
                        break;
                    case R.id.doctor_userlist_rb:
                        switchFragment(2);
                        break;
                    case R.id.doctor_personal_rb:
                        switchFragment(3);
                        break;
                    default:

                        break;
                }
            }
        });
    }
}

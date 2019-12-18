package com.jxj.jdoctorassistant.main.doctor.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.EaseConstant;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.DoctorMessageListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.EasyConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.bean.MessageBean;
import com.jxj.jdoctorassistant.bean.SignListBean;
import com.jxj.jdoctorassistant.bean.Urls;
import com.jxj.jdoctorassistant.main.doctor.ApplySignListActivity;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnItemClickListener;
import com.jxj.jdoctorassistant.main.doctor.adapter.SignAdapter;
import com.jxj.jdoctorassistant.main.doctor.schedule.ScheduleActivity;
import com.jxj.jdoctorassistant.main.doctor.schedule.ScheduleRecordActivity;
import com.jxj.jdoctorassistant.main.doctor.schedule.ScheduleSetActivity;
import com.jxj.jdoctorassistant.main.easymob.ChatActivity;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.EasyThread;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FragmentMessage extends Fragment implements OnItemClickListener {

    @ViewInject(R.id.doctor_message_schedule_title_tv)
    TextView scheduleTitleTv;
    @ViewInject(R.id.doctor_message_schedule_content_tv)
    TextView scheduleContentTv;

    @ViewInject(R.id.doctor_message_sign_title_tv)
    TextView signTitleTv;
    @ViewInject(R.id.doctor_message_sign_content_tv)
    TextView signContentTv;


    @ViewInject(R.id.doctor_message_lv)
    private RecyclerView messageLv;

    DoctorMessageListAdapter adapter;

    private Context context;
    private JSONArray array;
    private EasyThread getContactThread,addContactThread;
    private ImageView img_point_task,img_point_sign;
    DoctorSHThread doctorSHThread;

    List<MessageBean> list = new ArrayList<>();

    @OnClick({R.id.doctor_message_schedule_rl,R.id.doctor_message_sign_rl})
    void onClick(View view){
        switch (view.getId()){
            case R.id.doctor_message_schedule_rl:
                Intent intent = new Intent(context,ScheduleActivity.class);
                startActivity(intent);
                break;
            case R.id.doctor_message_sign_rl:
                startActivity(new Intent(context, ApplySignListActivity.class));
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_message,container,false);
        ViewUtils.inject(this,view);

        context=getActivity();
        init(view);
        initData();
//        getContact();
        return view;
    }


    public void init(View view){
        img_point_task = view.findViewById(R.id.img_point_task);
        img_point_sign = view.findViewById(R.id.img_point_sign);

    }

    public void initData(){
        //获取主页消息
        @SuppressLint("HandlerLeak") Handler handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = doctorSHThread.getResult();
                    Log.d( "handleMessage: ",result);
                    net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
                    int code = json.getInt("code");
                    if(code ==200) {
                        if(json.getInt("TaskCounts")>0) {
                            img_point_task.setVisibility(View.VISIBLE);
                            scheduleTitleTv.setText("日程待办"+"("+json.getInt("TaskCounts")+")");
                        }
                        if(json.getInt("SignCounts")>0) {
                            img_point_sign.setVisibility(View.VISIBLE);
                            signTitleTv.setText("申请签约"+"("+json.getInt("SignCounts")+")");
                        }
                    }else {
                        Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        doctorSHThread = new DoctorSHThread(ApiConstant.GETMSGMAININFO,handler,context);
        doctorSHThread.setDoctorId(MyApplication.Doctor_ID);
        doctorSHThread.start();

    }

    void addContact(String username){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_EASY_HANDLER){
//                    list= addContactThread.getUserList();
//                    for(String s:list){
//                        System.out.println("好友 》》》》》》s:"+s);
//                    }
//                    adapter.setList(list);
//                    adapter.notifyDataSetChanged();
                }
            }
        };
        addContactThread =new EasyThread(context,handler, EasyConstant.ADDCONTACT);
        addContactThread.setContactName(username);
        addContactThread.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d( "onDestroy: ","完蛋了艹");
    }

    @Override
    public void onItemClick(View view) {
        int position = messageLv.getChildLayoutPosition(view);
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra("s", (Serializable) list.get(adapter.getItemCount()));
//        startActivity(new Intent(context, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, list.get(position)));
    }
}

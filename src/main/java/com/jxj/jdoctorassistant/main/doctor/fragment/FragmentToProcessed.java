package com.jxj.jdoctorassistant.main.doctor.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.DoctorMessageListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.bean.MessageBean;
import com.jxj.jdoctorassistant.bean.ToProcessedBean;
import com.jxj.jdoctorassistant.bean.Urls;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnItemClickListener;
import com.jxj.jdoctorassistant.main.doctor.adapter.ToProcessedAdapter;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.sf.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentToProcessed extends android.support.v4.app.Fragment implements OnItemClickListener {
    RecyclerView list_to_processed;
    ToProcessedAdapter toProcessedAdapter;
    DoctorSHThread getScheduleThread;
    Context context;
    List<ToProcessedBean> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_processed, container, false);
        init(view);
        initData();
        context = getContext();
        return view;
    }

    public void init(View view){
        list_to_processed = view.findViewById(R.id.list_to_processed);

    }

    public void initData(){
        long time = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        String t = formatter.format(date);
        Log.d( "时间 ",t);

        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getScheduleThread.getResult();
                    net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
                    int code = json.getInt("code");
                    if(code ==200) {
                        JSONArray jsonArray = json.getJSONArray("Data");
                        Gson gson = new Gson();
                        list = gson.fromJson(jsonArray.toString(),new TypeToken<List<ToProcessedBean>>(){}.getType());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                        toProcessedAdapter = new ToProcessedAdapter(list,context,FragmentToProcessed.this);
                        list_to_processed.setLayoutManager(layoutManager);
                        list_to_processed.setAdapter(toProcessedAdapter);
                    }else {
                        Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        getScheduleThread = new DoctorSHThread(ApiConstant.GETSHSCHEDULEC,handler,context);
        getScheduleThread.setDate(t);
        getScheduleThread.setDoctorId(MyApplication.Doctor_ID);
        getScheduleThread.start();
    }

    @Override
    public void onItemClick(View view) {

    }
}

package com.jxj.jdoctorassistant.main.doctor.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.bean.SignListBean;
import com.jxj.jdoctorassistant.bean.Urls;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnSignClickListener;
import com.jxj.jdoctorassistant.main.doctor.adapter.SignAdapter;
import com.jxj.jdoctorassistant.main.doctor.thread.GetSignThread;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentSign extends android.support.v4.app.Fragment implements OnSignClickListener,View.OnClickListener {
    private RecyclerView list_sign;
    private SignAdapter signAdapter;
    private PopupWindow popupWindow;
    private View popview;
    private Button btn_confirm,btn_cancel;
    private RelativeLayout rl_cancel;
    List<SignListBean> list = new ArrayList<>();
    GetSignThread getSignThread;
    DoctorSHThread signThread;
    private int position = 0;
    private int id ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        init(view);
        initData();
        popu_service();
        return view;
    }

    public void init(View view){
        list_sign = view.findViewById(R.id.list_sign);

    }

    public void initData(){
        @SuppressLint("HandlerLeak") Handler handler  = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getSignThread.getResult();
                    Log.d( "handleMessage: ",result);
                    net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
                    int code = json.getInt("code");
                    if(code ==200) {
                        net.sf.json.JSONArray jsonArray = json.getJSONArray("DataList");
                        Gson gson = new Gson();
                        list = gson.fromJson(jsonArray.toString(),new TypeToken<List<SignListBean>>(){}.getType());
                        signAdapter = new SignAdapter(getContext(),list,FragmentSign.this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                        list_sign.setLayoutManager(layoutManager);
                        list_sign.setAdapter(signAdapter);
                    }else {
                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        getSignThread = new GetSignThread(ApiConstant.GETSHSIGNRECORDLIST ,handler,getContext());
        getSignThread.setDoctor_id(MyApplication.Doctor_ID);
        getSignThread.setPageIndex(0);
        getSignThread.setPageSize(10);
        getSignThread.start();
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onConfirmClick(final int position, final int signId) {  //同意签约

        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = signThread.getResult();
                    net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
                    int code = json.getInt("code");
                    if(code ==200) {
                        list.get(position).setState(0x02);
                        signAdapter.updateData(list);
                    }else {
                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        signThread = new DoctorSHThread(ApiConstant.SHSIGNAGREE,handler,getContext());
        signThread.setDoctorId(MyApplication.Doctor_ID);
        signThread.setSignId(signId);
        signThread.start();
    }

    @Override
    public void onCancelClick(int position,int signId) {
        popupWindow.showAtLocation(list_sign,Gravity.CENTER,0,-50);
        backgroundAlpha(0.5f);
        this.position = position;
        id = signId;
    }


    @SuppressLint("WrongConstant")
    private void popu_service() {
        // TODO Auto-generated method stub
        popview = LayoutInflater.from(getContext()).inflate(
                R.layout.popu_sign, null);
        btn_confirm = popview.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
       btn_cancel = popview.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        rl_cancel = popview.findViewById(R.id.rl_cancel);
        rl_cancel.setOnClickListener(this);
        popupWindow = new PopupWindow(popview,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new FragmentSign.poponDismissListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:  //确认拒绝
                refuse(position,id);
                popupWindow.dismiss();
                break;
            case R.id.btn_cancel:
                popupWindow.dismiss();
                break;
            case R.id.rl_cancel:
                popupWindow.dismiss();
                break;
                default:
                    break;
        }
    }


    //拒绝签约
    public void refuse(final int position , int signId){
        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = signThread.getResult();
                    net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
                    int code = json.getInt("code");
                    if(code ==200) {
                        list.get(position).setState(0x03);
                        signAdapter.updateData(list);
                    }else {
                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        signThread = new DoctorSHThread(ApiConstant.SHSIGNREFUSE,handler,getContext());
        signThread.setDoctorId(MyApplication.Doctor_ID);
        signThread.setSignId(signId);
        signThread.start();
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            // Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp =getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        if (bgAlpha == 1) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getActivity().getWindow().setAttributes(lp);
    }

}
